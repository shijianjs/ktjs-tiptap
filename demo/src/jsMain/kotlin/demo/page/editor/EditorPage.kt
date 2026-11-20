package demo.page.editor

import io.github.shijianjs.ktjs.demo.page.DemoPage
import io.github.shijianjs.ktjs.demo.i18n.Binder
import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.i18n.Translations
import io.github.shijianjs.ktjs.demo.i18n.texts
import io.github.shijianjs.ktjs.demo.page.Blocks.exampleBox
import io.github.shijianjs.ktjs.demo.page.Grid
import io.github.shijianjs.ktjs.demo.page.SectionBlock
import io.github.shijianjs.ktjs.dom.element.inlineStyle
import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.dom.webcomponent.CustomElementReferenceAdapter
import io.github.shijianjs.ktjs.tiptap.Editor
import io.github.shijianjs.ktjs.tiptap.core.HeadingAttributes
import io.github.shijianjs.ktjs.tiptap.core.LinkAttributes
import io.github.shijianjs.ktjs.tiptap.core.TiptapEvents
import io.github.shijianjs.ktjs.tiptap.core.editorOptions
import io.github.shijianjs.ktjs.tiptap.core.starterKit
import io.github.shijianjs.ktjs.tiptap.createEditor
import io.github.shijianjs.ktjs.vaadin.button.ButtonElement
import io.github.shijianjs.ktjs.vaadin.button.createButton
import web.animations.requestAnimationFrame
import web.cssom.FontFamily
import web.cssom.FontSize
import web.cssom.Margin
import web.cssom.WhiteSpace
import web.cssom.px
import web.dom.clickEvent
import web.events.addHandler
import web.html.HTMLElement
import web.html.HtmlTagName

