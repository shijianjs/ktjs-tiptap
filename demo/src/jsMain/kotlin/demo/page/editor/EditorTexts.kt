package demo.page.editor

import io.github.shijianjs.ktjs.dom.element.invoke

/** Every string this page shows, in one language. */
internal class EditorTexts(
    val title: String,
    val summary: String,
    val toolbarHeading: String,
    val htmlHeading: String,
    val statusLabel: String,
    val emptyHtml: String,
    val hint: String,
    val initial: String,
    val linkHref: String,
    /** The toolbar labels, in the order of [ACTIONS]. */
    val labels: List<String>,
)
