package com.github.codedex.codeview.adapters

import android.content.Context
import com.github.codedex.codeview.Highlighter
import com.github.codedex.codeview.highlight.ColorThemeData
import com.github.codedex.codeview.views.DiffModel
import com.github.codedex.codeview.views.LineDiffView

/**
 * @class CodeWithDiffsAdapter
 *
 * Code content adapter with ability to add diffs (additions & deletions) in footer.
 *
 */
open class CodeWithDiffsAdapter : AbstractCodeAdapter<DiffModel> {

    constructor(context: Context, h: Highlighter) : super(context, h)

    /**
     * Default constructor.
     */
    constructor(context: Context, content: String, theme: ColorThemeData) : super(context, content, theme)

    /**
     * Create footer view.
     *
     * @param entity Note content
     * @param isFirst Is first footer
     */
    override fun createFooter(context: Context, entity: DiffModel, isFirst: Boolean) =
            LineDiffView.create(context, entity)
}