package io.github.shijianjs.ktjs.tiptap.core

/**
 * The events an editor emits, as named constants.
 *
 * Pass one of these to `on` / `off` / `emit` instead of typing the string, so a typo is a compile
 * error. Names come from the editor's own `EditorEvents` map.
 */
object TiptapEvents {
    /** Before the editor is constructed. */
    inline val BeforeCreate: String get() = "beforeCreate"

    /** After the editor is constructed; the document exists and can be read. */
    inline val Create: String get() = "create"

    /** After the editor is mounted into its element. */
    inline val Mount: String get() = "mount"

    /** When the editor is unmounted. */
    inline val Unmount: String get() = "unmount"

    /** The content changed; the usual place to read `getHTML` / `getJSON`. */
    inline val Update: String get() = "update"

    /** The selection moved. */
    inline val SelectionUpdate: String get() = "selectionUpdate"

    /** A transaction was dispatched, dispatched or not. */
    inline val Transaction: String get() = "transaction"

    /** The editor gained focus. */
    inline val Focus: String get() = "focus"

    /** The editor lost focus. */
    inline val Blur: String get() = "blur"

    /** Before the editor is destroyed. */
    inline val Destroy: String get() = "destroy"

    /** The initial content could not be parsed; needs `enableContentCheck`. */
    inline val ContentError: String get() = "contentError"
}

/** A chain-free helper: the editor an event payload belongs to. */
external interface TiptapEventPayload {
    /** The editor that emitted the event. */
    val editor: io.github.shijianjs.ktjs.tiptap.Editor
}
