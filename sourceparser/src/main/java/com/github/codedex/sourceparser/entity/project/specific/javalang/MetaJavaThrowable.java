package com.github.codedex.sourceparser.entity.project.specific.javalang;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.MetaPackage;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaJavaThrowable extends MetaClass {
    private final static MetaJavaThrowable SINGLETON = new MetaJavaThrowable();

    private MetaJavaThrowable() {
        super("Throwable", MetaJavaLangPackage.getMetaPackage());
    }

    public static MetaJavaThrowable getMetaThrowable() {
        return SINGLETON;
    }
}
