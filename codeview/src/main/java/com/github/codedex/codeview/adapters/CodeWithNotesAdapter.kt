package com.github.codedex.codeview.adapters

import android.content.Context
import com.github.codedex.codeview.Highlighter
import com.github.codedex.codeview.highlight.ColorThemeData
import com.github.codedex.codeview.highlight.color
import com.github.codedex.codeview.views.LineNoteView

/**
 * @class CodeWithNotesAdapter
 *
 * Default code content adapter.
 *
 */
open class CodeWithNotesAdapter : AbstractCodeAdapter<String> {

    constructor(context: Context, h: Highlighter) : super(context, h)

    /**
     * Default constructor.
     */
    constructor(context: Context, content: String, colorTheme: ColorThemeData) : super(context, content, colorTheme)

    //todo: inflateFooter(int layoutId)

    /**
     * Create footer view.
     *
     * @param entity Note content
     * @param isFirst Is first footer view
     */
    override fun createFooter(context: Context, entity: String, isFirst: Boolean) =
            LineNoteView.create(context,
                    text = entity,
                    isFirst = isFirst,
                    bgColor = highlighter.theme.bgNum.color(),
                    textColor = highlighter.theme.noteColor.color())
}