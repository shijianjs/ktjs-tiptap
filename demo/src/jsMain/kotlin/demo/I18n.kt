package demo

import web.dom.document
import web.html.HTMLElement
import web.html.HtmlTagName

/** The two languages every demo page carries. */
enum class Language(val code: String) {
    /** English. */
    En("en"),

    /** Simplified Chinese. */
    Zh("zh-Hans"),
}

/**
 * A minimal two-language text table for the demo page.
 *
 * The demo is a Kotlin/JS page rather than a Docusaurus site, so it does not reuse the site's i18n
 * catalogs: every visible string is looked up here and re-applied when the language changes.
 */
class Texts(private val entries: Map<String, Pair<String, String>>) {
    /** The language the page is currently showing. */
    var language: Language = Language.En
        private set

    private val listeners = mutableListOf<() -> Unit>()

    /** The text registered under [key] in the current language. */
    operator fun get(key: String): String = entries[key]?.let { if (language == Language.En) it.first else it.second }
        ?: key

    /** Runs [listener] after every language change. */
    fun onChange(listener: () -> Unit) {
        listeners += listener
    }

    /** Switches to the other language and notifies every listener. */
    fun toggle() {
        language = if (language == Language.En) Language.Zh else Language.En
        document.documentElement?.setAttribute("lang", language.code)
        listeners.forEach { it() }
    }
}

/**
 * The language switch every demo page carries: a button whose label names the language it switches
 * *to*, wired to [texts].
 */
fun languageSwitch(texts: Texts, className: String = "lang"): HTMLElement =
    document.createElement(HtmlTagName.button).apply {
        this.className = web.cssom.ClassName(className)
        setAttribute("type", "button")
        setAttribute("aria-label", "Switch language")
        fun render() {
            textContent = if (texts.language == Language.En) "中文" else "EN"
        }
        render()
        texts.onChange { render() }
        onclick = web.events.EventHandler {
            texts.toggle()
        }
    }
