# Technical Decisions

`@tiptap/core` 3.34 的类型是从「装了哪些扩展」推导出来的：`SingleCommands`、`ChainedCommands` 都是
`UnionCommands` 的映射类型，而 `UnionCommands` 由各扩展通过 module augmentation 往里加命令。落到
Kotlin/JS external 声明时，这个「开放式联合」是最大的难点，下面的决定都围绕它。

## 1. 模块当命名空间绑，`Editor` 按名字取

`@tiptap/core` 同时有 ESM 与 CJS 构建，`Editor` 在两者里都是具名导出。把模块绑成对象再取成员，两种
解析方式都能工作：

```kotlin
@JsModule("@tiptap/core")
external object TiptapCoreModule {
    @JsName("Editor") val EditorClass: JsAny
}

private val constructEditor: (JsAny, EditorOptions) -> Editor =
    js("(cls, options) => new cls(options)")
```

**否决**：`@JsModule("...") external class Editor(...)` —— 依赖 default export 的互操作行为。

## 2. 命令集「显式写死」在三个接口里

上游的命令类型是开放联合，Kotlin 无法表达「装了 A 就有 a1、a2，装了 B 就多出 b1」。两条路：

1. 把命令集声明成 `JsAny` / `dynamic`，让调用方自己转型 —— 失去全部类型检查，不可接受。
2. 把 **StarterKit 保证提供的那一套**写成固定接口，超出部分留给调用方转型。

选了 2，并把同一套命令写成三种形态：

| 接口 | 来源 | 每个方法的返回 |
|---|---|---|
| `TiptapChainedCommands` | `editor.chain()` | 链本身，末尾 `run(): Boolean` |
| `TiptapSingleCommands` | `editor.commands` | `Boolean` |
| `TiptapCanCommands` | `editor.can()` | `Boolean`，且不提交任何事务 |

**踩过的坑**：第一版把 `run()` 只放在一个 `TiptapChainedResult` 子接口上，而命令方法返回基础接口，
于是 `chain().selectAll().toggleBold().run()` 里 `.run()` 解析到了 Kotlin 标准库的 `run(block)`，报
`No value passed for parameter 'block'`。修法是把 `run()` 直接放进 `TiptapChainedCommands`（上游
`ChainedCommands` 本来也是这么定义的）。

## 3. `SingleCommands` 的返回值统一成 `Boolean`

上游有几个命令声明为 `void`（`focus`、`blur` 等），其余返回 `boolean`。Kotlin 侧统一写 `Boolean`：
对 `void` 的那些，读到的是 `undefined`，Kotlin/JS 把它当 `false`。这样「命令有没有生效」这件事不会
静默丢失，代价是 `focus()` 永远为 `false` —— 调用方不该拿它的返回值判断焦点。

## 4. `can()` 也需要 `chain()`

上游 `CanCommands = SingleCommands & { chain: () => ChainedCommands }`。第一版漏了 `chain`，于是
`editor.can().chain().selectAll().run()` 编译失败。现在 `TiptapCanCommands` 里同时有 `chain()`。

另外记一条运行时行为：**被关掉的扩展，其命令在 `can()` 对象上也是 `undefined`**，不是「返回 false 的
函数」。所以 `starterKitOptions { bold = false }` 之后调 `can().toggleBold()` 会抛
`TypeError: toggleBold is not a function`。测试因此用 `typeof editor.commands[name]` 来判断命令是否存在。

## 5. 初始化是异步的，`create` 事件在下一个 tick

`Editor` 在构造末尾用 `setTimeout(…, 0)` 收尾，`isInitialized = true` 与 `create` 事件都在那里。
构造完监听 `on(TiptapEvents.Create)` 会永远收不到 —— 事件已经发过了。两条正确写法：

- 用选项里的 `onCreate` 回调（构造时注册，收尾时触发）；
- 或等到下一个宏任务再读 `isInitialized`。

测试里用 `Promise` + `setTimeout(cb, 0)` 的 `nextTick()`，Kotlin/JS 的 `kotlin.test` 支持返回
`Promise<Unit>` 的测试函数，Karma runner 会 await。

**注意**：`getHTML()` / `getText()` 在构造完立刻可用，文档是同步建的，只有「初始化完成」这个标志是
异步的。

## 6. 选项都是 `unsafeJso` builder

