package com.github.codedex.sourceparser.entity.project.specific;

import android.support.annotation.NonNull;

import com.github.codedex.sourceparser.entity.project.MetaPackage;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;

/**
 * Created by IPat (Local) on 22.03.2017.
 */

public class MetaRoot extends MetaPackage {
    public MetaRoot() {
        super(Type.PACKAGE, "", null);
    }

    @Override
    protected void setParent(MetaModel parent) {
        throw new RuntimeException("Attempted assigning MetaRoot a parent: Can't assign root a parent");
    }

    @NonNull
    public String getName() {
        return super.getName();
    }

    public boolean isRoot() {
        return true;
    }
}
