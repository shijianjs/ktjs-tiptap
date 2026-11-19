package demo

import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.i18n.SiteTexts
import io.github.shijianjs.ktjs.demo.i18n.Translations
import io.github.shijianjs.ktjs.demo.i18n.texts

/**
 * Every string the layout and the navigation show, in English.
 *
 * One object per language, declared once in [siteTexts]; adding a language is one more entry there
 * plus one in `LanguageState.languages`, and no existing string is touched.
 */
private val englishSite: SiteTexts = SiteTexts(
    siteTitle = "ktjs-tiptap",
    langLabel = "English",
    languageLabel = "Language",
    themeLabel = "Theme",
    docsLabel = "Docs",
    siteDocsLabel = "Documentation",
    apiLabel = "API reference",
    sourceLabel = "GitHub",
    lightLabel = "Light",
    darkLabel = "Dark",
    systemLabel = "Follow browser",
    groupTitles = mapOf(
        "start" to "Getting started",
        "examples" to "Examples",
    ),
    pageTitles = mapOf(
        "overview" to "Overview",
        "editor" to "Editor",
    ),
)

private val chineseSite: SiteTexts = SiteTexts(
    siteTitle = "ktjs-tiptap",
    langLabel = "中文",
    languageLabel = "语言",
    themeLabel = "外观",
    docsLabel = "文档",
    siteDocsLabel = "使用文档",
    apiLabel = "API 文档",
    sourceLabel = "GitHub 仓库",
    lightLabel = "亮色",
    darkLabel = "暗色",
    systemLabel = "跟随浏览器",
    groupTitles = mapOf(
        "start" to "开始",
        "examples" to "示例",
    ),
    pageTitles = mapOf(
        "overview" to "概览",
        "editor" to "编辑器",
    ),
)

/** The layout's strings, one entry per registered language. */
val siteTexts: Translations<SiteTexts> = texts(
    Langs.EN to englishSite,
    Langs.ZH_HANS to chineseSite,
)
