package com.github.codedex.sourceparser.entity.project.specific;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;

/**
 * Created by IPat (Local) on 29.03.2017.
 */

public class MetaThrowable extends MetaClass {
    public MetaThrowable(@NonNull String name, @Nullable MetaModel parent, MetaThrowable superclass, @Nullable String code) {
        super(name, parent, superclass, code);
    }
}
