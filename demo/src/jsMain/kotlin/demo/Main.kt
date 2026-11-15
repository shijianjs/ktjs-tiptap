package demo

import io.github.shijianjs.ktjs.tiptap.Editor
import io.github.shijianjs.ktjs.tiptap.core.HeadingAttributes
import io.github.shijianjs.ktjs.tiptap.core.LinkAttributes
import io.github.shijianjs.ktjs.tiptap.core.TiptapEvents
import io.github.shijianjs.ktjs.tiptap.core.editorOptions
import io.github.shijianjs.ktjs.tiptap.core.starterKit
import io.github.shijianjs.ktjs.tiptap.createEditor
import web.cssom.ClassName
import web.dom.document
import web.events.EventHandler
import web.html.HTMLElement
import web.html.HtmlTagName

private const val PROJECT_NAME = "ktjs-tiptap"

private val texts = Texts(
    mapOf(
        "title" to ("$PROJECT_NAME demo" to "$PROJECT_NAME 演示"),
        "intro" to (
            "A rich-text editor built with the real Tiptap core and StarterKit: format the text with " +
                "the toolbar, and the document stays headless — the HTML below is the only rendering." to
                "用真实 Tiptap core 与 StarterKit 搭的富文本编辑器：用工具栏格式化文本，文档本身不渲染界面，" +
                "下面那段 HTML 才是唯一产物。"
        ),
        "bold" to ("Bold" to "加粗"),
        "italic" to ("Italic" to "斜体"),
        "underline" to ("Underline" to "下划线"),
        "strike" to ("Strike" to "删除线"),
        "code" to ("Code" to "行内代码"),
        "h1" to ("Heading 1" to "一级标题"),
        "h2" to ("Heading 2" to "二级标题"),
        "paragraph" to ("Paragraph" to "正文"),
        "bullet" to ("Bullet list" to "无序列表"),
        "ordered" to ("Ordered list" to "有序列表"),
        "quote" to ("Blockquote" to "引用"),
        "codeblock" to ("Code block" to "代码块"),
        "link" to ("Link" to "链接"),
        "undo" to ("Undo" to "撤销"),
        "redo" to ("Redo" to "重做"),
        "clear" to ("Clear" to "清空"),
        "status" to ("Words / characters" to "词数 / 字符数"),
        "empty" to (
            "The document's HTML shows up here." to
                "文档的 HTML 会显示在这里。"
        ),
        "hint" to (
            "Everything on this page goes through the wrapper: `createEditor`, `editorOptions`, " +
                "`starterKit`, the command chain, `getHTML` / `getText`, and `TiptapEvents`." to
                "页面上的操作全走 wrapper：`createEditor`、`editorOptions`、`starterKit`、命令链、" +
                "`getHTML` / `getText`，以及 `TiptapEvents`。"
        ),
        "initial" to (
            "<p>This paragraph is the initial content. Select a word and try the toolbar: the editor " +
                "is headless, so what you see is the browser rendering the document Tiptap keeps.</p>" +
                "<p>Type <code>**markdown**</code> shortcuts too — StarterKit installs the input rules.</p>" to
                "<p>这一段就是初始内容。选中一个词再点工具栏试试：编辑器是无头的，你看到的是浏览器在渲染 " +
                "Tiptap 持有的文档。</p><p>也可以直接敲 <code>**markdown**</code> 快捷键 —— StarterKit 已经装好了 " +
                "输入规则。</p>"
        ),
        "linkHref" to ("https://tiptap.dev" to "https://tiptap.dev"),
    ),
)

