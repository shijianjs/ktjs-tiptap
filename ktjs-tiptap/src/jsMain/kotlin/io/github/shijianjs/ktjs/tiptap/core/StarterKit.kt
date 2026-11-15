package io.github.shijianjs.ktjs.tiptap.core

import io.github.shijianjs.ktjs.tiptap.TiptapExtension
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.JsName
import kotlin.js.unsafeCast

/** The `@tiptap/starter-kit` module: it exports the configured `StarterKit` extension. */
@JsModule("@tiptap/starter-kit")
external object StarterKitModule {
    /** The pre-configured `StarterKit` extension. */
    @JsName("StarterKit")
    val StarterKit: JsAny
}

private val configureStarterKit: (JsAny, StarterKitOptions) -> TiptapExtension =
    js("(starterKit, options) => starterKit.configure(options)")

/**
 * Builds the StarterKit extension set: bold, italic, strike, code, underline, headings, blockquote,
 * bullet and ordered lists, code block, hard break, horizontal rule, link, paragraph, document, text,
 * undo/redo, dropcursor, gapcursor, list keymap and the trailing node.
 *
 * ```kotlin
 * extensions = arrayOf(
 *     starterKit(starterKitOptions {
 *         heading = headingOptions { levels = arrayOf(1.0, 2.0, 3.0) }
 *     }),
 * )
 * ```
 */
fun starterKit(options: StarterKitOptions = starterKitOptions {}): TiptapExtension =
    configureStarterKit(StarterKitModule.StarterKit, options)

/**
 * Which StarterKit pieces to install, and with what settings — upstream `StarterKitOptions`.
 *
 * Setting a piece to `false` leaves it out; the object form overrides its options. An empty builder
 * installs everything with the defaults.
 */
external interface StarterKitOptions {
    /** Blockquote settings, or `false` to leave it out. */
    var blockquote: JsAny?

    /** Bold settings, or `false` to leave it out. */
    var bold: JsAny?

    /** Bullet-list settings, or `false` to leave it out. */
    var bulletList: JsAny?

    /** Inline-code settings, or `false` to leave it out. */
    var code: JsAny?

    /** Code-block settings, or `false` to leave it out. */
    var codeBlock: JsAny?

    /** `false` leaves the document node out; the editor needs it, so this is a footgun. */
    var document: JsAny?

    /** Drop-cursor settings, or `false` to leave it out. */
    var dropcursor: JsAny?

    /** `false` leaves the gap cursor out. */
    var gapcursor: JsAny?

    /** Hard-break settings, or `false` to leave it out. */
    var hardBreak: JsAny?

    /** Heading settings, or `false` to leave it out. */
    var heading: JsAny?

    /** Undo/redo settings, or `false` to leave the history commands out. */
    var undoRedo: JsAny?

    /** Horizontal-rule settings, or `false` to leave it out. */
    var horizontalRule: JsAny?

    /** Italic settings, or `false` to leave it out. */
    var italic: JsAny?

    /** List-item settings, or `false` to leave it out. */
    var listItem: JsAny?

    /** List keymap settings, or `false` to leave the list keyboard shortcuts out. */
    var listKeymap: JsAny?

    /** Link settings, or `false` to leave the link mark out. */
    var link: JsAny?

    /** Ordered-list settings, or `false` to leave it out. */
    var orderedList: JsAny?

    /** Paragraph settings, or `false` to leave it out. */
    var paragraph: JsAny?

    /** Strikethrough settings, or `false` to leave it out. */
    var strike: JsAny?

    /** `false` leaves the text node out; the editor needs it. */
    var text: JsAny?

    /** Underline settings, or `false` to leave it out. */
    var underline: JsAny?

    /** Trailing-node settings, or `false` to leave it out. */
    var trailingNode: JsAny?
}

/** Builds [StarterKitOptions], setting only the pieces the block assigns. */
inline fun starterKitOptions(block: StarterKitOptions.() -> Unit): StarterKitOptions =
    unsafeJso(block)

/**
 * Heading settings, upstream `HeadingOptions`.
 *
 * The wrapper declares the field that matters for a configuration (`levels`); the rest of the
 * extension option object (`HTMLAttributes`, `modifiers`) stays reachable through
 * [headingOptionsRaw] for projects that need it.
 */
external interface HeadingOptions {
    /** The heading levels to allow, 1 to 6; defaults to all six. */
    var levels: Array<Double>?
}

/** Builds [HeadingOptions]. */
inline fun headingOptions(block: HeadingOptions.() -> Unit): HeadingOptions = unsafeJso(block)

/** Turns a [HeadingOptions] into the `JsAny` that [StarterKitOptions.heading] expects. */
fun headingOptionsRaw(options: HeadingOptions): JsAny = options.unsafeCast<JsAny>()

/** The names StarterKit installs, as the extension objects report them. */
object StarterKitNames {
    /** The blockquote node. */
    inline val Blockquote: String get() = "blockquote"

    /** The bold mark. */
    inline val Bold: String get() = "bold"

    /** The bullet-list node. */
    inline val BulletList: String get() = "bulletList"

    /** The inline-code mark. */
    inline val Code: String get() = "code"

    /** The code-block node. */
    inline val CodeBlock: String get() = "codeBlock"

    /** The document node. */
    inline val Document: String get() = "doc"

    /** The hard-break node. */
    inline val HardBreak: String get() = "hardBreak"

    /** The heading node. */
    inline val Heading: String get() = "heading"

    /** The horizontal-rule node. */
    inline val HorizontalRule: String get() = "horizontalRule"

    /** The italic mark. */
    inline val Italic: String get() = "italic"

    /** The list-item node. */
    inline val ListItem: String get() = "listItem"

    /** The link mark. */
    inline val Link: String get() = "link"

    /** The ordered-list node. */
    inline val OrderedList: String get() = "orderedList"

    /** The paragraph node. */
    inline val Paragraph: String get() = "paragraph"

    /** The strikethrough mark. */
    inline val Strike: String get() = "strike"

    /** The text node. */
    inline val Text: String get() = "text"

    /** The underline mark. */
    inline val Underline: String get() = "underline"

    /** The undo/redo extension. */
    inline val UndoRedo: String get() = "undoRedo"

    /** The drop-cursor extension. */
    inline val DropCursor: String get() = "dropCursor"

    /** The gap-cursor extension. */
    inline val GapCursor: String get() = "gapCursor"

    /** The trailing-node extension. */
    inline val TrailingNode: String get() = "trailingNode"
}
