package com.github.codedex.sourceparser.entity.project.specific.javalang;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.MetaPackage;

/**
 * Created by IPat (Local) on 29.03.2017.
 */

public final class MetaJavaObject extends MetaClass {

    private final static MetaJavaObject SINGLETON = new MetaJavaObject();

    private MetaJavaObject() {
        // Passing null as the superclass parameter is an absolute exception and only allowed here, as java.lang.Object is a root class
        super("Object", new MetaPackage("lang", new MetaPackage("java")), null);
    }

    public static MetaJavaObject getMetaObject() {
        return SINGLETON;
    }
}
