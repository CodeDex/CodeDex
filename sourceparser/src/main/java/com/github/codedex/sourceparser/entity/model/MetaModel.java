package com.github.codedex.sourceparser.entity.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class for container for any metadata, f.e. source code in the example of
 * MetaClass or other MetaContainers in MetaPackage.
 */

public abstract class MetaModel {

    private Type type;
    private final String name;
    private URL docURL;

    private MetaModel parent;
    private final Set<MetaModel> children;

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
        this.children = new HashSet<>();
        setParent(parent);
    }

    public Type getType() {
        return this.type;
    }
    public @NonNull String getName() {
        return this.name;
    }
    public void setDocURL(URL docURL) {
        this.docURL = docURL;
    }
    public @Nullable URL getDocURL() {
        return this.docURL;
    }

    public void setParent(@Nullable MetaModel parent) {
        if (parent == null)
            this.parent = null;
        else if (!parent.getChildren().contains(this)) {
            if (this.parent != null)
                this.parent.getChildren().remove(this);
            parent.addChild(this);
            this.parent = parent;
        }
    }
    public MetaModel getParent() {
        return parent;
    }

    public void addChild(@NonNull MetaModel child) {
        child.setParent(this);
        this.children.add(child);
    }
    public Set<MetaModel> getChildren() {
        return getChildren((Type[]) null);
    }
    public Set<MetaModel> getChildren(Type... types) {
        if (types == null || new ArrayList<>(Arrays.asList(types)).contains(Type.ALL)) return this.children;

        Set<MetaModel> buffer = new HashSet<>();
        for (Type type : types)
            for (MetaModel child : this.children)
                if (type.equals(child.getType()))
                    buffer.add(child);
        return buffer;
    }
}
