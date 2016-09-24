package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;

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

    public void setData(PackageData data) {
        this.data = data;
    }

    public MetaPackage(@NonNull String name, PackageData data) {
        this.name = name;
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.interfaces = new ArrayList<>();
        if (data == null) {
            setData(new PackageData() {
                @Override
                public MetaPackage getParentPackage() {
                    return null;
                }
            });
            return;
        }
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

    public MetaPackage(@NonNull String name) {
        this(name, null);
    }

    public String getPackageName() {
        return name;
    }

    public MetaPackage getParentPackage() {
        return data.getParentPackage();
    }
}
