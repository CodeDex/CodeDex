package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.model.MetaModel;
import com.github.codedex.sourceparser.exception.ParentTypeException;

/**
 * Created by IPat (Local) on 12.03.2017.
 */

public class MetaPackage extends MetaModel {
    public MetaPackage(@NonNull String name, @Nullable MetaPackage parent) {
        super(Type.PACKAGE, name, parent);
    }

    public MetaPackage(@NonNull String name) {
        this(name, null);
    }
}
