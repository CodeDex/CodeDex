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

    private Set<MetaConstructor> constructors;
    private MetaType superclass;
    private Set<MetaInterface> implementedInterfaces = new HashSet<>(0);

    protected MetaClass(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent, MetaType superclass, @Nullable String code) {
        super(type, name, parent, code);
        this.superclass = superclass;
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
        super(Type.CLASS, name, parseParent(parent), code);
        this.superclass = superclass;
    }

    private static MetaModel parseParent(MetaModel parent) {
        if (parent != null)
            return parent;
        else
            return new MetaRoot();
    }

    protected Set<NonAccessModifier> getDefaultNonAccessModifiers(MetaModel parent) {
        if (parent == null || parent.getType() != Type.CLASS)
            return EnumSet.of(NonAccessModifier.STATIC);      // Even though this decision is discussable, it should depend on how the classes get displayed / drawn.
        else
            return EnumSet.noneOf(NonAccessModifier.class);
    }
}
