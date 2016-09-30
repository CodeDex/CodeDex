package com.github.codedex.codeview

import android.content.Context
import android.util.AttributeSet
import com.github.codedex.codeview.adapters.AbstractCodeAdapter
import com.github.codedex.codeview.adapters.CodeWithNotesAdapter
import com.github.codedex.codeview.views.GestureRecyclerView

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
open class CodeView : GestureRecyclerView {

    /**
     * Default constructor.
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        layoutManager = CodeLayoutManager(context)
        //vCodeList.isNestedScrollingEnabled = true
    }

    /**
     * Initialize RecyclerView with adapter
     * then start highlighting
     */
    fun init(h: Highlighter, adapter: AbstractCodeAdapter<*>) {
        if (h.code.isEmpty()) {
            throw IllegalStateException("Please set code() before init/highlight")
        }

        adapter.setHasStableIds(true)
        this.adapter = adapter

        highlight()
    }

    /**
     * Initialize RecyclerView with adapter
     * then start highlighting
     */
    fun init(adapter: AbstractCodeAdapter<*>) {
        init(adapter.highlighter, adapter)
    }

    /**
     * Initialize RecyclerView with adapter
     * then start highlighting
     */
    fun init(h: Highlighter) {
        init(h, CodeWithNotesAdapter(context, h))
    }

    /**
     * Highlight code by defined programming language.
     * It holds the placeholder on the view until code is highlighted.
     */
    fun highlight() {
        if (this.adapter == null) {
            throw IllegalStateException("Please set adapter or use init(highlighter) before highlight()")
        }

        getCodeAdapter()?.highlight() {
            this.adapter?.notifyDataSetChanged()
        }
    }

    /**
     * Remove code listener.
     */
    fun removeLineClickListener() {
        getCodeAdapter()?.highlighter?.lineClickListener = null
    }

    fun getCodeAdapter() = this.adapter as? AbstractCodeAdapter<*>

    /**
     * Update code.
     */
    fun update(code: String) {
        getCodeAdapter()?.updateCode(code)
    }

    /**
     * Update code.
     */
    fun update(h: Highlighter) {
        init(getCodeAdapter()!!.highlighter.update(h))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightMeasureSpec)
    }
}

/**
 * Provides listener to code line clicks.
 */
interface OnCodeLineClickListener {
    fun onLineClicked(n: Int, line: String)
}
