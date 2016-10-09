package com.github.codedex.codeview.items;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.github.codedex.codeview.R;
import com.github.codedex.codeview.highlight.MonoFontCache;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

/**
 * Created by fabianterhorst on 09.10.16.
 */

public class CodeLineItem extends AbstractItem<CodeLineItem, CodeLineItem.ViewHolder> {

    private String line;

    private Spanned spannable;

    public CodeLineItem(String line) {
        this.line = line;
    }

    public CodeLineItem(Spanned spannable) {
        this.spannable = spannable;
    }

    public void setSpannable(Spanned spannable) {
        this.spannable = spannable;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Spanned getSpannable() {
        return spannable;
    }

    public String getLine() {
        return line;
    }

    @Override
    public int getType() {
        return R.id.code_line_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_code_line;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);
        if (spannable == null) {
            viewHolder.lineContent.setText(line);
        } else {
            viewHolder.lineContent.setText(spannable);
        }
        viewHolder.lineNumber.setText(String.valueOf(viewHolder.getAdapterPosition() + 1));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView lineNumber;

        private TextView lineContent;

        public ViewHolder(View view) {
            super(view);
            lineNumber = (TextView) view.findViewById(R.id.tv_line_num);
            lineContent = (TextView) view.findViewById(R.id.tv_line_content);
            Typeface monoTypeFace = MonoFontCache.getInstance(view.getContext()).getTypeface();
            lineNumber.setTypeface(monoTypeFace);
            lineContent.setTypeface(monoTypeFace);
        }
    }
}
