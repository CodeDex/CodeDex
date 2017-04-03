package com.github.codedex.sourceparser.entity.project;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.project.model.MetaModel;
import com.github.codedex.sourceparser.entity.project.specific.MetaRoot;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaPackage extends MetaModel {

    protected MetaPackage(@NonNull Type type, @NonNull String name, @Nullable MetaPackage parent) {
        super(type, name, parent);
    }

    public MetaPackage(@NonNull String name, @Nullable MetaPackage parent) {
        super(Type.PACKAGE, name, parent == null ? new MetaRoot() : parent);
    }

    public MetaPackage(@NonNull String name) {
        this(name, null);
    }
}
