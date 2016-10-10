package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains metadata for package.
 */

public class MetaContainer {

    private ParentReference data;

    public interface ParentReference {
        MetaContainer getParentPackage();
    }

    public enum Type {
        PACKAGE,
        CLASS,
        INTERFACE
    }

    private final Type type;
    private String name;
    private List<MetaContainer> packages;
    private List<MetaContainer> classes;
    private List<MetaContainer> interfaces;
    private URL docURL;

    public void setData(ParentReference data) {
        this.data = data;
    }

    /**
     * Method for getting an instance of a root container for MetaInheritables
     */
    public static @NonNull MetaContainer getNewRootPackage() {
        return new MetaContainer();
    }

    private MetaContainer() {
        this.type = Type.PACKAGE;
        this.name = null;
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.interfaces = new ArrayList<>();
        setData(new ParentReference() {
                @Override
                public MetaContainer getParentPackage() {
                    return null;
                }
            });
    }

    // When calling this constructor, make sure you declare the parent package some point later
    public MetaContainer(@NonNull Type type, @NonNull String name) {
        this(type, name, new ParentReference() {
            @Override
            public MetaContainer getParentPackage() {
                return null;
            }
        });
    }

    public MetaContainer(@NonNull Type type, @NonNull String name, @Nullable URL docURL) {// When calling this constructor, make sure you declare the parent package some point later
        this(type, name, new ParentReference() {
            @Override
            public MetaContainer getParentPackage() {
                return null;
            }
        }, docURL);
    }

    public MetaContainer(@NonNull Type type, @NonNull String name, @NonNull ParentReference data) {
        this(type, name, data, null);
    }

    public MetaContainer(@NonNull Type type, @NonNull String name, @NonNull ParentReference data, @Nullable URL docURL) {
        this.type = type;
        this.name = name;
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.interfaces = new ArrayList<>();
        this.docURL = docURL;
        setData(data);
        this.data.getParentPackage().addChild(this);
    }

    public void addChild(@NonNull MetaContainer childContainer) {
        if (!childContainer.getParentPackage().equals(this)) {
            childContainer.getParentPackage().removePackage(childContainer);
            childContainer.setData(new ParentReference() {
                @Override
                public MetaContainer getParentPackage() {
                    return MetaContainer.this;
                }
            });
        }
        if (!packages.contains(childContainer)) packages.add(childContainer);
    }

    public boolean removePackage(@NonNull MetaContainer childPackage) {
        return packages.remove(childPackage);
    }

    public void setDocURL(URL docURL) {
        this.docURL = docURL;
    }

    public URL getDocURL() {
        return docURL;
    }

    public boolean containsPackage(@NonNull MetaContainer childPackage) {
        return packages.contains(childPackage);
    }

    public List<MetaContainer> getChildPackages() {     // Use Type enum for param, generalize method
        return packages;
    }

    public String getName() {
        return name;
    }

    public MetaContainer getParentPackage() {
        return data.getParentPackage();
    }

    public boolean isRootPackage() {
        return this.data.getParentPackage() == null;
    }

    public Type getType() {
        return this.type;
    }
}
