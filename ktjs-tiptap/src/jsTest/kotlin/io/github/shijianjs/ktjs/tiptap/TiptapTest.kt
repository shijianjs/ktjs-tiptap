package io.github.shijianjs.ktjs.tiptap

import io.github.shijianjs.ktjs.tiptap.core.CodeBlockAttributes
import io.github.shijianjs.ktjs.tiptap.core.HeadingAttributes
import io.github.shijianjs.ktjs.tiptap.core.LinkAttributes
import io.github.shijianjs.ktjs.tiptap.core.editorOptions
import io.github.shijianjs.ktjs.tiptap.core.headingOptions
import io.github.shijianjs.ktjs.tiptap.core.starterKit
import io.github.shijianjs.ktjs.tiptap.core.starterKitOptions
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import web.dom.document
import web.html.HTMLElement
import web.html.HtmlTagName

/**
 * Runs the wrapper against the real `@tiptap/core` and StarterKit packages in Chromium.
 *
 * The editor needs a real element and a selection, so every test mounts into a host appended to the
 * document. Initialisation finishes on a timer (`isInitialized` and the `create` event both flip
 * there), hence the promise-returning tests: the Karma runner awaits them.
 */
class TiptapTest {
    private fun host(tag: String): HTMLElement {
        val element = document.createElement(HtmlTagName.div).unsafeCast<HTMLElement>()
        element.setAttribute("data-role", tag)
        document.body?.appendChild(element)
        return element
    }

    private fun editorOn(tag: String, html: String = "<p>Hello world</p>"): Editor =
        createEditor(
            editorOptions {
                element = host(tag)
                content = html
                extensions = arrayOf(starterKit())
            },
        )

    @Test
    fun theEditorMountsItsOwnEditableElement(): Promise<Unit> {
        var created = 0
        val host = host("mount")
        val editor = createEditor(
            editorOptions {
                element = host
                content = "<p>Hello world</p>"
                extensions = arrayOf(starterKit())
                onCreate = { created++ }
            },
        )

        assertEquals("<p>Hello world</p>", editor.getHTML(), "the initial content is parsed")
        assertTrue(host.querySelector(".tiptap") != null, "the editable element is inside the host")

        return nextTick().then<Unit> {
            assertTrue(editor.isInitialized, "initialisation finishes on the next tick")
            assertEquals(1, created, "the create callback fired once")
            editor.destroy()
            Unit
        }
    }

    @Test
    fun contentRoundTripsThroughHtmlAndText() {
        val editor = editorOn("content", "<p>Hello <strong>world</strong></p>")

        assertEquals("<p>Hello <strong>world</strong></p>", editor.getHTML(), "HTML comes back unchanged")
        assertEquals("Hello world", editor.getText(), "text drops the markup")
        assertFalse(editor.isEmpty, "the document has content")

        editor.chain().setContent("<p>Second draft</p>").run()
        assertEquals("<p>Second draft</p>", editor.getHTML(), "setContent replaces the document")
        editor.destroy()
    }

    @Test
    fun theChainAppliesMarks() {
        val editor = editorOn("marks")
        editor.chain().selectAll().toggleBold().run()

        assertTrue(editor.getHTML().contains("<strong>"), "bold reached the document: ${editor.getHTML()}")
        assertTrue(editor.isActiveNode("bold"), "bold is active at the selection")

        editor.chain().selectAll().toggleBold().run()
        assertFalse(editor.getHTML().contains("<strong>"), "toggling again removes the mark")
        editor.destroy()
    }

    @Test
    fun aHeadingReplacesTheParagraph() {
        val editor = editorOn("heading")
        editor.chain().selectAll().setHeading(HeadingAttributes(2.0)).run()

        assertTrue(editor.getHTML().contains("<h2>"), "the block became a heading: ${editor.getHTML()}")
        assertTrue(editor.can().setHeading(HeadingAttributes(2.0)), "the level is allowed by the schema")

        editor.chain().selectAll().setParagraph().run()
        assertTrue(editor.getHTML().contains("<p>"), "the heading is back to a paragraph")
        editor.destroy()
    }

