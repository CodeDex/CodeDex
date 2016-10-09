package com.github.codedex.codeview

import android.content.Context
import android.util.AttributeSet
import com.github.codedex.codeview.highlight.CodeHighlighter
import com.github.codedex.codeview.items.CodeLineItem
import com.github.codedex.codeview.views.GestureRecyclerView
import com.mikepenz.fastadapter.adapters.FastItemAdapter

/**
 * @class CodeView
 *
 * Presents your code content.
 *
 * Before view built or started to, as the first step, placeholder
 * measures & prepare place for code view. Amount of view params is
 * not big, view has mutable state & non-standard initialization behavior.
 * That is why there is no usual & well-known Builder pattern implementation.
 *
 * To control interaction state, being & built, was selected tasks queue.
 * If user has already built view his task performs immediately, otherwise
 * it puts in queue to awaiting adapter creation & processing by build flow.
 * This helps to avoid errors & solve the init tasks in more elegant way.
 *
 */
open class CodeView

(context: Context, attrs: AttributeSet) : GestureRecyclerView(context, attrs) {

    /**
     * Initialize RecyclerView with adapter
     * then start highlighting
     */
    fun init(highlighter: Highlighter) {
        val fastItemAdapter = FastItemAdapter<CodeLineItem>()
        this.adapter = fastItemAdapter
        Thread.async() {
            val allLines = extractLines(highlighter.code)
            val items = allLines.map { CodeLineItem(it) }
            Thread.ui {
                fastItemAdapter.setNewList(items)
            }
            val code = CodeHighlighter.highlight(highlighter.language!!, highlighter.code, highlighter.theme)
            for ((index,spannable) in code.spannable.withIndex()) {
                fastItemAdapter.getItem(index).spannable = spannable
            }
            Thread.ui {
                fastItemAdapter.notifyAdapterDataSetChanged()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightMeasureSpec)
    }

    init {
        layoutManager = CodeLayoutManager(context)
    }
}