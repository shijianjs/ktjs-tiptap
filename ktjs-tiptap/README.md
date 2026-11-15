# ktjs-tiptap (module)

Kotlin/JS wrapper for [`@tiptap/core`](https://www.npmjs.com/package/@tiptap/core) 3.34.0 and
[`@tiptap/starter-kit`](https://www.npmjs.com/package/@tiptap/starter-kit) 3.34.0 — the headless
rich-text editor and its batteries-included extension set.

One npm scope, one Gradle module, one Kotlin package (`io.github.shijianjs.ktjs.tiptap`) with a
`core` sub-package for the options, the commands, the StarterKit factory and the events.

## Installation

```kotlin
dependencies {
    implementation("io.github.shijianjs:ktjs-tiptap:3.34.0-kt.1")
}
```

Tiptap is headless: it renders a bare contenteditable and nothing else, so the page supplies its own
CSS. `injectCSS` (default `true`) only adds the minimum Tiptap needs to behave.

## Usage

```kotlin
import io.github.shijianjs.ktjs.tiptap.core.HeadingAttributes
import io.github.shijianjs.ktjs.tiptap.core.editorOptions
import io.github.shijianjs.ktjs.tiptap.core.headingOptions
import io.github.shijianjs.ktjs.tiptap.core.starterKit
import io.github.shijianjs.ktjs.tiptap.core.starterKitOptions
import io.github.shijianjs.ktjs.tiptap.createEditor

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

editor.chain().selectAll().toggleBold().run()
editor.chain().selectAll().setHeading(HeadingAttributes(2.0)).run()

println(editor.getText())
```

- Three command shapes, three interfaces: `chain()` returns `TiptapChainedCommands` (each method returns
  the chain, `run()` dispatches), `commands` returns `TiptapSingleCommands` (one call, boolean result),
  and `can()` returns `TiptapCanCommands` (boolean per command, nothing dispatched).
- The command set is the one StarterKit guarantees. A command whose extension was configured away does
  not exist at runtime — `bold = false` makes `editor.commands.toggleBold` `undefined`, so guard it.
- Initialisation finishes on a timer: `isInitialized` and the `create` event both flip there. Use the
  `onCreate` option or wait a macrotask; content is readable immediately after construction.
- `setContent` is a command, not a method: `editor.chain().setContent(html).run()`.
- Options, attribute objects and StarterKit switches are `unsafeJso` builders, so unassigned fields stay
  absent and Tiptap's own defaults apply.
- Extension packages other than StarterKit (`extension-table`, `-image`, `-mention`, …) are not wrapped:
  add their npm dependency and pass the imported extension into `extensions`. The commercial
  `@tiptap-pro/*` extensions are deliberately out of scope.

## Build and test

```bash
./gradlew :ktjs-tiptap:jsBrowserTest   # Chromium: mounts a real editor, drives the command chain
./gradlew :demo:e2eTest                # Playwright against the demo page
```

After adding or upgrading an npm dependency, refresh the shared lock file with the official registry
(the lock file is committed, and CI has no mirror):

```bash
./gradlew --stop
$env:npm_config_registry='https://registry.npmjs.org'
./gradlew kotlinUpgradePackageLock
```

## License

MIT, same as the upstream packages:

> MIT License
> Copyright (c) 2025, Tiptap GmbH

Tiptap also sells **Tiptap Pro**, commercial extensions published under the `@tiptap-pro/*` scope on
Tiptap's own registry. None of them is wrapped here and none is referenced by this build; installing one
puts you under its own commercial terms. See
[docs/guide/license](https://shijianjs.github.io/ktjs-tiptap/guide/license).
