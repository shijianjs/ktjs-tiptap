package demo.e2e

import com.microsoft.playwright.Page
import com.microsoft.playwright.junit.UsePlaywright
import com.microsoft.playwright.options.WaitUntilState
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junitpioneer.jupiter.RetryingTest

/**
 * Smoke checks for the demo page: the bundle loads, the editor mounts the sample document, its toolbar
 * goes through the command chain, and the language switch swaps every visible string.
 */
@UsePlaywright(DemoOptions::class)
class DemoPageTest {
    @RetryingTest(maxAttempts = 2)
    fun `page renders heading, logo and styles`(page: Page) {
        openPage(page)

        assertTrue(page.title().contains("ktjs-tiptap"), "title: ${page.title()}")
        assertEquals("ktjs-tiptap demo", page.locator("h1").textContent())
        assertTrue(page.locator("img.logo").isVisible(), "the demo serves the project logo")

        val height = (
            page.locator(".canvas").evaluate("(node) => node.getBoundingClientRect().height") as Number
            ).toDouble()
        assertTrue(height > 200, "the stylesheet gives the editor a height ($height)")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the editor mounts the sample document`(page: Page) {
        openPage(page)

        assertTrue(page.locator(".canvas .tiptap").count() == 1, "Tiptap mounts its editable element")
        val html = page.locator("[data-role=html]").textContent()
        assertTrue(html.contains("<p>"), "the sample document is rendered: ${html.take(80)}")
        assertTrue(html.contains("markdown"), "the sample keeps its second paragraph")

        val words = page.locator(".view").getAttribute("data-words")?.toIntOrNull() ?: 0
        assertTrue(words > 20, "the status line counts the words ($words)")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the toolbar bolds the selected text`(page: Page) {
        openPage(page)

        selectFirstParagraph(page)
        page.locator("[data-action=bold]").click()

        val html = page.locator("[data-role=html]").textContent()
        assertTrue(html.contains("<strong>"), "bold reached the document: ${html.take(120)}")
        assertEquals("true", page.locator("[data-action=bold]").getAttribute("data-active"), "the toolbar reflects it")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the list button wraps the block and undo restores it`(page: Page) {
        openPage(page)

        selectFirstParagraph(page)
        page.locator("[data-action=bullet]").click()

        val listed = page.locator("[data-role=html]").textContent()
        assertTrue(listed.contains("<ul>"), "a bullet list was created: ${listed.take(120)}")

        page.locator("[data-action=undo]").click()
        val undone = page.locator("[data-role=html]").textContent()
        assertTrue(!undone.contains("<ul>"), "undo removed the list again: ${undone.take(120)}")
    }

    @RetryingTest(maxAttempts = 2)
    fun `heading and paragraph buttons switch the block type`(page: Page) {
        openPage(page)

        selectFirstParagraph(page)
        page.locator("[data-action=h2]").click()
        assertTrue(
            page.locator("[data-role=html]").textContent().contains("<h2>"),
            "the block became a level-2 heading",
        )

        page.locator("[data-action=paragraph]").click()
        assertTrue(
            page.locator("[data-role=html]").textContent().contains("<p>"),
            "the block is a paragraph again",
        )
    }

    @RetryingTest(maxAttempts = 2)
    fun `the clear button empties the document`(page: Page) {
        openPage(page)

        page.locator("[data-action=clear]").click()

        assertEquals("0", page.locator(".view").getAttribute("data-chars"), "nothing is left to count")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the language switch swaps every visible string`(page: Page) {
        openPage(page)

        page.locator("button.lang").click()

        assertEquals("ktjs-tiptap 演示", page.locator("h1").textContent())
        assertEquals("zh-Hans", page.locator("html").getAttribute("lang"))
        assertEquals("加粗", page.locator(".toolbar button").first().textContent())

        page.locator("button.lang").click()

        assertEquals("ktjs-tiptap demo", page.locator("h1").textContent())
        assertEquals("en", page.locator("html").getAttribute("lang"))
    }

    @RetryingTest(maxAttempts = 2)
    fun `page logs no console errors or failed requests`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        val failedRequests = mutableListOf<String>()
        page.onConsoleMessage { message -> if (message.type() == "error") consoleErrors += message.text() }
        page.onRequestFailed { request -> failedRequests += "${request.url()} ${request.failure()}" }

        openPage(page)
        selectFirstParagraph(page)
        page.locator("[data-action=bold]").click()

        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
        assertTrue(failedRequests.isEmpty(), "failed requests: $failedRequests")
    }

    private fun openPage(page: Page) {
        page.navigate("/", Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE))
    }

    /**
     * Focuses the editor and selects everything through ProseMirror's own `selectAll` binding.
     *
     * Setting a DOM range from the outside does not reach the editor state, and the toolbar buttons
     * rely on that state when they `focus()`.
     */
    private fun selectFirstParagraph(page: Page) {
        page.locator(".canvas .tiptap p").first().click()
        page.keyboard().press("Control+a")
    }
}
