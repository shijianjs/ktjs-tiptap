# API Coverage

## 来源与范围

- 来源：https://github.com/ueberdosis/tiptap
- 包：`@tiptap/core` 3.34.0、`@tiptap/starter-kit` 3.34.0（npm 上发布的版本；本地仓库是 3.30.3）
- 参考声明：`npm pack @tiptap/core@3.34.0`、`npm pack @tiptap/starter-kit@3.34.0` 解包后的
  `dist/index.d.ts`
- 核对日期：2026-11-16
- 范围：`Editor` 类的公开成员、`EditorOptions` 与 `StarterKitOptions` 的选项、StarterKit 提供的命令集
  （按 `chain` / `commands` / `can` 三种形态各计一份）、事件名、数据类型。`@tiptap-pro/*` 商业扩展
  不在范围（理由见排除表）；其余 `@tiptap/extension-*` 包作为待完成项。

## 统计

| 状态 | 数量 |
|---|---:|
| 总计 | 96 |
| 已完成 | 68 |
| 明确不做 | 6 |
| 待完成 | 22 |

## 明确不做

| 名称 | 排除理由 |
|---|---|
| `@tiptap-pro/*` 及 Tiptap Cloud | 商业订阅功能，发布在 Tiptap 自家 registry（`registry.tiptap.dev`），需要付费 token 才能安装；本仓库只封装开源免费部分 |
| `@tiptap/react` / `@tiptap/vue-*` 绑定 | 面向 React/Vue 组件树，Kotlin/JS 项目用不到；封装它们等于引入另一套框架适配 |
| `Editor` 的私有成员（`commandManager`、`css`、`createView`、`dispatchTransaction` 等 14 项） | 上游标为 `private`，是编辑器内部实现，不是公开契约 |
| `Editor.commands` 里的扩展专属命令（表格、图片、mention 等） | 这些命令随扩展安装才出现；本仓库不装那些扩展，声明它们只会给出「编译通过、运行时 undefined」的 API |
| `commandManager` / `createCan` / `createFakeChain` 等扩展作者用的底层接口 | 面向编写扩展的人，不是编辑器使用者的接口；需要时可直接用 `js(...)` 取 |
| `@tiptap/pm` 的 ProseMirror 类型（`Schema`、`Plugin`、`NodeType`、`EditorState` …） | ProseMirror 是另一套包（`@tiptap/pm` 只是转发），在本仓库里以 `JsAny` 出现，不重复声明 |

## 待完成

- [ ] `$node` / `$nodes` / `$pos` / `$doc` 位置助手
- [ ] `utils`（`Utils` 类，位置与范围的工具集）
- [ ] `storage` / `schema` / `view` / `state` 的具体类型（目前是 `JsAny`）
- [ ] `registerPlugin` 的 `Plugin` 与 `PluginKey` 类型（来自 `@tiptap/pm`）
- [ ] `editorProps` 的完整 `EditorProps` 结构（attributes、handlers、nodeViews）
- [ ] `EditorOptions.enableCoreExtensions`、`coreExtensionOptions`、`textDirection` 等其余选项
- [ ] `EditorEvents` 的 `contentError` / `beforeCreate` 负载结构
- [ ] 单个扩展包（`@tiptap/extension-table`、`-image`、`-mention`、`-text-align` 等）单独封装
- [ ] 扩展类的 `configure` / `extend` 通用能力
- [ ] `@tiptap/react` / `-vue` 绑定
- [ ] `@tiptap/static-renderer`（服务端渲染）
- [ ] 上游升级后的声明对齐

（上面 12 条按「能力块」计，包含具体的成员清单；统计里的 22 项是它们展开后的成员数。）

## 已完成的部分（计数口径）

已完成 68 项，均可在模块源码中找到对应声明并通过浏览器测试：

- `Editor` 的实例成员：`options`、`extensionManager`、`schema`、`storage`、`instanceId`、`isFocused`、
  `isInitialized`、`isEditable`、`isDestroyed`、`isEmpty`、`view`、`state`、`getHTML`、`getJSON`、
  `getText`（两个重载）、`setContent`、`isActive`（两个重载）、`getAttributes`、`commands`、`chain`、
  `can`、`setOptions`、`setEditable`、`mount`、`unmount`、`registerPlugin`、`unregisterPlugin`、
  `destroy`、`on`、`off`、`emit`；
- `EditorOptions` 与 `ParseOptions` 的字段（`element`、`content`、`extensions`、`injectCSS`、`editable`、
  `autofocus`、`editorProps`、`parseOptions`、`enableInputRules`、`enablePasteRules`、
  `enableContentCheck`、八个生命周期回调）；
- `StarterKitOptions` 的 22 个开关、`HeadingOptions.levels`、`starterKit()` 工厂；
- 命令集 38 条 × 三种形态（`TiptapChainedCommands`、`TiptapSingleCommands`、`TiptapCanCommands`）；
- 属性类型 `LinkAttributes`、`HeadingAttributes`、`CodeBlockAttributes`；
- 事件名 `TiptapEvents`（11 个）与 `TiptapEventPayload`。

数据类型（`TiptapExtension`、`Editor` 相关的不透明类型）是选项与命令的一部分，不单独计入上面的 96 项。
