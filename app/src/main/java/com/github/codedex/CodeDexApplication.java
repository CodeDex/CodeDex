package com.github.codedex;

import android.app.Application;

import com.github.codedex.codeview.classifier.CodeProcessor;


/**
 * CodeDex Application class
 */

public class CodeDexApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CodeProcessor.init(this);
    }
}
