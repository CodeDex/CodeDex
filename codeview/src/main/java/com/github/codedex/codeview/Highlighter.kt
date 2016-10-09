package com.github.codedex.codeview

import android.content.Context
import com.github.codedex.codeview.highlight.ColorTheme
import com.github.codedex.codeview.highlight.ColorThemeData

data class Highlighter(val context: Context) {

    var theme: ColorThemeData = ColorTheme.DEFAULT.theme()
    var code = ""
    var language: String? = null
    var codeResId = 0
    var themeUpdated = false
    var codeUpdated = false
    var languageUpdated = false
    var codeResIdUpdated = false

    fun language(language: String): Highlighter {
        this.language = language
        languageUpdated = true
        return this
    }

    fun code(code: String): Highlighter {
        this.code = code
        codeUpdated = true
        return this
    }

    fun code(codeResId: Int): Highlighter {
        this.code = context.getString(codeResId)
        codeResIdUpdated = true
        return this
    }

    fun theme(theme: ColorTheme): Highlighter {
        return this.theme(theme.theme())
    }

    fun theme(theme: ColorThemeData): Highlighter {
        this.theme = theme
        themeUpdated = true
        return this
    }

    /**
     * Highlight code finally.
     */
    fun highlight(codeView: CodeView) {
        codeView.init(this)
    }

    /**
     * Update highlighter.
     */
    fun update(newSettings: Highlighter): Highlighter {
        if (newSettings.themeUpdated) {
            theme = newSettings.theme
        }
        if (newSettings.codeUpdated) {
            code = newSettings.code
        }
        if (newSettings.languageUpdated) {
            language = newSettings.language
        }
        if (newSettings.codeResIdUpdated) {
            codeResId = newSettings.codeResId
        }

        return this
    }
}