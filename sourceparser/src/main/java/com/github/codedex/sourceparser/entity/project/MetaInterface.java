package com.github.codedex.sourceparser.entity.project;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaInterface extends MetaType {

    private Set<MetaType> superclasses = new HashSet<>();

    protected MetaInterface(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent, Set<MetaType> superclasses, @Nullable String code) {
        super(type, name, parent, code);
    }

    public MetaInterface(@NonNull String name, @Nullable MetaModel parent) {
        this(name, parent, null);
    }

    public MetaInterface(@NonNull String name, @Nullable MetaModel parent, @Nullable String code) {
        this(name, parent, null, code);
    }

    public MetaInterface(@NonNull String name, @Nullable MetaModel parent, Set<MetaType> superclasses, @Nullable String code) {
        super(Type.INTERFACE, name, parent, code);
    }

    protected Set<NonAccessModifier> getDefaultNonAccessModifiers(MetaModel parent) {
        return EnumSet.of(NonAccessModifier.STATIC);
    }
}
