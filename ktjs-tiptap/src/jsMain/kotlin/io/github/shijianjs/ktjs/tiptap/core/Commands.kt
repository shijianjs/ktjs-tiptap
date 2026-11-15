package io.github.shijianjs.ktjs.tiptap.core

import kotlin.js.JsAny
import kotlin.js.definedExternally

/**
 * The commands the wrapper declares, upstream `ChainedCommands` narrowed to what StarterKit installs.
 *
 * Tiptap builds its command types from the installed extensions, so the full type is open-ended:
 * every extension adds its own commands. The wrapper declares the set that StarterKit always provides
 * (the marks, the nodes, the lists, the history commands and the content commands) and leaves the
 * per-extension ones out — see `docs/development/coverage.md`.
 *
 * This interface is the **chain** flavour: every method returns the chain so calls can be stacked,
 * and [TiptapChainedCommands.run] dispatches them.
 */
external interface TiptapChainedCommands {
    /** Dispatches every command in the chain; `true` when all of them applied. */
    fun run(): Boolean

    /** Moves the selection into the editor, and optionally to a position. */
    fun focus(position: JsAny? = definedExternally): TiptapChainedCommands

    /** Removes the selection from the editor. */
    fun blur(): TiptapChainedCommands

    /** Deletes the whole document. */
    fun clearContent(emitUpdate: Boolean? = definedExternally): TiptapChainedCommands

    /** Inserts content at the selection. */
    fun insertContent(content: JsAny): TiptapChainedCommands

    /** Inserts plain text at the selection or at a given position. */
    fun insertText(text: String, position: JsAny? = definedExternally): TiptapChainedCommands

    /** Replaces the document. */
    fun setContent(content: JsAny, emitUpdate: Boolean? = definedExternally): TiptapChainedCommands

    /** Deletes the selection. */
    fun deleteSelection(): TiptapChainedCommands

    /** Selects the whole document. */
    fun selectAll(): TiptapChainedCommands

    /** Sets custom metadata on the current transaction. */
    fun setMeta(key: String, value: JsAny): TiptapChainedCommands

    /** Steps back in the history. */
    fun undo(): TiptapChainedCommands

    /** Steps forward in the history. */
    fun redo(): TiptapChainedCommands

    /** Toggles bold. */
    fun toggleBold(): TiptapChainedCommands

    /** Applies bold. */
    fun setBold(): TiptapChainedCommands

    /** Removes bold. */
    fun unsetBold(): TiptapChainedCommands

    /** Toggles italic. */
    fun toggleItalic(): TiptapChainedCommands

    /** Applies italic. */
    fun setItalic(): TiptapChainedCommands

    /** Removes italic. */
    fun unsetItalic(): TiptapChainedCommands

    /** Toggles strikethrough. */
    fun toggleStrike(): TiptapChainedCommands

    /** Applies strikethrough. */
    fun setStrike(): TiptapChainedCommands

    /** Removes strikethrough. */
    fun unsetStrike(): TiptapChainedCommands

    /** Toggles inline code. */
    fun toggleCode(): TiptapChainedCommands

    /** Applies inline code. */
    fun setCode(): TiptapChainedCommands

    /** Removes inline code. */
    fun unsetCode(): TiptapChainedCommands

    /** Toggles underline. */
    fun toggleUnderline(): TiptapChainedCommands

    /** Applies underline. */
    fun setUnderline(): TiptapChainedCommands

    /** Removes underline. */
    fun unsetUnderline(): TiptapChainedCommands

    /** Toggles the heading level at the selection. */
    fun toggleHeading(attributes: HeadingAttributes): TiptapChainedCommands

    /** Turns the block into a heading. */
    fun setHeading(attributes: HeadingAttributes): TiptapChainedCommands

    /** Turns the heading back into a paragraph. */
    fun unsetHeading(): TiptapChainedCommands

    /** Toggles a blockquote around the selection. */
    fun toggleBlockquote(): TiptapChainedCommands

    /** Wraps the selection in a blockquote. */
    fun setBlockquote(): TiptapChainedCommands

    /** Unwraps the blockquote. */
    fun unsetBlockquote(): TiptapChainedCommands

    /** Toggles a bullet list around the selection. */
    fun toggleBulletList(): TiptapChainedCommands

    /** Toggles an ordered list around the selection. */
    fun toggleOrderedList(): TiptapChainedCommands

    /** Toggles a code block around the selection. */
    fun toggleCodeBlock(attributes: CodeBlockAttributes? = definedExternally): TiptapChainedCommands

    /** Wraps the selection in a code block. */
    fun setCodeBlock(attributes: CodeBlockAttributes? = definedExternally): TiptapChainedCommands

    /** Turns the code block back into a paragraph. */
    fun unsetCodeBlock(): TiptapChainedCommands

    /** Inserts a hard break. */
    fun setHardBreak(): TiptapChainedCommands

    /** Inserts a horizontal rule. */
    fun setHorizontalRule(): TiptapChainedCommands

    /** Turns the selection into a plain paragraph. */
    fun setParagraph(): TiptapChainedCommands

    /** Toggles a link on the selection. */
    fun toggleLink(attributes: LinkAttributes): TiptapChainedCommands

    /** Applies a link to the selection. */
    fun setLink(attributes: LinkAttributes): TiptapChainedCommands

    /** Removes the link from the selection. */
    fun unsetLink(): TiptapChainedCommands
}



