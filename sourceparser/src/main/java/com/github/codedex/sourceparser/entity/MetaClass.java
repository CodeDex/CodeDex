package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.model.MetaCodeModel;
import com.github.codedex.sourceparser.entity.model.MetaModel;

/**
 * Created by IPat (Local) on 24.03.2017.
 */

public class MetaClass extends MetaCodeModel {
    protected MetaClass(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent, @Nullable String code) {
        super(type, name, parent, code);
    }

    public MetaClass(@NonNull String name, @Nullable MetaModel parent, @Nullable String code) {
        super(Type.CLASS, name, parent, code);
    }
}
