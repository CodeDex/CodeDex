package com.github.codedex;

import android.app.Application;


/**
 * CodeDex Application class
 */

public class CodeDexApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //CodeProcessor.init(this);
    }
}
