package demo.page.overview

import io.github.shijianjs.ktjs.demo.page.DemoPage
import demo.API_URL
import demo.DOCS_URL
import demo.SOURCE_URL
import io.github.shijianjs.ktjs.demo.i18n.Binder
import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.i18n.Translations
import io.github.shijianjs.ktjs.demo.i18n.texts
import io.github.shijianjs.ktjs.demo.page.Blocks.anchor
import io.github.shijianjs.ktjs.demo.page.Blocks.codeText
import io.github.shijianjs.ktjs.demo.page.Blocks.paragraph
import io.github.shijianjs.ktjs.demo.page.Blocks.row
import io.github.shijianjs.ktjs.demo.page.SectionBlock
import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.dom.element.unaryPlus
import io.github.shijianjs.ktjs.dom.webcomponent.CustomElementReferenceAdapter
import web.html.HtmlTagName

/** Every string this page shows, in one language. */
private class OverviewTexts(
    val title: String,
    val summary: String,
    val docsLink: String,
    val sourceLink: String,
    val apiLink: String,
    val purposeHeading: String,
    val body: String,
    val bullets: List<String>,
    val headlessHeading: String,
    val headlessBody: String,
    val nextHeading: String,
    val nextBody: String,
)

private val overviewEnglish: OverviewTexts = OverviewTexts(
    title = "Overview",
    summary = "A Kotlin/JS wrapper for Tiptap core and StarterKit: a typed options object, a typed " +
        "command chain, and the document as HTML or text.",
    docsLink = "Documentation",
    sourceLink = "Source repository",
    apiLink = "API reference",
    purposeHeading = "What it wraps",
    body = "Tiptap keeps a headless ProseMirror document: it owns the state and you own the rendering. " +
        "In JavaScript the options object, the extension list and the command chain all live in " +
        "untyped literals; the wrapper declares them, so `editorOptions { element = host; … }` is " +
        "checked, `setHeading(HeadingAttributes(1.0))` takes a number, and `TiptapEvents` is a list " +
        "of constants instead of string literals.",
    bullets = listOf(
        "createEditor(editorOptions { … }) — the editor over one element, StarterKit included.",
        "editor.chain().focus()…run() — every toolbar button is one chain.",
        "editor.getHTML() / getText() / isActiveNode(mark) — the document, read back.",
    ),
    headlessHeading = "Headless by design",
    headlessBody = "The editor does not draw its own chrome: the page supplies the toolbar and the " +
        "canvas, and the document renders through the browser. That is why this demo's only output " +
        "is the HTML under the canvas — everything you see above it is the page, not Tiptap.",
    nextHeading = "Reading order",
    nextBody = "The overview, then the editor: format the text with the toolbar and watch the HTML " +
        "and the word count follow every keystroke.",
)

private val overviewChinese: OverviewTexts = OverviewTexts(
    title = "概览",
    summary = "Tiptap core 与 StarterKit 的 Kotlin/JS 封装：选项对象有类型、命令链有类型，文档可以取回 HTML 或纯文本。",
    docsLink = "使用文档",
    sourceLink = "源码仓库",
    apiLink = "API 文档",
    purposeHeading = "它封装了什么",
    body = "Tiptap 持有一份无头的 ProseMirror 文档：它管状态，界面归你。在 JavaScript 里选项对象、扩展列表、" +
        "命令链全是没有类型的字面量；封装把它们声明出来：`editorOptions { element = host; … }` 会被检查，" +
        "`setHeading(HeadingAttributes(1.0))` 收的是数字，`TiptapEvents` 是一组常量而不是字符串字面量。",
    bullets = listOf(
        "createEditor(editorOptions { … }) —— 一个元素上的编辑器，StarterKit 已装好。",
        "editor.chain().focus()…run() —— 每个工具栏按钮就是一条链。",
        "editor.getHTML() / getText() / isActiveNode(mark) —— 把文档读回来。",
    ),
    headlessHeading = "天生无头",
    headlessBody = "编辑器不画自己的界面：工具栏与画布都由页面提供，文档由浏览器渲染。所以这个 demo 唯一的产物" +
        "是画布下面那段 HTML——画布上面看到的一切都是页面，不是 Tiptap。",
    nextHeading = "建议顺序",
    nextBody = "先看概览，再看编辑器：用工具栏格式化文本，HTML 与字数会跟着每一次输入更新。",
)

private val overviewTexts: Translations<OverviewTexts> = texts(
    Langs.EN to overviewEnglish,
    Langs.ZH_HANS to overviewChinese,
)

/**
 * Landing page: what the library wraps, how it is consumed, where the reference lives.
 *
 * The sections are plain blocks, not components: they have nothing to swap at run time, so a
 * component per section would only add indirection.
 */
internal class OverviewPage : DemoPage() {

    private val texts = Binder(overviewTexts)


    init {
        addLink(texts.bind(anchor("", DOCS_URL, external = true), { it.docsLink }))
        addLink(texts.bind(anchor("", API_URL, external = true), { it.apiLink }))
        addLink(texts.bind(anchor("", SOURCE_URL, external = true), { it.sourceLink }))

        val purpose = SectionBlock.NAME { show() }
        texts.bind(purpose.heading, { it.purposeHeading })
        val bullets = HtmlTagName.ul {
            List(3) { index -> texts.bind(+HtmlTagName.li()) { it.bullets.getOrNull(index).orEmpty() } }
        }
        purpose.body.append(texts.bind(paragraph(), { it.body }), bullets)

        val headless = SectionBlock.NAME { show() }
        texts.bind(headless.heading, { it.headlessHeading })
        headless.body.append(
            texts.bind(paragraph(), { it.headlessBody }),
            row(codeText("createEditor(editorOptions { element = host; extensions = arrayOf(starterKit()) })")),
        )

        val next = SectionBlock.NAME { show() }
        texts.bind(next.heading, { it.nextHeading })
        next.body.append(texts.bind(paragraph(), { it.nextBody }))

        body.append(purpose.root, headless.root, next.root)
    }

    override fun applyTranslation(lang: Lang) {
        val current = overviewTexts[lang]
        setTitle(current.title)
        setSummary(current.summary)
        texts.apply(lang)
    }

    companion object : CustomElementReferenceAdapter<OverviewPage>(OverviewPage::class.js, "tiptap-overview-page")
}
