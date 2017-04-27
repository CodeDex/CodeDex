package com.github.codedex.sourceparser.entity;

/**
 * @author Patrick "IPat" Hein
 */

public interface AccessModifiable {
    enum AccessModifier {
        PRIVATE,
        PACKAGE,
        PROTECTED,
        PUBLIC,
    }

    AccessModifier getAccessModifier();
}
