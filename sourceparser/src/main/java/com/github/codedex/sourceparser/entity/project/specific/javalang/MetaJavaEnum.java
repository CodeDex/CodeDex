package com.github.codedex.sourceparser.entity.project.specific.javalang;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.MetaPackage;

/**
 * Created by IPat (Local) on 29.03.2017.
 */

public final class MetaJavaEnum extends MetaClass {

    private final static MetaJavaEnum SINGLETON = new MetaJavaEnum();

    private MetaJavaEnum() {
        super("Enum", new MetaPackage("lang", new MetaPackage("java")));
    }

    public static MetaJavaEnum getMetaEnum() {
        return SINGLETON;
    }
}
