package com.github.codedex.codeview.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.codedex.codeview.*
import com.github.codedex.codeview.Thread.async
import com.github.codedex.codeview.Thread.ui
import com.github.codedex.codeview.classifier.CodeClassifier
import com.github.codedex.codeview.classifier.CodeProcessor
import com.github.codedex.codeview.highlight.*
import java.util.*

/**
 * @class AbstractCodeAdapter
 *
 * Basic adapter for code view.
 *
 */
abstract class AbstractCodeAdapter<T> : RecyclerView.Adapter<AbstractCodeAdapter.ViewHolder> {

    private val mContext: Context
    private var mContent: String
    private var mLines: List<String>
    private val mMaxLines: Int
    private var mDroppedLines: List<String>?

    internal var isFullShowing: Boolean

    internal var codeListener: OnCodeLineClickListener?

    internal var colorTheme: ColorThemeData
        set(colorTheme) {
            field = colorTheme
            notifyDataSetChanged()
        }

    internal var footerEntities: HashMap<Int, List<T>>
        set(footerEntities) {
            field = footerEntities
            notifyDataSetChanged()
        }

    init {
        mLines = ArrayList()
        mDroppedLines = null
        isFullShowing = true
        colorTheme = ColorTheme.SOLARIZED_LIGHT.with()
        footerEntities = HashMap()
    }

    /**
     * Adapter constructor.
     *
     * @param content Context
     * @param content Code content
     * @param isShowFull Do you want to show all code content?
     * @param maxLines Max lines to show (when limit is reached, rest is dropped)
     * @param shortcutNote When rest lines is dropped, note is shown as last string
     * @param listener Listener to code line clicks
     */
    constructor(context: Context,
                content: String,
                isShowFull: Boolean = true,
                maxLines: Int = MAX_SHORTCUT_LINES,
                shortcutNote: String = context.getString(R.string.show_all),
                listener: OnCodeLineClickListener? = null) {
        mContext = context
        mContent = content
        mMaxLines = maxLines
        codeListener = listener

        if (isShowFull) {
            isFullShowing = true
            mLines = extractLines(content)
        } else
            initCodeContent(isShowFull, shortcutNote)
    }

    /**
     * Split code content by lines. If listing must not be shown full it shows
     * only necessary lines & rest are dropped (and stores in named variable).
     *
     * @param isShowFull Show full listing?
     * @param shortcutNote Note will shown below code for listing shortcut
     */
    private fun initCodeContent(isShowFull: Boolean,
                                shortcutNote: String = showAllBottomNote()) {
        var lines: MutableList<String> = ArrayList(extractLines(mContent))
        isFullShowing = isShowFull || lines.size <= mMaxLines // limit is not reached

        if (!isFullShowing) {
            mDroppedLines = ArrayList(lines.subList(mMaxLines, lines.lastIndex))
            lines = lines.subList(0, mMaxLines)
            lines.add(shortcutNote.toUpperCase())
        }
        mLines = lines
    }

    // - Adapter interface

    /**
     * Update code with new content.
     *
     * @param newContent New code content
     */
    fun updateCodeContent(newContent: String) {
        mContent = newContent
        initCodeContent(isFullShowing)
        notifyDataSetChanged()
    }

    /**
     * Add footer entity for code line.
     *
     * @param num Line number
     * @param entity Footer entity
     */
    fun addFooterEntity(num: Int, entity: T) {
        val notes = footerEntities[num] ?: ArrayList()
        footerEntities.put(num, notes + entity)
        notifyDataSetChanged()
    }

    /**
     * Highlight code content.
     *
     * @param onReady Callback when content is highlighted
     * @param language Programming language to highlight
     */
    fun highlightCode(language: String? = null, onReady: () -> Unit) {
        async() {
            val classifiedLanguage = language ?: classifyContent()
            highlighting(classifiedLanguage, onReady)
        }
    }

    /**
     * Mapper from entity to footer view.
     *
     * @param context Context
     * @param entity Entity to init view
     * @param isFirst Is first footer view
     * @return Footer view
     */
    abstract fun createFooter(context: Context, entity: T, isFirst: Boolean): View

    // - Helpers (for accessors)

    /**
     * Classify current code content.
     *
     * @return Classified language
     */
    private fun classifyContent(): String {
        val processor = CodeProcessor.getInstance(mContext)

        val language = if (processor.isTrained)
            processor.classify(mContent).get()
        else CodeClassifier.DEFAULT_LANGUAGE

        return language
    }

