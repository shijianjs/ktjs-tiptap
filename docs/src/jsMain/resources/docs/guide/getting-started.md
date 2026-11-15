---
sidebar_position: 2
title: Getting started
---

# Getting started

## Dependency

```kotlin
kotlin {
    js {
        binaries.executable()
        browser()
    }
    sourceSets {
        jsMain.dependencies {
            implementation("io.github.shijianjs:ktjs-tiptap:3.34.0-kt.1")
            // The public API exposes `kotlin-browser`'s `web.*` types, so a consumer must be able to
            // compile against them.
            implementation("org.jetbrains.kotlin-wrappers:kotlin-browser:2026.11.1")
        }
    }
}
```

Kotlin 2.4.21 or newer; the module emits ESM and targets `es2015` or later.

## Register the plugins

Tiptap has no plugin registry: **extensions are objects you pass in**, and every `@tiptap/extension-*` package is its own npm dependency. This wrapper ships two of them, the two a project needs to get started:

```kotlin
import io.github.shijianjs.ktjs.tiptap.createEditor
import io.github.shijianjs.ktjs.tiptap.core.editorOptions
import io.github.shijianjs.ktjs.tiptap.core.starterKit
```

`StarterKit` is one extension that bundles bold, italic, strike, code, underline, headings, blockquote,
bullet and ordered lists, code block, hard break, horizontal rule, link, paragraph, document, text,
undo/redo, dropcursor, gapcursor, list keymap and the trailing node. Every other extension
(`extension-table`, `extension-image`, `extension-mention`, …) is out of scope here: add its npm
dependency yourself and pass the imported extension into `extensions` as `JsAny`.

The paid part of the ecosystem lives under the `@tiptap-pro/*` scope on Tiptap's own registry, and it
is **not** wrapped — see [License](../license).

## First call

```kotlin
import io.github.shijianjs.ktjs.tiptap.core.HeadingAttributes
import io.github.shijianjs.ktjs.tiptap.core.editorOptions
import io.github.shijianjs.ktjs.tiptap.core.headingOptions
import io.github.shijianjs.ktjs.tiptap.core.starterKit
import io.github.shijianjs.ktjs.tiptap.core.starterKitOptions
import io.github.shijianjs.ktjs.tiptap.createEditor

fun mount(host: web.html.HTMLElement) {
    val editor = createEditor(editorOptions {
        element = host
        content = "<p>Hello <strong>world</strong></p>"
        extensions = arrayOf(
            starterKit(starterKitOptions {
                heading = headingOptions { levels = arrayOf(1.0, 2.0, 3.0) }
            }),
        )
        onUpdate = { println(editor.getHTML()) }
    })

    // A chain runs several commands in one transaction.
    editor.chain().selectAll().toggleBold().run()
    editor.chain().selectAll().setHeading(HeadingAttributes(2.0)).run()

    println(editor.getText())
}
```

Tiptap is headless: the editor element is a bare contenteditable, so a page that wants borders,
spacing or typography supplies its own CSS. `injectCSS` (default `true`) only adds the bare minimum.

## Build and test

```bash
./gradlew build                     # compile, Detekt, browser tests
./gradlew :ktjs-tiptap:jsBrowserTest     # only the wrapper's browser tests
./gradlew :docs:docusaurusStart     # documentation dev server
./gradlew pagesSite                 # docs at /, demo at /demo/, Dokka at /api/
```
