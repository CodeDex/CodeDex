package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.model.MetaCodeModel;
import com.github.codedex.sourceparser.entity.model.MetaModel;

/**
 * Created by IPat (Local) on 24.03.2017.
 */

public class MetaInterface extends MetaCodeModel {
    protected MetaInterface(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent, @Nullable String code) {
        super(type, name, parent, code);
    }

    public MetaInterface(@NonNull String name, @Nullable MetaModel parent, @Nullable String code) {
        super(Type.INTERFACE, name, parent, code);
    }
}
