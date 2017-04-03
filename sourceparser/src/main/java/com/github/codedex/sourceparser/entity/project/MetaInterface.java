package com.github.codedex.sourceparser.entity.project;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaInterface extends MetaType {

    private AccessModifier accessModifier = AccessModifier.PACKAGE;
    private Set<NonAccessModifier> nonAccessModifiers = EnumSet.noneOf(NonAccessModifier.class);

    protected MetaInterface(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent, MetaType superclass, @Nullable String code) {
        super(type, name, parent, superclass, code);
    }

    public MetaInterface(@NonNull String name, @Nullable MetaModel parent) {
        this(name, parent, null);
    }

    public MetaInterface(@NonNull String name, @Nullable MetaModel parent, @Nullable String code) {
        this(name, parent, null, code);
    }

    public MetaInterface(@NonNull String name, @Nullable MetaModel parent, MetaType superclass, @Nullable String code) {
        super(Type.INTERFACE, name, parent, superclass, code);
    }

    @Override
    public Set<NonAccessModifier> getNonAccessModifiers() {
        return this.nonAccessModifiers;
    }

    @Override
    public AccessModifier getAccessModifier() {
        return accessModifier;
    }
}
