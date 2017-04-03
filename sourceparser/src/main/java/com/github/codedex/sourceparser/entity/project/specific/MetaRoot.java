package com.github.codedex.sourceparser.entity.project.specific;

import android.support.annotation.NonNull;

import com.github.codedex.sourceparser.entity.project.MetaPackage;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaRoot extends MetaPackage {
    public MetaRoot() {
        super(Type.PACKAGE, "", null);
    }

    @Override
    public void setParent(MetaModel parent) {
        throw new RuntimeException("Attempted assigning MetaRoot a parent: Can't assign root a parent");
    }

    @NonNull
    public String getName() {
        return super.getName();
    }
}