/**
 * The same commands called one at a time, upstream `SingleCommands`.
 *
 * Every method reports whether it applied. Tiptap declares a few of them as `void`; the wrapper keeps
 * `Boolean` throughout so a failed command cannot go unnoticed — the value is `undefined` for those,
 * which Kotlin/JS reads as `false`.
 */
external interface TiptapSingleCommands {
    /** Moves the selection into the editor, and optionally to a position. */
    fun focus(position: JsAny? = definedExternally): Boolean

    /** Removes the selection from the editor. */
    fun blur(): Boolean

    /** Deletes the whole document. */
    fun clearContent(emitUpdate: Boolean? = definedExternally): Boolean

    /** Inserts content at the selection. */
    fun insertContent(content: JsAny): Boolean

    /** Inserts plain text at the selection or at a given position. */
    fun insertText(text: String, position: JsAny? = definedExternally): Boolean

    /** Replaces the document. */
    fun setContent(content: JsAny, emitUpdate: Boolean? = definedExternally): Boolean

    /** Deletes the selection. */
    fun deleteSelection(): Boolean

    /** Selects the whole document. */
    fun selectAll(): Boolean

    /** Sets custom metadata on the current transaction. */
    fun setMeta(key: String, value: JsAny): Boolean

    /** Steps back in the history. */
    fun undo(): Boolean

    /** Steps forward in the history. */
    fun redo(): Boolean

    /** Toggles bold. */
    fun toggleBold(): Boolean

    /** Applies bold. */
    fun setBold(): Boolean

    /** Removes bold. */
    fun unsetBold(): Boolean

    /** Toggles italic. */
    fun toggleItalic(): Boolean

    /** Applies italic. */
    fun setItalic(): Boolean

    /** Removes italic. */
    fun unsetItalic(): Boolean

    /** Toggles strikethrough. */
    fun toggleStrike(): Boolean

    /** Applies strikethrough. */
    fun setStrike(): Boolean

    /** Removes strikethrough. */
    fun unsetStrike(): Boolean

    /** Toggles inline code. */
    fun toggleCode(): Boolean

    /** Applies inline code. */
    fun setCode(): Boolean

    /** Removes inline code. */
    fun unsetCode(): Boolean

    /** Toggles underline. */
    fun toggleUnderline(): Boolean

    /** Applies underline. */
    fun setUnderline(): Boolean

    /** Removes underline. */
    fun unsetUnderline(): Boolean

    /** Toggles the heading level at the selection. */
    fun toggleHeading(attributes: HeadingAttributes): Boolean

    /** Turns the block into a heading. */
    fun setHeading(attributes: HeadingAttributes): Boolean

    /** Turns the heading back into a paragraph. */
    fun unsetHeading(): Boolean

    /** Toggles a blockquote around the selection. */
    fun toggleBlockquote(): Boolean

    /** Wraps the selection in a blockquote. */
    fun setBlockquote(): Boolean

    /** Unwraps the blockquote. */
    fun unsetBlockquote(): Boolean

    /** Toggles a bullet list around the selection. */
    fun toggleBulletList(): Boolean

    /** Toggles an ordered list around the selection. */
    fun toggleOrderedList(): Boolean

    /** Toggles a code block around the selection. */
    fun toggleCodeBlock(attributes: CodeBlockAttributes? = definedExternally): Boolean

    /** Wraps the selection in a code block. */
    fun setCodeBlock(attributes: CodeBlockAttributes? = definedExternally): Boolean

    /** Turns the code block back into a paragraph. */
    fun unsetCodeBlock(): Boolean

    /** Inserts a hard break. */
    fun setHardBreak(): Boolean

    /** Inserts a horizontal rule. */
    fun setHorizontalRule(): Boolean

    /** Turns the selection into a plain paragraph. */
    fun setParagraph(): Boolean

    /** Toggles a link on the selection. */
    fun toggleLink(attributes: LinkAttributes): Boolean

    /** Applies a link to the selection. */
    fun setLink(attributes: LinkAttributes): Boolean

    /** Removes the link from the selection. */
    fun unsetLink(): Boolean
}