fun main() {
    document.body?.apply { className = ClassName("page") }
    val page = document.body ?: return

    val header = document.createElement(HtmlTagName.div).apply { className = ClassName("header") }
    header.appendChild(
        document.createElement(HtmlTagName.img).apply {
            className = ClassName("logo")
            setAttribute("src", "img/$PROJECT_NAME-logo.png")
            setAttribute("alt", "$PROJECT_NAME logo")
        },
    )
    val heading = document.createElement(HtmlTagName.h1)
    header.appendChild(heading)
    header.appendChild(languageSwitch(texts))
    page.appendChild(header)

    val intro = document.createElement(HtmlTagName.p).apply { className = ClassName("hint") }
    page.appendChild(intro)

    val toolbar = document.createElement(HtmlTagName.div).apply {
        className = ClassName("row toolbar")
        setAttribute("role", "toolbar")
    }
    page.appendChild(toolbar)

    val view = document.createElement(HtmlTagName.div).apply { className = ClassName("view") }
    page.appendChild(view)

    val host = document.createElement(HtmlTagName.div).apply {
        className = ClassName("canvas editor")
        setAttribute("id", "editor")
    }
    page.appendChild(host)

    val output = document.createElement(HtmlTagName.pre).apply {
        className = ClassName("html")
        setAttribute("data-role", "html")
    }
    page.appendChild(output)

    val hint = document.createElement(HtmlTagName.p).apply { className = ClassName("hint small") }
    page.appendChild(hint)

    // `onUpdate` is wired before the editor exists, so the callback reaches the instance through this
    // holder instead of capturing a variable that is still being initialised.
    var editorRef: Editor? = null
    val markButtons = mutableListOf<Pair<String, HTMLElement>>()

    fun refresh() {
        val editor = editorRef ?: return
        val text = editor.getText()
        val words = text.split(" ", "\n").filter { it.isNotBlank() }.size
        view.textContent = "${texts["status"]}: $words / ${text.length}"
        view.setAttribute("data-words", words.toString())
        view.setAttribute("data-chars", text.length.toString())
        view.setAttribute("data-html", editor.getHTML().length.toString())
        output.textContent = editor.getHTML()
        markButtons.forEach { (mark, button) ->
            val active = editor.isActiveNode(mark).toString()
            button.setAttribute("data-active", active)
            button.setAttribute("aria-pressed", active)
        }
    }

    val editor: Editor = createEditor(
        editorOptions {
            element = host
            content = texts["initial"]
            extensions = arrayOf(starterKit())
            onUpdate = { refresh() }
        },
    )

    editorRef = editor

    fun action(label: String, mark: String? = null, handler: (Editor) -> Unit): HTMLElement =
        document.createElement(HtmlTagName.button).apply {
            className = ClassName("action")
            setAttribute("type", "button")
            setAttribute("data-action", label)
            textContent = texts[label]
            texts.onChange { textContent = texts[label] }
            onclick = EventHandler {
                handler(editor)
                refresh()
            }
            if (mark != null) markButtons += mark to this
            toolbar.appendChild(this)
        }

    action("bold", "bold") { it.chain().focus().toggleBold().run() }
    action("italic", "italic") { it.chain().focus().toggleItalic().run() }
    action("underline", "underline") { it.chain().focus().toggleUnderline().run() }
    action("strike", "strike") { it.chain().focus().toggleStrike().run() }
    action("code", "code") { it.chain().focus().toggleCode().run() }
    action("h1") { it.chain().focus().setHeading(HeadingAttributes(1.0)).run() }
    action("h2") { it.chain().focus().setHeading(HeadingAttributes(2.0)).run() }
    action("paragraph") { it.chain().focus().setParagraph().run() }
    action("bullet") { it.chain().focus().toggleBulletList().run() }
    action("ordered") { it.chain().focus().toggleOrderedList().run() }
    action("quote") { it.chain().focus().toggleBlockquote().run() }
    action("codeblock") { it.chain().focus().toggleCodeBlock().run() }
    action("link") { it.chain().focus().selectAll().setLink(LinkAttributes(texts["linkHref"])).run() }
    action("undo") { it.chain().focus().undo().run() }
    action("redo") { it.chain().focus().redo().run() }
    action("clear") { it.chain().focus().clearContent(true).run() }

    texts.onChange {
        heading.textContent = texts["title"]
        intro.textContent = texts["intro"]
        hint.textContent = texts["hint"]
        refresh()
    }

    heading.textContent = texts["title"]
    intro.textContent = texts["intro"]
    hint.textContent = texts["hint"]
    editor.on(TiptapEvents.Create) { refresh() }
    refresh()
}
