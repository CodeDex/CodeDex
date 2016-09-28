package com.github.codedex.codeview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by fabianterhorst on 27.09.16.
 */

public class CodeLayoutManager extends LinearLayoutManager {

    public CodeLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
