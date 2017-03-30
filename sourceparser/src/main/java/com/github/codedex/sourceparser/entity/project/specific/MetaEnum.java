package com.github.codedex.sourceparser.entity.project.specific;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;
import com.github.codedex.sourceparser.entity.project.specific.javalang.MetaJavaEnum;

/**
 * Created by IPat (Local) on 24.03.2017.
 */

public class MetaEnum extends MetaClass {
    protected MetaEnum(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent, @Nullable String code) {
        super(type, name, parent, MetaJavaEnum.getMetaEnum(), code);
    }

    public MetaEnum(@NonNull String name, @Nullable MetaModel parent, @Nullable String code) {
        this(Type.ENUM, name, parent, code);
    }
}
