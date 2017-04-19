package com.github.codedex.sourceparser.entity.project.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.MetaInterface;
import com.github.codedex.sourceparser.entity.project.MetaPackage;
import com.github.codedex.sourceparser.entity.project.MetaPlaceholder;
import com.github.codedex.sourceparser.fetcher.MetaModelFetcher;
import com.github.codedex.sourceparser.web.javadoc.fetcher.MetaModelJDocV7Fetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 *
 * Abstract class of container for any metadata, f.e. source code in the example of
 * MetaClass or other MetaContainers in MetaPackage.
 */

public abstract class MetaModel {

    private final Type type;
    private final String name;
    private final URL jdocURL;

    private MetaModel parent;
    protected final Set<MetaModel> children = new HashSet<>();

    public enum Type {
        ALL,
        PACKAGE,
        INTERFACE,
        CLASS,
        PLACEHOLDER
    }

    protected MetaModel(Type type, MetaModelFetcher fetcher) {
        this.type = type;
        this.name = fetcher.getName();
        this.jdocURL = fetcher.getJDocURL();
        setParent(fetcher.getParent());
    }

    public Type getType() {
        return this.type;
    }
    public String getName() {
        return this.name;
    }
    public String getFullName() {
        final StringBuilder nameBuilder = new StringBuilder(this.name == null ? "" : this.name);
        MetaModel iterator = this;
        while (!iterator.isRoot()) {
            iterator = iterator.getParent();
            nameBuilder.insert(0, ".");
            nameBuilder.insert(0, iterator.getName());
        }
        return nameBuilder.toString();
    }

    public @Nullable URL getJDocURL() {
        return this.jdocURL;
    }
    public void buildFromJDoc() throws IOException {
        final URL jdocURL = getJDocURL();
        if (jdocURL == null) return;
        final Document entityJDoc = Jsoup.connect(jdocURL.toString()).get();

        final MetaModelJDocV7Fetcher fetcher = new MetaModelJDocV7Fetcher(entityJDoc);
        buildFromFetcher(fetcher);
    }

    void setParent(@Nullable MetaModel parent) {
        if (parent == this.parent) return;  // Redundancy check

        if (this.parent != null)                // Check current parent
            this.parent.children.remove(this);      // Do not use getChildren(), it returns a copy (Immutability, please use setParent())

        if (parent != null)                     // Check new parent
            parent.children.add(this);              // Read above

        this.parent = parent;               // Assign new parent
    }
    public MetaModel getParent() {
        return parent;
    }

    public Set<MetaModel> getChildren() {
        return getChildren((Type[]) null);
    }
    public Set<MetaModel> getChildren(Type... types) {
        if (types == null || new ArrayList<>(Arrays.asList(types)).contains(Type.ALL)) return new HashSet<>(this.children);

        Set<MetaModel> buffer = new HashSet<>();
        for (Type type : types)
            for (MetaModel child : this.children)
                if (type.equals(child.getType()))
                    buffer.add(child);
        return buffer;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public MetaModel getRoot() {
        MetaModel iterator = this;
        while (!iterator.isRoot()) {
            iterator = iterator.getParent();
        }
        return iterator;
    }

    /**
     * Clears up all references to this object that could be out of reach for the user
     * @param newParent Represents the new parent the children of this instance will have
     * @return The new parent
     */
    public MetaModel kill(@Nullable MetaModel newParent) {
        this.jdocURL = null;
        setParent(null);
        for (MetaModel child : children)
            child.setParent(newParent);
        return newParent;
    }

    public static MetaModel getMetaModel(MetaModel.Type type, @NonNull String name, MetaModel parent) {
        switch (type) {
            case CLASS:
                return new MetaClass(name, parent);
            case INTERFACE:
                return new MetaInterface(name, parent);
            case PACKAGE:
                if (parent instanceof MetaPackage)
                    return new MetaPackage(name, (MetaPackage) parent);     // "break;" / "else" missing on purpose
            default:
                return new MetaPlaceholder(name, parent);
        }
    }
}