    /**
     * Highlight code content by language.
     *
     * @param language Language to highlight
     * @param onReady Callback
     */
    private fun highlighting(language: String, onReady: () -> Unit) {
        val code = CodeHighlighter.highlight(language, mContent, colorTheme)
        val lines = extractLines(code)
        updateContent(lines, onReady)
    }

    /**
     * Return control to UI-thread when highlighted content is ready.
     *
     * @param codeLines Highlighted code lines
     * @param onUpdated Control callback
     */
    private fun updateContent(codeLines: List<String>, onUpdated: () -> Unit) {
        ui {
            mLines = codeLines
            onUpdated()
        }
    }

    private fun showAllBottomNote() = mContext.getString(R.string.show_all)
    
    private fun monoTypeface() = MonoFontCache.getInstance(mContext).typeface

    // - View holder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val lineView = inflater.inflate(R.layout.item_code_line, parent, false)
        lineView.setBackgroundColor(colorTheme.bgContent.color())

        val tvLineNum = lineView.findViewById(R.id.tv_line_num) as TextView
        tvLineNum.typeface = monoTypeface()
        tvLineNum.setTextColor(colorTheme.numColor.color())
        tvLineNum.setBackgroundColor(colorTheme.bgNum.color())

        val tvLineContent = lineView.findViewById(R.id.tv_line_content) as TextView
        tvLineContent.typeface = monoTypeface()

        return ViewHolder(lineView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val codeLine = mLines[position]
        holder.mItem = codeLine

        holder.itemView.setOnClickListener {
            codeListener?.onCodeLineClicked(position, codeLine)
        }

        setupLine(position, codeLine, holder)
        displayLineFooter(position, holder)
        addExtraPadding(position, holder)
    }

    override fun getItemCount() = mLines.size

    private fun Int.isFirst() = this == 0
    private fun Int.isLast() = this == itemCount - 1
    private fun Int.isJustFirst() = isFirst() && !isLast()
    private fun Int.isJustLast() = isLast() && !isFirst()
    private fun Int.isBorder() = isFirst() || isLast()

    // - Helpers (for view holder)

    private fun setupLine(position: Int, line: String, holder: ViewHolder) {
        holder.tvLineContent.text = html(line)
        holder.tvLineContent.setTextColor(colorTheme.noteColor.color())

        if (!isFullShowing && position == MAX_SHORTCUT_LINES) {
            holder.tvLineNum.textSize = 10f
            holder.tvLineNum.text = mContext.getString(R.string.dots)
        } else {
            holder.tvLineNum.textSize = 12f
            holder.tvLineNum.text = "${position + 1}"
        }
    }

    private fun displayLineFooter(position: Int, holder: ViewHolder) {
        val entityList = footerEntities[position]

        holder.llLineFooter.removeAllViews()

        entityList?.let {
            holder.llLineFooter.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE

            var isFirst = true

            it.forEach { entity ->
                val footerView = createFooter(mContext, entity, isFirst)

                holder.llLineFooter.addView(footerView)

                isFirst = false
            }
        }
    }

    private fun addExtraPadding(position: Int, holder: ViewHolder) {
        if (position.isBorder()) {
            val dp8 = dpToPx(mContext, 8)
            val topPadding = if (position.isJustFirst()) dp8 else 0
            val bottomPadding = if (position.isJustLast()) dp8 else 0
            holder.tvLineNum.setPadding(0, topPadding, 0, bottomPadding)
            holder.tvLineContent.setPadding(0, topPadding, 0, bottomPadding)
        } else {
            holder.tvLineNum.setPadding(0, 0, 0, 0)
            holder.tvLineContent.setPadding(0, 0, 0, 0)
        }
    }

    companion object {
        internal const val MAX_SHORTCUT_LINES = 6
    }

    /**
     * View holder for code adapter.
     * Stores all views related to code line layout.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLineNum: TextView
        var tvLineContent: TextView
        var llLineFooter: LinearLayout

        var mItem: String? = null

        init {
            tvLineNum = itemView.findViewById(R.id.tv_line_num) as TextView
            tvLineContent = itemView.findViewById(R.id.tv_line_content) as TextView
            llLineFooter = itemView.findViewById(R.id.ll_line_footer) as LinearLayout
        }

        override fun toString() = "${super.toString()} '$mItem'"
    }
}