    @Test
    fun listsAndQuotesNest() {
        val editor = editorOn("lists")
        editor.chain().selectAll().toggleBulletList().run()
        assertTrue(editor.getHTML().contains("<ul>"), "a bullet list was created: ${editor.getHTML()}")

        editor.chain().selectAll().toggleBlockquote().run()
        assertTrue(editor.getHTML().contains("<blockquote>"), "the block is quoted")
        editor.destroy()
    }

    @Test
    fun linksCarryTheirAttributes() {
        val editor = editorOn("links")
        editor.chain().selectAll().setLink(LinkAttributes("https://tiptap.dev")).run()

        assertTrue(
            editor.getHTML().contains("href=\"https://tiptap.dev\""),
            "the link is in the document: ${editor.getHTML()}",
        )
        assertTrue(editor.isActiveNode("link"), "the link mark is active")

        editor.chain().selectAll().unsetLink().run()
        assertFalse(editor.getHTML().contains("<a "), "the link was removed")
        editor.destroy()
    }

    @Test
    fun canAnswersWithoutChangingTheDocument() {
        val editor = editorOn("can")
        val before = editor.getHTML()

        assertTrue(editor.can().toggleBold(), "bold can be applied")
        assertEquals(before, editor.getHTML(), "asking does not change the document")

        assertTrue(editor.can().setHeading(HeadingAttributes(3.0)), "a level-3 heading is allowed")
        assertTrue(editor.can().chain().selectAll().toggleItalic().run(), "can() chains like chain()")
        assertEquals(before, editor.getHTML(), "the chained probe dispatched nothing")
        editor.destroy()
    }

    @Test
    fun eventsReportTheChanges() {
        var updates = 0
        var transactions = 0
        val editor = createEditor(
            editorOptions {
                element = host("events")
                content = "<p>Hello</p>"
                extensions = arrayOf(starterKit())
                onUpdate = { updates++ }
                onTransaction = { transactions++ }
            },
        )

        editor.chain().insertContent("<p>More</p>").run()

        assertTrue(updates > 0, "update fired for the change ($updates)")
        assertTrue(transactions > 0, "transaction fired for the dispatched chain ($transactions)")

        var destroyed = 0
        editor.on("destroy") { destroyed++ }
        editor.destroy()
        assertEquals(1, destroyed, "the destroy listener saw the teardown")
    }

    @Test
    fun starterKitPartsCanBeLeftOut() {
        val editor = createEditor(
            editorOptions {
                element = host("configured")
                content = "<p>Hello</p>"
                extensions = arrayOf(
                    starterKit(
                        starterKitOptions {
                            bold = false
                            heading = headingOptions { levels = arrayOf(1.0, 2.0) }
                        },
                    ),
                )
            },
        )

        assertEquals("<p>Hello</p>", editor.getHTML(), "the editor still renders without the omitted parts")
        assertTrue(hasCommand(editor, "toggleBold").not(), "bold was left out, so it has no command")
        assertTrue(hasCommand(editor, "toggleItalic"), "italic is still installed")
        assertTrue(editor.can().setHeading(HeadingAttributes(2.0)), "level 2 is allowed")
        assertTrue(editor.chain().selectAll().setHeading(HeadingAttributes(4.0)).run().not(), "level 4 was configured away")

        editor.on("update") { }
        editor.destroy()
    }

    @Test
    fun codeBlocksCarryTheirLanguage() {
        val editor = editorOn("code")
        editor.chain().selectAll().toggleCodeBlock(codeBlockAttributes("kotlin")).run()

        val html = editor.getHTML()
        assertTrue(html.contains("<pre"), "a code block was created: $html")
        assertTrue(html.contains("language-kotlin"), "the language is kept on the code element: $html")
        editor.destroy()
    }
}

/** Runs the callback on the next macrotask, which is where Tiptap finishes initialising. */
private fun nextTick(): Promise<Unit> = Promise { resolve, _ ->
    scheduleTick { resolve(Unit) }
}

private val scheduleTick: ((() -> Unit) -> Unit) = js("(cb) => setTimeout(cb, 0)")

/** Whether the editor exposes a given command at runtime. */
private fun hasCommand(editor: Editor, name: String): Boolean =
    commandTypeOf(editor, name) == "function"

private val commandTypeOf: (Editor, String) -> String = js("(editor, name) => typeof editor.commands[name]")

private fun codeBlockAttributes(language: String) =
    io.github.shijianjs.ktjs.tiptap.core.codeBlockAttributes { this.language = language }