`editorOptions`、`parseOptions`、`starterKitOptions`、`headingOptions` 都是
`inline fun xxx(block: T.() -> Unit): T = unsafeJso(block)`。Tiptap 的默认值来自「键不存在」，
写入 `null` 会覆盖默认值 —— builder 保证没赋值的字段根本不出现。

## 7. StarterKit 是工厂，`configure` 通过 shim 调

`StarterKit` 在包里是一个**已经构造好的扩展实例**（`Extension<StarterKitOptions, any>`），想改配置得
调实例上的 `configure(options)`。因为实例在 wrapper 里是 `TiptapExtension`（不透明），用 shim 调：

```kotlin
private val configureStarterKit: (JsAny, StarterKitOptions) -> TiptapExtension =
    js("(starterKit, options) => starterKit.configure(options)")
```

## 8. `content` 用 `JsAny?`

上游 `Content = string | JSONContent | JSONContent[] | null`。Kotlin/JS 里 `String` 与 `Array` 本身就是
`JsAny`，所以一个 `JsAny?` 同时接受 HTML 字符串与 JSON 文档，不需要再造联合类型。

## 9. `extensions` 用 `Array<TiptapExtension>`

扩展实例来自各个 `@tiptap/extension-*` 包，字段（`name`、`options`、`storage`）在不同包里有不同形状。
声明一个不透明的 `TiptapExtension`（不写 `: JsAny`）足以承载 StarterKit 的返回值与调用方自带的扩展；
需要成员时由调用方转型，而不是让 wrapper 假装知道扩展的内部结构。

## 10. 属性对象用 `unsafeJso`，`level` 用 `Double`

- `LinkAttributes(href: String)` 是最常用形态，另有 block 形式设置 `target` / `rel` / `className`。
- `HeadingAttributes(level: Double)`：上游是 `1 | 2 | 3 | 4 | 5 | 6` 字面量联合，Kotlin 侧用 `Double`，
  超出范围由扩展自己 clamp —— 用 `sealed interface` 六个常量也能做，但会让 `for ((1..6))` 这类循环
  无法表达，收益不成比例。
- `CodeBlockAttributes.language` 是可选的单字段对象。

## 11. 事件名有常量，负载是 `JsAny`

`TiptapEvents` 收住 11 个事件名（`create`、`update`、`selectionUpdate`、`transaction`、`focus`、
`blur`、`destroy`、`contentError`、`mount`、`unmount`、`beforeCreate`），拼错即编译错误。负载是
`JsAny`：`update` 带 `{ editor, transaction }`，`contentError` 带 `{ editor, error, disableCollaboration }`，
声明成一个具体类型会按事件种类撒谎。需要 `editor` 时转型成 `TiptapEventPayload`。

## 12. `setContent` 是命令，不是方法

上游 `Editor` 没有 `setContent` 方法（第一版按习惯写了一个，运行时报
`editor.setContent is not a function`）。改文档要走命令：`editor.chain().setContent(html).run()`。
`Editor.setContent` 已从声明里删掉。

## 13. 演示页的刷新用「实例持有者」而不是直接捕获

`createEditor(editorOptions { onUpdate = { refresh() } })` 里 `refresh()` 需要访问 `editor`，而 `editor`
正是这次调用的返回值。Kotlin 的局部函数必须先声明后使用，于是改成：

```kotlin
var editorRef: Editor? = null
fun refresh() { val editor = editorRef ?: return; … }
val editor = createEditor(editorOptions { onUpdate = { refresh() } })
editorRef = editor
```

`onUpdate` 在构造期间可能触发一次，此时 `editorRef` 还是空，`refresh()` 直接返回；构造完再手动
`refresh()` 补齐一次。

## 14. 演示页的工具栏靠编辑器自身选区

工具栏按钮用 `chain().focus()` 恢复编辑器记录的选区。从外部设置 DOM range **不会**进入 ProseMirror
的 state，`toggleBold` 会作用在旧选区。因此 e2e 用「点进段落 + 编辑器自己的 `Control+a`」来选内容，
而不是 `document.createRange()`。

## 15. 断言按真实输出写，不按想象写

- 代码块的语言落成 `class="language-kotlin"`，不是 `data-language="kotlin"`；
- `selectAll().setHeading(2)` 之后 `isActive('heading')` 可能落在尾随的空段落上而为 `false`；
- 无头页面的 `focus` 事件在 Playwright 里不一定触发，用 `onTransaction` 计数来验证「命令确实提交了事务」；
- `isInitialized` 只能在下一个宏任务里断言。

这四条都写进了测试的注释，避免后来者把它们当 bug 重新排查。
