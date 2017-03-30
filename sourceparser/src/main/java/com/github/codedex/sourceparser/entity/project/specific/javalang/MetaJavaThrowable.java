package com.github.codedex.sourceparser.entity.project.specific.javalang;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.MetaPackage;

/**
 * Created by IPat (Local) on 30.03.2017.
 */

public class MetaJavaThrowable extends MetaClass {
    private final static MetaJavaThrowable SINGLETON = new MetaJavaThrowable();

    private MetaJavaThrowable() {
        super("Throwable", new MetaPackage("lang", new MetaPackage("java")));
    }

    public static MetaJavaThrowable getMetaThrowable() {
        return SINGLETON;
    }
}
