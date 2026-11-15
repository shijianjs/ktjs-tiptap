# ktjs-tiptap

Kotlin/JS bindings for Tiptap — headless rich text editor

The wrapper covers Tiptap 3.34: `@tiptap/core` (the `Editor` class, its options, content access, selection helpers, the three command shapes, plugin registration, events) and `@tiptap/starter-kit` (as a factory with per-extension switches). The command interfaces spell out the set StarterKit guarantees, because upstream derives command types from whichever extensions are installed. Initialisation is asynchronous, so `create` is an event and `isInitialized` flips on the next tick; content is readable immediately after construction. Everything wrapped is MIT; the `@tiptap-pro/*` commercial extensions are deliberately out of scope.

- Upstream package: [`@tiptap/core`](https://github.com/ueberdosis/tiptap) 3.34.0
- Docs: https://shijianjs.github.io/ktjs-tiptap/
- Demo: https://shijianjs.github.io/ktjs-tiptap/demo/
- API: https://shijianjs.github.io/ktjs-tiptap/api/
- 中文说明：[README.zh-CN.md](README.zh-CN.md)

## Usage

```kotlin
implementation("io.github.shijianjs:ktjs-tiptap:3.34.0-kt.1")
```

## Build

```bash
./gradlew build
./gradlew :ktjs-tiptap:jsBrowserTest
./gradlew :demo:e2eTest
./gradlew pagesSite
```

## License

The wrapper is MIT, same as the upstream `@tiptap/core` — see the "License" page of the docs.
