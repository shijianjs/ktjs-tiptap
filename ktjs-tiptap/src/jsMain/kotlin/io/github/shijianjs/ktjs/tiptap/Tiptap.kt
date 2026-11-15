/**
 * Kotlin/JS bindings for [Tiptap](https://github.com/ueberdosis/tiptap), the headless rich-text
 * editor.
 *
 * The entry point is [Editor]; the options, the command set and the StarterKit factory live in
 * [io.github.shijianjs.ktjs.tiptap.core].
 */
package io.github.shijianjs.ktjs.tiptap

import io.github.shijianjs.ktjs.tiptap.core.EditorOptions
import io.github.shijianjs.ktjs.tiptap.core.TiptapCanCommands
import io.github.shijianjs.ktjs.tiptap.core.TiptapChainedCommands
import io.github.shijianjs.ktjs.tiptap.core.TiptapSingleCommands
import io.github.shijianjs.ktjs.tiptap.core.editorOptions
import kotlin.js.JsAny
import kotlin.js.JsName
import kotlin.js.definedExternally

/**
 * The `@tiptap/core` module.
 *
 * The package is ESM with a CommonJS build, and `Editor` is a named export in both. Binding the
 * module as a namespace and reading `Editor` out of it keeps the import identical under either
 * resolution.
 */
@JsModule("@tiptap/core")
external object TiptapCoreModule {
    /** The `Editor` class. */
    @JsName("Editor")
    val EditorClass: JsAny
}

private val constructEditor: (JsAny, EditorOptions) -> Editor =
    js("(cls, options) => new cls(options)")

/** Creates an editor from [options], which is what `new Editor({ … })` does. */
fun createEditor(options: EditorOptions): Editor =
    constructEditor(TiptapCoreModule.EditorClass, options)

/**
 * An editor instance, upstream `Editor`.
 *
 * ```kotlin
 * val editor = createEditor(editorOptions {
 *     element = host
 *     extensions = arrayOf(starterKit())
 *     content = "<p>Hello</p>"
 * })
 * editor.chain().focus().toggleBold().run()
 * ```
 *
 * The class needs a real element with a size and a caret, so it is meant for the browser.
 */
external class Editor {
    /** The options the editor was created with; [setOptions] replaces them. */
    var options: EditorOptions

    /** The extension manager, which knows every node, mark and extension. */
    val extensionManager: JsAny

    /** The ProseMirror schema built from the extensions. */
    val schema: JsAny

    /** Storage of every extension, keyed by extension name. */
    val storage: JsAny

    /** A unique id for this editor instance. */
    val instanceId: String

    /** Whether the editor currently holds the selection. */
    val isFocused: Boolean

    /** Whether the editor has finished initialising. */
    val isInitialized: Boolean

    /** Whether the content can be edited right now. */
    val isEditable: Boolean

    /** Whether [destroy] has already run. */
    val isDestroyed: Boolean

    /** Whether the document is empty. */
    val isEmpty: Boolean

    /** The ProseMirror view behind the editor. */
    val view: JsAny

    /** The ProseMirror state behind the editor. */
    val state: JsAny

    // ---------------------------------------------------------------------------------------
    // Content
    // ---------------------------------------------------------------------------------------

    /** The document as HTML. */
    fun getHTML(): String

    /** The document as a ProseMirror JSON document. */
    fun getJSON(): JsAny

    /** The document as plain text, one block per line by default. */
    @JsName("getText")
    fun getText(): String

    /** [getText] with a block separator and custom serialisers. */
    @JsName("getText")
    fun getText(options: JsAny): String

    /** Replaces the whole document. */
    fun setContent(content: JsAny, emitUpdate: Boolean? = definedExternally)

    // ---------------------------------------------------------------------------------------
    // Selection and attributes
    // ---------------------------------------------------------------------------------------

    /** Whether the given node or mark is active at the selection. */
    @JsName("isActive")
    fun isActiveNode(name: String, attributes: JsAny? = definedExternally): Boolean

    /** Whether a node or mark with the given attributes is active at the selection. */
    @JsName("isActive")
    fun isActiveAttributes(attributes: JsAny): Boolean

    /** The attributes of the node or mark at the selection. */
    fun getAttributes(nameOrType: JsAny): JsAny

    // ---------------------------------------------------------------------------------------
    // Commands
    // ---------------------------------------------------------------------------------------

    /** The registered commands, called one at a time. */
    val commands: TiptapSingleCommands

    /** Starts a command chain; [TiptapChainedCommands.run] dispatches it. */
    fun chain(): TiptapChainedCommands

    /** Starts a chain that only reports whether the commands would succeed. */
    fun can(): TiptapCanCommands

    // ---------------------------------------------------------------------------------------
    // Lifetime
    // ---------------------------------------------------------------------------------------

    /** Updates the options of a live editor. */
    fun setOptions(options: EditorOptions)

    /** Turns editing on or off. */
    fun setEditable(editable: Boolean, emitUpdate: Boolean? = definedExternally)

    /** Mounts the editor into another element. */
    fun mount(element: web.dom.Element)

    /** Removes the editor from the document, keeping the instance alive. */
    fun unmount()

    /** Registers an extra ProseMirror plugin. */
    fun registerPlugin(plugin: JsAny, handlePlugins: ((JsAny, Array<JsAny>) -> Array<JsAny>)? = definedExternally): JsAny

    /** Unregisters a plugin by name, key, or a list of either. */
    fun unregisterPlugin(nameOrPluginKeyToRemove: JsAny): JsAny?

    /** Destroys the editor and releases its DOM. */
    fun destroy()

    // ---------------------------------------------------------------------------------------
    // Events
    // ---------------------------------------------------------------------------------------

    /** Subscribes to an editor event; the payload carries the `editor` that fired it. */
    fun on(event: String, callback: (JsAny) -> Unit)

    /** Removes a listener registered with [on]. */
    fun off(event: String, callback: (JsAny) -> Unit)

    /** Fires an editor event. */
    fun emit(event: String, payload: JsAny)
}

/** `new Editor({ … })`, spelled as a function. */
fun tiptapEditor(options: EditorOptions): Editor = createEditor(options)

/** An extension instance, upstream `Extension` — what [io.github.shijianjs.ktjs.tiptap.core.starterKit] returns. */
external interface TiptapExtension

/** An editor with the default options: no element, no content, no extensions. */
fun emptyTiptapEditor(): Editor = createEditor(editorOptions {})
