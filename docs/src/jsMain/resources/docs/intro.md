---
sidebar_position: 1
title: Introduction
---

# Introduction

`ktjs-tiptap` is a Kotlin/JS wrapper for [`@tiptap/core`](https://github.com/ueberdosis/tiptap) from [Tiptap](https://github.com/ueberdosis/tiptap) 3.34.0: every type and member of
the upstream `.d.ts` files becomes an `external` declaration the compiler checks, with no
`dynamic` fallback.

The wrapper covers `@tiptap/core` 3.34 and `@tiptap/starter-kit` 3.34, both MIT: the `Editor` class with its options, mounting, content access (`getHTML` / `getJSON` / `getText` / `setContent`), the selection helpers (`isActive`, `getAttributes`), the three command shapes (`chain`, `commands`, `can`), the plugin registration calls, and the events. The command set is the one StarterKit guarantees — the marks, the nodes, the lists, the history commands and the content commands — plus the attribute types those commands take. StarterKit itself is wrapped as a factory (`starterKit`) with its per-extension switches (`bold = false`, `heading = headingOptions { levels = … }` …).

## What is wrapped

| Module | Gradle artifact | Upstream |
| --- | --- | --- |
| `ktjs-tiptap` | `io.github.shijianjs:ktjs-tiptap` | `@tiptap/core` 3.34.0, `@tiptap/starter-kit` 3.34.0 |

One npm scope, one Gradle module, one Kotlin package (`io.github.shijianjs.ktjs.tiptap`) with a `core` sub-package for the options, the commands, the StarterKit factory and the events.

## License

The wrapper itself is MIT. The upstream `@tiptap/core` license differs from it — read the
[license notes](guide/license.md) before shipping it commercially.

## Where to go next

- [Getting started](guide/getting-started.md) — add the dependency, make the first call, run the build.
- [API overview](guide/api-overview.md) — package layout, type mapping conventions, common patterns.
- [Demo](guide/demo.md) — see it running in the browser.
