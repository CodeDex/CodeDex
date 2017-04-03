package com.github.codedex.sourceparser.entity;

import java.util.Set;

/**
 * @author Patrick "IPat" Hein
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
