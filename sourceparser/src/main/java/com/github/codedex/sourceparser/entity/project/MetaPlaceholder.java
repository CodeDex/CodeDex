package com.github.codedex.sourceparser.entity.project;

import com.github.codedex.sourceparser.entity.project.model.MetaModel;

import java.net.URL;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 *
 * Represents a class that wasn't able to be analyzed.
 */
public final class MetaPlaceholder extends MetaModel {

    private final MetaPackage.Updater updater;
    public MetaPackage.Updater getUpdater() {
        return this.updater;
    }

    public MetaPlaceholder(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children) {
        this(new MetaPackage.Updater(name, jdocURL, parent, children));
    }

    protected MetaPlaceholder(MetaPackage.Updater updater) {
        super(updater);
        this.updater = updater;
    }

    public Type getType() {
        return Type.PLACEHOLDER;
    }

    public static class Updater extends MetaModel.Updater {
        protected Updater(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children) {
            super(name, jdocURL, parent, children);
        }
    }
}
