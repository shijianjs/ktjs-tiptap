package demo

import demo.page.editor.newEditorPage
import demo.page.overview.newOverviewPage
import io.github.shijianjs.ktjs.demo.nav.DemoMenu
import io.github.shijianjs.ktjs.demo.nav.Entry
import io.github.shijianjs.ktjs.demo.nav.Group

/** The Tiptap version this demo is built against; the navbar shows it under the site name. */
const val SITE_VERSION: String = "3.34.0"

/** The documentation site; its pages describe the very demo next to it. */
const val DOCS_URL: String = "https://shijianjs.github.io/ktjs-tiptap/"

/** Dokka reference assembled by `pagesSite` next to the demo. */
const val API_URL: String = "https://shijianjs.github.io/ktjs-tiptap/api/"

/** Source repository, linked from the navbar's link menu. */
const val SOURCE_URL: String = "https://github.com/shijianjs/ktjs-tiptap"

internal val START: Group = Group("start", "lucide:sparkles")

internal val EXAMPLES: Group = Group("examples", "lucide:play")

/**
 * The site: one entry per page. Adding a page means adding one entry here and its titles in
 * [siteTexts] — the sidebar, the breadcrumb, the router and the page cache all derive from this list.
 */
fun siteMenu(): DemoMenu = DemoMenu(
    listOf(
        Entry("overview", START, "lucide:sparkles") { newOverviewPage() },
        Entry("editor", EXAMPLES, "lucide:text-cursor-input") { newEditorPage() },
    ),
)
