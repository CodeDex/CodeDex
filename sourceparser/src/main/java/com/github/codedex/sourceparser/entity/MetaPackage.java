package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains metadata for package.
 */

public class MetaPackage {

    private PackageData data;

    public interface PackageData {
        MetaPackage getParentPackage();
    }

    private String name;
    private List<MetaPackage> packages;
    private List<MetaClass> classes;
    private List<MetaInterface> interfaces;
    private URL docURL;

    public void setData(PackageData data) {
        this.data = data;
    }

    /**
     * Method for getting an instance of a root container for other MetaPackages
     */
    public static @NonNull MetaPackage getNewRootPackage() {
        return new MetaPackage();
    }

    private MetaPackage() {
        this.name = null;
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.interfaces = new ArrayList<>();
        setData(new PackageData() {
                @Override
                public MetaPackage getParentPackage() {
                    return null;
                }
            });
    }

    public MetaPackage(@NonNull String name) {                      // When calling this constructor, make sure you declare the parent package some point later
        this(name, new PackageData() {
            @Override
            public MetaPackage getParentPackage() {
                return null;
            }
        });
    }

    public MetaPackage(@NonNull String name, @Nullable URL docURL) {// When calling this constructor, make sure you declare the parent package some point later
        this(name, new PackageData() {
            @Override
            public MetaPackage getParentPackage() {
                return null;
            }
        }, docURL);
    }

    public MetaPackage(@NonNull String name, @NonNull PackageData data) {
        this(name, data, null);
    }

    public MetaPackage(@NonNull String name, @NonNull PackageData data, @Nullable URL docURL) {
        this.name = name;
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.interfaces = new ArrayList<>();
        this.docURL = docURL;
        setData(data);
        this.data.getParentPackage().addChildPackage(this);
    }

    public void addChildPackage(@NonNull MetaPackage childPackage) {
        if (!childPackage.getParentPackage().equals(this)) {
            childPackage.getParentPackage().removePackage(childPackage);
            childPackage.setData(new PackageData() {
                @Override
                public MetaPackage getParentPackage() {
                    return MetaPackage.this;
                }
            });
        }
        if (!packages.contains(childPackage)) packages.add(childPackage);
    }

    public boolean removePackage(@NonNull MetaPackage childPackage) {
        return packages.remove(childPackage);
    }

    public void addClass(@NonNull MetaClass childClass) {
        classes.add(childClass);
    }

    public void addInterface(@NonNull MetaInterface childInterface) {
        interfaces.add(childInterface);
    }

    public void setDocURL(URL docURL) {
        this.docURL = docURL;
    }

    public URL getDocURL() {
        return docURL;
    }

    public boolean containsPackage(@NonNull MetaPackage childPackage) {
        return packages.contains(childPackage);
    }

    public List<MetaPackage> getChildrenPackages() {
        return packages;
    }

    public String getPackageName() {
        return name;
    }

    public MetaPackage getParentPackage() {
        return data.getParentPackage();
    }

    public boolean isRootPackage() {
        return this.data.getParentPackage() == null;
    }
}
