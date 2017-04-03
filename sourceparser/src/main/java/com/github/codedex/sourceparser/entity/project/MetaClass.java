package com.github.codedex.sourceparser.entity.project;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;
import com.github.codedex.sourceparser.entity.project.specific.javalang.MetaJavaObject;
import com.github.codedex.sourceparser.entity.project.specific.MetaRoot;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaClass extends MetaType {

    private AccessModifier accessModifier = AccessModifier.PACKAGE;
    private Set<NonAccessModifier> nonAccessModifiers;
    private Set<MetaInterface> implementedInterfaces = new HashSet<>();

    protected MetaClass(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent, MetaType superclass, @Nullable String code) {
        super(type, name, parent, superclass, code);
    }

    public MetaClass(@NonNull String name) {
        this(name, null);
    }

    public MetaClass(@NonNull String name, MetaModel parent) {
        this(name, parent, MetaJavaObject.getMetaObject());
    }

    public MetaClass(@NonNull String name, MetaModel parent, @NonNull MetaType superclass) {
        this(name, parent, superclass, null);
    }

    public MetaClass(@NonNull String name, MetaModel parent, @NonNull MetaType superclass, @Nullable String code) {
        super(Type.CLASS, name, parseParent(parent), superclass, code);
        setDefaultNonAccessModifiers(parent);
    }

    private static MetaModel parseParent(MetaModel parent) {
        if (parent != null)
            return parent;
        else
            return new MetaRoot();
    }

    private void setDefaultNonAccessModifiers(MetaModel parent) {
        if (parent instanceof MetaType)                                 // Parent is class / interface
            nonAccessModifiers = EnumSet.noneOf(NonAccessModifier.class);
        else                                                            // Parent is package
            nonAccessModifiers = EnumSet.of(NonAccessModifier.STATIC);      // Even though this decision is discussable, it should depend on how the classes get displayed.
    }

    public AccessModifier getAccessModifier() {
        return this.accessModifier;
    }

    public Set<NonAccessModifier> getNonAccessModifiers() {
        return this.nonAccessModifiers;
    }
}
