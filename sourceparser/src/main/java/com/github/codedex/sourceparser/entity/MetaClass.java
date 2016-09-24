package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains metadata about class
 */

public class MetaClass {

    public interface ClassData {
        String getName();
        MetaInterface[] getInterfaces();
        MetaMethod[] getMethods();
    }

    private String name;
    private List<MetaInterface> interfaces;
    private List<MetaMethod> methods;

    public MetaClass(@NonNull ClassData data) {
        update(data);
    }

    public void update(@NonNull ClassData data) {
        this.name = data.getName();
        this.interfaces = new ArrayList<>(Arrays.asList(data.getInterfaces()));
        this.methods = new ArrayList<>(Arrays.asList(data.getMethods()));
    }


}
