package com.github.codedex.sourceparser.entity;

import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public interface NonAccessModifiable {
    enum NonAccessModifier {
        FINAL,
        STATIC,
        ABSTRACT,
        SYNCHRONIZED,
        VOLATILE,
        TRANSIENT
    }

    Set<NonAccessModifier> getNonAccessModifiers();


}
