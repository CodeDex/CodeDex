package com.github.codedex.sourceparser.entity.project.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class of container for any metadata, f.e. source code in the example of
 * MetaClass or other MetaContainers in MetaPackage.
 */

public abstract class MetaModel {

    private Type type;
    private final String name;
    private URL docURL;

    private MetaModel parent;
    protected final Set<MetaModel> children = new HashSet<>();

    public enum Type {
        ALL,
        PACKAGE,
        ENUM,
        INTERFACE,
        CLASS
    }

    protected MetaModel(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent) {
        this.type = type;
        this.name = name;
        setParent(parent);
    }

    public Type getType() {
        return this.type;
    }
    public @NonNull String getName() {
        return this.name;
    }
    // TODO: Make immutable: Move DocURL setter to constructor and create builder, enabling copying and overloading an existing MetaModel
    public void setDocURL(URL docURL) {
        this.docURL = docURL;
    }
    public @Nullable URL getDocURL() {
        return this.docURL;
    }

    protected void setParent(@Nullable MetaModel parent) {
        if (parent == this.parent) return;  // Redundancy check

        if (this.parent != null)                // Check current parent
            this.parent.children.remove(this);      // Do not use getChildren(), it returns a copy (Immutability)

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
}