/**
 * The reporting flavour of the commands, upstream `CanCommands`: each call answers whether the command
 * *would* apply, without dispatching anything.
 */
external interface TiptapCanCommands {
    /** Starts a non-dispatching chain, which is how several commands are probed together. */
    fun chain(): TiptapChainedCommands

    /** Whether the selection could move into the editor. */
    fun focus(position: JsAny? = definedExternally): Boolean

    /** Whether the focus could be removed. */
    fun blur(): Boolean

    /** Whether the document could be emptied. */
    fun clearContent(emitUpdate: Boolean? = definedExternally): Boolean

    /** Whether content could be inserted at the selection. */
    fun insertContent(content: JsAny): Boolean

    /** Whether text could be inserted. */
    fun insertText(text: String, position: JsAny? = definedExternally): Boolean

    /** Whether the document could be replaced. */
    fun setContent(content: JsAny, emitUpdate: Boolean? = definedExternally): Boolean

    /** Whether something is selected to delete. */
    fun deleteSelection(): Boolean

    /** Whether the whole document could be selected. */
    fun selectAll(): Boolean

    /** Whether metadata could be set on a transaction. */
    fun setMeta(key: String, value: JsAny): Boolean

    /** Whether there is history to undo. */
    fun undo(): Boolean

    /** Whether there is history to redo. */
    fun redo(): Boolean

    /** Whether bold could be toggled. */
    fun toggleBold(): Boolean

    /** Whether bold could be applied. */
    fun setBold(): Boolean

    /** Whether bold could be removed. */
    fun unsetBold(): Boolean

    /** Whether italic could be toggled. */
    fun toggleItalic(): Boolean

    /** Whether italic could be applied. */
    fun setItalic(): Boolean

    /** Whether italic could be removed. */
    fun unsetItalic(): Boolean

    /** Whether strikethrough could be toggled. */
    fun toggleStrike(): Boolean

    /** Whether strikethrough could be applied. */
    fun setStrike(): Boolean

    /** Whether strikethrough could be removed. */
    fun unsetStrike(): Boolean

    /** Whether inline code could be toggled. */
    fun toggleCode(): Boolean

    /** Whether inline code could be applied. */
    fun setCode(): Boolean

    /** Whether inline code could be removed. */
    fun unsetCode(): Boolean

    /** Whether underline could be toggled. */
    fun toggleUnderline(): Boolean

    /** Whether underline could be applied. */
    fun setUnderline(): Boolean

    /** Whether underline could be removed. */
    fun unsetUnderline(): Boolean

    /** Whether the heading level could be toggled. */
    fun toggleHeading(attributes: HeadingAttributes): Boolean

    /** Whether the block could become a heading. */
    fun setHeading(attributes: HeadingAttributes): Boolean

    /** Whether the heading could become a paragraph. */
    fun unsetHeading(): Boolean

    /** Whether a blockquote could be toggled. */
    fun toggleBlockquote(): Boolean

    /** Whether the selection could be wrapped in a blockquote. */
    fun setBlockquote(): Boolean

    /** Whether the blockquote could be unwrapped. */
    fun unsetBlockquote(): Boolean

    /** Whether a bullet list could be toggled. */
    fun toggleBulletList(): Boolean

    /** Whether an ordered list could be toggled. */
    fun toggleOrderedList(): Boolean

    /** Whether a code block could be toggled. */
    fun toggleCodeBlock(attributes: CodeBlockAttributes? = definedExternally): Boolean

    /** Whether the selection could become a code block. */
    fun setCodeBlock(attributes: CodeBlockAttributes? = definedExternally): Boolean

    /** Whether the code block could become a paragraph. */
    fun unsetCodeBlock(): Boolean

    /** Whether a hard break could be inserted. */
    fun setHardBreak(): Boolean

    /** Whether a horizontal rule could be inserted. */
    fun setHorizontalRule(): Boolean

    /** Whether the selection could become a plain paragraph. */
    fun setParagraph(): Boolean

    /** Whether a link could be toggled. */
    fun toggleLink(attributes: LinkAttributes): Boolean

    /** Whether a link could be applied. */
    fun setLink(attributes: LinkAttributes): Boolean

    /** Whether the link could be removed. */
    fun unsetLink(): Boolean
}
