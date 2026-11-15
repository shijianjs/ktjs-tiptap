# TODO

## ktjs-tiptap 上游 API 对齐

把上游 `@tiptap/core` 3.34.0-kt.1 的类型声明逐个落成 Kotlin/JS 声明，并让 demo、文档、
测试与之一致。

- 总计：96
- 已完成：68
- 明确不做：6
- 待完成：22

详细清单：[coverage.md](docs/development/coverage.md)
技术决策：[technical-decisions.md](docs/development/technical-decisions.md)

### 当前待办

- [ ] `focusOn` 式的 `$node` / `$nodes` / `$pos` / `$doc` 位置助手
- [ ] `utils`（`Utils` 类，位置与范围的工具集）
- [ ] `storage` / `schema` / `view` / `state` 的具体类型（目前是 `JsAny`）
- [ ] `registerPlugin` 的 `Plugin` 与 `PluginKey` 类型（来自 `@tiptap/pm`）
- [ ] `editorProps` 的完整 `EditorProps` 结构（attributes、handlers、nodeViews）
- [ ] `editorOptions.enableCoreExtensions`、`coreExtensionOptions`、`textDirection` 等其余选项
- [ ] `EditorEvents` 的 `contentError` / `beforeCreate` 负载结构
- [ ] 单个扩展包（`@tiptap/extension-table`、`-image`、`-mention`、`-text-align` 等）单独封装
- [ ] 扩展类的 `configure` / `extend` 通用能力
- [ ] `@tiptap/react` / `-vue` 绑定
- [ ] `@tiptap/static-renderer`（服务端渲染）
- [ ] 上游升级后的声明对齐

### 注意事项

- 上游 `@tiptap/core`、`@tiptap/starter-kit` 与各 `@tiptap/extension-*` 都是 MIT；付费的 `@tiptap-pro/*` 不在范围内，构建里也没有引用。
- 编辑器是「无头」的：它只给出 contenteditable，页面要自己提供排版样式；`injectCSS` 只补最小样式。
- 初始化在 `setTimeout(…, 0)` 里收尾，`isInitialized` 与 `create` 事件都在那时发生；内容在构造完立刻可读。
- 被 `starterKitOptions` 关掉的扩展，其命令在运行时是 `undefined`：`bold = false` 后再调 `toggleBold` 会抛 TypeError，测试用运行时 `typeof` 断言这一点。
- 代码块的语言落到 `class="language-…"`，不是 `data-language`。
- 演示页的工具栏按钮靠 `focus()` 恢复编辑器自身的选区，因此 e2e 用编辑器自己的 `Control+a` 绑定选区，而不是从外部设 DOM range。
