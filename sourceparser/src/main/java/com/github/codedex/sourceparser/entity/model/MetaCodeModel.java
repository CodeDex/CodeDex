package com.github.codedex.sourceparser.entity.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by IPat (Local) on 24.03.2017.
 */

public abstract class MetaCodeModel extends MetaModel {
    private String code;

    protected MetaCodeModel(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent, @Nullable String code) {
        super(type, name, parent);
        this.code = code;
    }

    @Nullable
    public String getCode() {
        return this.code;
    }

    public void setCode(@Nullable String code) {
        this.code = code;
    }

    // TODO: Every MetaCodeModel happens to contain Methods in the section "Method Detail".
    // An enum also has "Enum Constant Detail".
    // There is usually a Method Summary at the top, which shouldn't be used mainly, since the bottom is more descriptive. Though, it also contains information about method inheritance.
    // Classes and enums show their inheritance tree at the top, while interfaces just show direct sub- or superinterfaces.
    // Classes and enums have a "Constructor Detail" section as well. (And also Constructor Summary, but other than Method Summary it doesn't contain any additional information).

    // DECISION: MetaEnums can probably extend from MetaClass, since they're displayed as a class themselves. Do implement custom analyzing behaviour for Enum constants, though!
    // Enums do only differ in their behaviour of extending classes. (vice-versa) However, that is important during compile-time of code, not when you're already creating a Javadoc.

}
