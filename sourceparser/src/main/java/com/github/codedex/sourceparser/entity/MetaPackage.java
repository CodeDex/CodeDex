package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by IPat (Local) on 24.09.2016.
 */

public class MetaPackage {

    private PackageData data;

    public interface PackageData {
        Package getParentPackage();
    }

    private String name;
    private List<Package> packages;

    public MetaPackage(@NonNull String name, PackageData data) {
        this.name = name;
        this.data = data;
    }

    public MetaPackage(@NonNull String name) {
        this(name, null);
    }

    public String getPackageName() {
        return name;
    }

    public Package getParentPackage() {
        return data.getParentPackage();
    }
}
