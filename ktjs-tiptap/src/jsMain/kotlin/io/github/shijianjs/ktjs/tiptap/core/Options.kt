package io.github.shijianjs.ktjs.tiptap.core

import io.github.shijianjs.ktjs.tiptap.TiptapExtension
import js.objects.unsafeJso
import kotlin.js.JsAny

/**
 * The options an editor accepts, upstream `EditorOptions`.
 *
 * Built with [editorOptions]. Every field is optional upstream too: an editor with nothing set is
 * legal, it just draws nothing.
 */
external interface EditorOptions {
    /**
     * The element to bind to: the editor appends its own editable element inside it. `null` keeps the
     * editor unmounted, which `mount` can do later.
     */
    var element: web.dom.Element?

    /** The initial content: an HTML string, a JSON document, or a JSON array of documents. */
    var content: JsAny?

    /** The extensions to install; [io.github.shijianjs.ktjs.tiptap.TiptapExtension] instances. */
    var extensions: Array<TiptapExtension>?

    /** Whether to inject Tiptap's base CSS into the page. */
    var injectCSS: Boolean?

    /** Whether the content can be edited; `false` gives a read-only viewer. */
    var editable: Boolean?

    /** Where the caret goes on mount: `"start"`, `"end"`, `"all"`, `true`, `false`, or a number. */
    var autofocus: JsAny?

    /** Attributes and handlers handed to the ProseMirror view, upstream `EditorProps`. */
    var editorProps: JsAny?

    /** How the content is parsed, upstream `ParseOptions`. */
    var parseOptions: ParseOptions?

    /** Whether input rules (markdown-style shortcuts) run while typing. */
    var enableInputRules: JsAny?

    /** Whether paste rules run on pasted content. */
    var enablePasteRules: JsAny?

    /** Whether to check the initial content and emit `contentError` when it does not fit the schema. */
    var enableContentCheck: Boolean?

    /** Called after the editor is constructed. */
    var onCreate: ((JsAny) -> Unit)?

    /** Called after the editor is mounted into its element. */
    var onMount: ((JsAny) -> Unit)?

    /** Called when the editor is unmounted. */
    var onUnmount: ((JsAny) -> Unit)?

    /** Called on every document change. */
    var onUpdate: ((JsAny) -> Unit)?

    /** Called when the selection moves. */
    var onSelectionUpdate: ((JsAny) -> Unit)?

    /** Called on every dispatched transaction. */
    var onTransaction: ((JsAny) -> Unit)?

    /** Called when the editor gains focus. */
    var onFocus: ((JsAny) -> Unit)?

    /** Called when the editor loses focus. */
    var onBlur: ((JsAny) -> Unit)?

    /** Called when the content cannot be parsed against the schema. */
    var onContentError: ((JsAny) -> Unit)?

    /** Called before the editor is destroyed. */
    var onDestroy: ((JsAny) -> Unit)?
}

/** Builds [EditorOptions], setting only the fields the block assigns. */
inline fun editorOptions(block: EditorOptions.() -> Unit): EditorOptions = unsafeJso(block)

/** Content parsing options, upstream `ParseOptions`. */
external interface ParseOptions {
    /** `true` collapses whitespace, `false` keeps it, `"full"` keeps it including newlines. */
    var preserveWhitespace: JsAny?
}

/** Builds [ParseOptions]. */
inline fun parseOptions(block: ParseOptions.() -> Unit): ParseOptions = unsafeJso(block)

/**
 * Attributes for a link command, upstream `LinkAttributes`.
 *
 * `href` is the only required attribute, which is why it is the one parameterised factory.
 */
external interface LinkAttributes {
    /** The target of the link. */
    var href: String?

    /** Where the link opens: `"_blank"`, `"_self"`, … */
    var target: String?

    /** The `rel` attribute, for example `"noopener noreferrer nofollow"`. */
    var rel: String?

    /** The `class` attribute. */
    var className: String?
}

/** Builds [LinkAttributes]. */
inline fun linkAttributes(block: LinkAttributes.() -> Unit): LinkAttributes = unsafeJso(block)

/** A link to [href], the common case. */
@Suppress("FunctionName")
fun LinkAttributes(href: String): LinkAttributes = unsafeJso { this.href = href }

/** Attributes for a heading command, upstream `Level`. */
external interface HeadingAttributes {
    /** The heading level, 1 to 6. The editor clamps out-of-range values. */
    var level: Double
}

/** Builds [HeadingAttributes]. */
@Suppress("FunctionName")
fun HeadingAttributes(level: Double): HeadingAttributes = unsafeJso { this.level = level }

/** Attributes accepted by `setCodeBlock`. */
external interface CodeBlockAttributes {
    /** The language of the block, kept as `data-language` and used by highlighters. */
    var language: String?
}

/** Builds [CodeBlockAttributes]. */
inline fun codeBlockAttributes(block: CodeBlockAttributes.() -> Unit): CodeBlockAttributes =
    unsafeJso(block)
