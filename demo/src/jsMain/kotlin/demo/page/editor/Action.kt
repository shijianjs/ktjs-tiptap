package demo.page.editor

import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.tiptap.Editor

/** One toolbar button: the command it runs, and the mark whose active state it reports. */
internal class Action(
    val id: String,
    val mark: String?,
    val run: (Editor, EditorTexts) -> Unit,
)
