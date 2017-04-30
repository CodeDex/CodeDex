package com.github.codedex.sourceparser.entity.project.model;

import com.github.codedex.sourceparser.entity.MetaMutable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.codedex.sourceparser.Utils.checkSet;

/**
 * @author Patrick "IPat" Hein
 *
 * Abstract class of container for any metadata, f.e. source code in the example of
 * MetaClass or other MetaContainers in MetaPackage.
 *
 * Only used in project context, for elements that can have children / parents.
 *
 * (Don't confuse parents with superclasses:)
 * @see MetaType
 */

public abstract class MetaModel implements MetaMutable {

    protected final Updater updater;

    public enum Type {
        ALL,
        PACKAGE,
        INTERFACE,
        CLASS,
        PLACEHOLDER // Decided against "OTHER" as name, since unresolved classes really are supposed to be resolved and "OTHER" maybe doesn't bring that across
    }

    protected MetaModel(Type type, String name, URL jdocURL, MetaModel parent, Set<MetaModel> children) {
        this.updater = new Updater(type, name, jdocURL, parent, checkSet(children));
    }

    protected MetaModel(Updater updater) {
        this.updater = updater;
    }

    public Type getType() {
        return updater.type;
    }
    public String getName() {
        return updater.name;
    }
    public URL getJDocURL() {
        return updater.jdocURL;
    }
    public MetaModel getParent() {
        return updater.parent;
    }
    public Set<MetaModel> getChildren() {
        return getChildren((Type[]) null);
    }
    public Updater getUpdater() {
        return this.updater;
    }

    /**
     * @see com.github.codedex.sourceparser.entity.MetaMutable.MetaUpdater
     * The Updater is implemented as an information reference and interface to it at the same time.
     */
    public static class Updater implements MetaUpdater {
        private Type type;
        private String name;
        private URL jdocURL;
        private MetaModel parent;
        private final Set<MetaModel> children;

        private final Set<MetaModel> modChildren;

        protected Updater(Type type, String name, URL jdocURL, MetaModel parent, Set<MetaModel> children) {
            this.type = type;
            this.name = name;
            this.jdocURL = jdocURL;
            this.parent = parent;
            this.children = Collections.unmodifiableSet(children);

            this.modChildren = children;
        }

        public void setType(Type type) {
            this.type = type;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setJdocURL(URL jdocURL) {
            this.jdocURL = jdocURL;
        }
        public void setParent(MetaModel parent) {
            this.parent = parent;
        }
        public Set<MetaModel> getChildren() {
            return this.modChildren;
        }
    }

    public Set<MetaModel> getChildren(Type... types) {
        if (types == null || new ArrayList<>(Arrays.asList(types)).contains(Type.ALL)) return updater.children;

        Set<MetaModel> buffer = new LinkedHashSet<>();
        for (Type type : types)
            for (MetaModel child : updater.children)
                if (type.equals(child.getType()))
                    buffer.add(child);
        return Collections.unmodifiableSet(buffer);
    }

    public boolean isRoot() {
        return updater.parent == null;
    }

    public MetaModel getRoot() {
        MetaModel iterator = this;
        while (!iterator.isRoot())
            iterator = iterator.getParent();
        return iterator;
    }

    public String getFullName() {
        final StringBuilder nameBuilder = new StringBuilder(updater.name == null ? "" : updater.name);
        for (MetaModel iterator = this; !iterator.isRoot(); ) {
            iterator = iterator.getParent();
            nameBuilder.insert(0, ".");
            nameBuilder.insert(0, iterator.getName());
        }
        return nameBuilder.toString();
    }
}
