---
sidebar_position: 3
title: API overview
---

# API overview

## Package layout

| Package | Contents |
| --- | --- |
| `io.github.shijianjs.ktjs.tiptap` | `Editor`, `createEditor`, `tiptapEditor`, `emptyTiptapEditor`, `TiptapExtension`, `TiptapCoreModule` |
| `…tiptap.core` | `EditorOptions`, `editorOptions`, `ParseOptions`, `parseOptions`, `LinkAttributes`, `HeadingAttributes`, `CodeBlockAttributes`, `codeBlockAttributes`, `StarterKitOptions`, `starterKitOptions`, `starterKit`, `HeadingOptions`, `headingOptions`, `StarterKitNames`, `TiptapChainedCommands`, `TiptapSingleCommands`, `TiptapCanCommands`, `TiptapEvents`, `TiptapEventPayload` |

## Conventions this wrapper follows

- **Three command shapes, three interfaces.** Upstream derives every command type from the installed
  extensions, via one big union. The wrapper declares the StarterKit set explicitly:
  `chain()` answers [TiptapChainedCommands] (every method returns the chain, `run()` dispatches),
  `commands` answers [TiptapSingleCommands] (one call, boolean result), and `can()` answers
  [TiptapCanCommands] (boolean per command, nothing dispatched).
- **Initialisation is asynchronous.** Tiptap finishes in a `setTimeout(…, 0)`, which sets
  `isInitialized` and emits `create`. Listen for the event or check the flag after a macrotask;
  reading content right after construction already works, because the document is built eagerly.
- **Options are `unsafeJso` builders.** `editorOptions`, `parseOptions`, `starterKitOptions`,
  `headingOptions` leave unassigned fields absent rather than `null`, so Tiptap's own defaults apply.
- **StarterKit is a factory, not a constant.** `starterKit(options)` calls `StarterKit.configure(options)`
  and hands back the configured extension; `starterKitOptions { bold = false }` is how a piece is left
  out.
- **Commands that a configuration removed do not exist.** With `bold = false`, `editor.commands.toggleBold`
  is `undefined`; the wrapper cannot type that away, so guard it with `can()` / a runtime check.

## What is deliberately typed loosely

| Member | Kotlin type | Why |
| --- | --- | --- |
| `EditorOptions.content` | `JsAny?` | upstream is `string \| JSONContent \| JSONContent[] \| null`; strings and arrays are `JsAny` already |
| `EditorOptions.extensions` | `Array<TiptapExtension>` | an extension instance, one opaque type for every `@tiptap/extension-*` package |
| `EditorOptions.editorProps` | `JsAny?` | ProseMirror view props: attributes, handlers, node views |
| `EditorOptions.autofocus` | `JsAny?` | `'start' \| 'end' \| 'all' \| number \| boolean \| null` |
| `EditorOptions.enableInputRules` / `enablePasteRules` | `JsAny?` | `boolean \| string[] \| { … }` |
| `Editor.getJSON()` | `JsAny` | a ProseMirror document tree |
| event payloads | `JsAny` | each event carries its own object; cast to `TiptapEventPayload` for `editor` |

Every row is a real upstream shape, and each is recorded in `docs/development/technical-decisions.md`.
