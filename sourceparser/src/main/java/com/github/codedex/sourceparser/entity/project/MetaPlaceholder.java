package com.github.codedex.sourceparser.entity.project;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.project.model.MetaModel;

/**
 * Created by IPat on 01.04.2017.
 */

/**
 * Represents a class that wasn't able to be analyzed.
 */
public final class MetaPlaceholder extends MetaModel {
    private MetaPlaceholder(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent) {
        super(type, name, parent);
    }

    public MetaPlaceholder(@NonNull String name, @Nullable MetaModel parent) {
        this(Type.PLACEHOLDER, name, parent);
    }

    public void replaceWith(@NonNull MetaModel newMetaModel) {
        final MetaModel parent = getParent();
        kill(newMetaModel);
        newMetaModel.setParent(parent);
    }
}
