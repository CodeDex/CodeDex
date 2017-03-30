package com.github.codedex.sourceparser.entity;

import java.util.Set;

/**
 * Created by IPat (Local) on 28.03.2017.
 */

public interface Modifiable {
    enum NonAccessModifier {
        FINAL,
        STATIC,
        ABSTRACT,
        SYNCHRONIZED,
        VOLATILE,
        TRANSIENT
    }
    Set<NonAccessModifier> getNonAccessModifiers();

    interface AccessModifiable extends Modifiable {
        enum AccessModifier {
            PRIVATE,
            PACKAGE,
            PROTECTED,
            PUBLIC,
        }

        AccessModifier getAccessModifier();
    }
}
