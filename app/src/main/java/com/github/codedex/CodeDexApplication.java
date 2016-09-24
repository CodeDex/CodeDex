package com.github.codedex;

import android.app.Application;

import com.github.codedex.codeview.classifier.CodeProcessor;

/**
 * Created by fabianterhorst on 24.09.16.
 */

public class CodeDexApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CodeProcessor.init(this);
    }
}
