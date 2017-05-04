package com.github.codedex.sourceparser.entity.project.specific;

import com.github.codedex.sourceparser.entity.project.MetaPackage;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;

import java.util.LinkedHashSet;

/**
 * @author Patrick "IPat" Hein
 */

public final class MetaRoot extends MetaPackage {

    private final Updater updater;
    public Updater getUpdater() {
        return this.updater;
    }

    public MetaRoot() {
        this(new Updater(""));
    }

    protected MetaRoot(Updater updater) {
        super(updater);
        this.updater = updater;
    }

    public static class Updater extends MetaPackage.Updater {

        protected Updater(String name) {
            super(name, null, null, new LinkedHashSet<MetaModel>());
        }

        @Override
        public void setParent(MetaModel parent) {
            throw new UnsupportedOperationException("Attempted assigning MetaRoot a parent: Can't assign root a parent");
        }
    }
}
