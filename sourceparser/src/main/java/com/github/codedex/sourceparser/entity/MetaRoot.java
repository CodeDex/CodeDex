package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;

import com.github.codedex.sourceparser.entity.model.MetaModel;

/**
 * Created by IPat (Local) on 22.03.2017.
 */

public class MetaRoot extends MetaPackage {
    public MetaRoot() {
        super("");
    }

    @Override
    public void setParent(MetaModel parent) {
        throw new RuntimeException("Can't assign root a parent");
        /* WORKAROUND
        for (MetaModel child : getChildren()) {
            child.setParent(parent);
        }
         */
    }

    @NonNull
    @Override
    public String getName() {
        return super.getName();
    }
}
