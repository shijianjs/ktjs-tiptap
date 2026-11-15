# ktjs-tiptap

Tiptap 的 Kotlin/JS 绑定 —— 无头富文本编辑器

封装覆盖 Tiptap 3.34：`@tiptap/core`（`Editor` 类、选项、内容访问、选区助手、三种命令形态、插件注册、事件）与 `@tiptap/starter-kit`（作为可逐扩展开关的工厂）。命令接口显式写出 StarterKit 保证提供的那一套，因为上游是按「装了哪些扩展」推导命令类型的。初始化是异步的，`create` 是事件、`isInitialized` 在下一个 tick 才翻转；内容在构造完立刻可读。封装到的部分都是 MIT；`@tiptap-pro/*` 商业扩展有意不在范围内。

- 上游包：[`@tiptap/core`](https://github.com/ueberdosis/tiptap) 3.34.0
- 文档：https://shijianjs.github.io/ktjs-tiptap/
- 演示：https://shijianjs.github.io/ktjs-tiptap/demo/
- API：https://shijianjs.github.io/ktjs-tiptap/api/
- 英文说明：[README.md](README.md)

## 用法

```kotlin
implementation("io.github.shijianjs:ktjs-tiptap:3.34.0-kt.1")
```

## 构建

```bash
./gradlew build
./gradlew :ktjs-tiptap:jsBrowserTest
./gradlew :demo:e2eTest
./gradlew pagesSite
```

## 许可证

wrapper 与上游 `@tiptap/core` 同为 MIT；版权声明见文档的「许可证」页。
