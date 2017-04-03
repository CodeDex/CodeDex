package com.github.codedex.sourceparser.entity.project.specific.javalang;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.MetaPackage;

/**
 * @author Patrick "IPat" Hein
 */

public final class MetaJavaEnum extends MetaClass {

    private final static MetaJavaEnum SINGLETON = new MetaJavaEnum();

    private MetaJavaEnum() {
        super("Enum", MetaJavaLangPackage.getMetaPackage());
    }

    public static MetaJavaEnum getMetaEnum() {
        return SINGLETON;
    }
}