/** Every string this page shows, in one language. */
private class EditorTexts(
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

private val editorEnglish: EditorTexts = EditorTexts(
    title = "Editor",
    summary = "The real Tiptap core and StarterKit behind a typed command chain: the toolbar formats " +
        "the text, the document stays headless, and the HTML under it is the only rendering.",
    toolbarHeading = "Format",
    htmlHeading = "The document as HTML",
    statusLabel = "Words / characters",
    emptyHtml = "The document's HTML shows up here.",
    hint = "Everything on this page goes through the wrapper: `createEditor`, `editorOptions`, " +
        "`starterKit`, the command chain, `getHTML` / `getText`, and `TiptapEvents`.",
    initial = "<p>This paragraph is the initial content. Select a word and try the toolbar: the editor " +
        "is headless, so what you see is the browser rendering the document Tiptap keeps.</p>" +
        "<p>Type <code>**markdown**</code> shortcuts too — StarterKit installs the input rules.</p>",
    linkHref = "https://tiptap.dev",
    labels = listOf(
        "Bold", "Italic", "Underline", "Strike", "Code", "Heading 1", "Heading 2", "Paragraph",
        "Bullet list", "Ordered list", "Blockquote", "Code block", "Link", "Undo", "Redo", "Clear",
    ),
)

private val editorChinese: EditorTexts = EditorTexts(
    title = "编辑器",
    summary = "真实 Tiptap core 与 StarterKit，外面是一条有类型的命令链：工具栏格式化文本，文档保持无头，" +
        "下面那段 HTML 才是唯一产物。",
    toolbarHeading = "格式化",
    htmlHeading = "文档的 HTML",
    statusLabel = "词数 / 字符数",
    emptyHtml = "文档的 HTML 会显示在这里。",
    hint = "页面上的操作全走封装：`createEditor`、`editorOptions`、`starterKit`、命令链、" +
        "`getHTML` / `getText`，以及 `TiptapEvents`。",
    initial = "<p>这一段就是初始内容。选中一个词再点工具栏试试：编辑器是无头的，你看到的是浏览器在渲染 " +
        "Tiptap 持有的文档。</p><p>也可以直接敲 <code>**markdown**</code> 快捷键 —— StarterKit 已经装好了 " +
        "输入规则。</p>",
    linkHref = "https://tiptap.dev",
    labels = listOf(
        "加粗", "斜体", "下划线", "删除线", "行内代码", "一级标题", "二级标题", "正文",
        "无序列表", "有序列表", "引用", "代码块", "链接", "撤销", "重做", "清空",
    ),
)

private val editorTexts: Translations<EditorTexts> = texts(
    Langs.EN to editorEnglish,
    Langs.ZH_HANS to editorChinese,
)

/** One toolbar button: the command it runs, and the mark whose active state it reports. */
private class Action(
    val id: String,
    val mark: String?,
    val run: (Editor, EditorTexts) -> Unit,
)

private val ACTIONS: List<Action> = listOf(
    Action("bold", "bold") { editor, _ -> editor.chain().focus().toggleBold().run() },
    Action("italic", "italic") { editor, _ -> editor.chain().focus().toggleItalic().run() },
    Action("underline", "underline") { editor, _ -> editor.chain().focus().toggleUnderline().run() },
    Action("strike", "strike") { editor, _ -> editor.chain().focus().toggleStrike().run() },
    Action("code", "code") { editor, _ -> editor.chain().focus().toggleCode().run() },
    Action("h1", null) { editor, _ -> editor.chain().focus().setHeading(HeadingAttributes(1.0)).run() },
    Action("h2", null) { editor, _ -> editor.chain().focus().setHeading(HeadingAttributes(2.0)).run() },
    Action("paragraph", null) { editor, _ -> editor.chain().focus().setParagraph().run() },
    Action("bullet", null) { editor, _ -> editor.chain().focus().toggleBulletList().run() },
    Action("ordered", null) { editor, _ -> editor.chain().focus().toggleOrderedList().run() },
    Action("quote", null) { editor, _ -> editor.chain().focus().toggleBlockquote().run() },
    Action("codeblock", null) { editor, _ -> editor.chain().focus().toggleCodeBlock().run() },
    Action("link", null) { editor, texts -> editor.chain().focus().selectAll().setLink(LinkAttributes(texts.linkHref)).run() },
    Action("undo", null) { editor, _ -> editor.chain().focus().undo().run() },
    Action("redo", null) { editor, _ -> editor.chain().focus().redo().run() },
    Action("clear", null) { editor, _ -> editor.chain().focus().clearContent(true).run() },
)

/**
 * The editor page: a toolbar, the editable canvas, and the document read back as HTML.
 *
 * Tiptap is headless — it draws nothing — so the page supplies both the toolbar and the canvas. The
 * editor is created once; a language switch only relabels the buttons and re-reads the document, so
 * the text, the selection and the undo history survive it.
 */
internal class EditorPage : DemoPage() {

    private val texts = Binder(editorTexts)

    /** The strings the status line and the link command read; kept in step with the switch. */
    private var current: EditorTexts = editorEnglish

    private var editor: Editor? = null

    /** The editable area; the box around it is the card, so this only reserves room to type in. */
    private val canvas: HTMLElement = HtmlTagName.div {
        setAttribute("data-canvas", "")
        inlineStyle.minHeight = 200.px
    }

    /** The readout, marked for the end-to-end suite: word count, character count and HTML length. */
    private val status: HTMLElement = HtmlTagName.p { setAttribute("data-status", "") }

    /** The document as text: a `<pre>` is what it is, the card behind it supplies the surface. */
    private val html: HTMLElement = HtmlTagName.pre {
        setAttribute("data-html", "")
        inlineStyle.apply {
            margin = Margin(0.px, 0.px)
            fontFamily = FontFamily.monospace
            fontSize = 13.px
            whiteSpace = WhiteSpace.preWrap
        }
    }

    /** One button per action, each tagged so the end-to-end suite can address it by name. */
    private val buttons: List<ButtonElement> = ACTIONS.map { action ->
        createButton { setAttribute("data-action", action.id) }
    }


    init {
        val section = SectionBlock.NAME { show() }
        texts.bind(section.heading, { it.toolbarHeading })
        texts.bind(section.hint, { it.hint })

        section.body.append(
            Grid.NAME { show(110); add(*buttons.toTypedArray()) },
            status,
            exampleBox(null, canvas),
        )

        // The document read back is its own block: a heading, then the card holding the `<pre>`.
        val output = SectionBlock.NAME { show() }
        texts.bind(output.heading, { it.htmlHeading })
        output.body.append(exampleBox(null, html))
        body.append(section.root, output.root)

        buttons.forEachIndexed { index, button ->
            val action = ACTIONS[index]
            button.clickEvent.addHandler {
                val target = editor ?: return@addHandler
                action.run(target, current)
                refresh()
            }
        }

        createOnceMounted()
    }

    /** Creates the editor as soon as the canvas is in the document. */
    private fun createOnceMounted() {
        if (editor != null) return
        if (!canvas.isConnected) {
            requestAnimationFrame { _ -> createOnceMounted() }
            return
        }
        editor = createEditor(
            editorOptions {
                element = canvas
                content = current.initial
                extensions = arrayOf(starterKit())
                onUpdate = { refresh() }
            },
        ).also { it.on(TiptapEvents.Create) { refresh() } }
        refresh()
    }

    /** Re-reads the document: the readout, the HTML panel and each mark button's pressed state. */
    private fun refresh() {
        val target = editor ?: return
        val text = target.getText()
        val words = text.split(" ", "\n").filter { it.isNotBlank() }.size
        status.textContent = "${current.statusLabel}: $words / ${text.length}"
        status.setAttribute("data-words", words.toString())
        status.setAttribute("data-chars", "${text.length}")
        val document = target.getHTML()
        status.setAttribute("data-html-length", "${document.length}")
        html.textContent = document
        buttons.forEachIndexed { index, button ->
            val mark = ACTIONS[index].mark ?: return@forEachIndexed
            val active = target.isActiveNode(mark)
            button.setAttribute("data-active", active.toString())
            button.setAttribute("aria-pressed", active.toString())
        }
    }

    override fun applyTranslation(lang: Lang) {
        current = editorTexts[lang]
        setTitle(current.title)
        setSummary(current.summary)
        texts.apply(lang)
        buttons.forEachIndexed { index, button ->
            button.textContent = current.labels.getOrElse(index) { "" }
        }
        refresh()
    }

    companion object : CustomElementReferenceAdapter<EditorPage>(EditorPage::class.js, "tiptap-editor-page")
}
