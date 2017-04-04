package com.github.codedex.sourceparser.fetcher;

import android.support.v4.util.SimpleArrayMap;

import com.github.codedex.sourceparser.entity.object.MetaMethod;
import com.github.codedex.sourceparser.entity.project.MetaInterface;
import com.github.codedex.sourceparser.entity.project.model.MetaType;

import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 *
 * The adapter that is the key-interface between MetaModels and custom Parsers
 */

public abstract class MetaModelFetcher<I> {

    private final MetaType superclass;          // Used for classes
    private final Set<MetaType> superclasses;   // Used for interfaces
    private final Set<MetaInterface> interfaces;   // Used for classes
    private final Set<SimpleArrayMap<String, String>> enumConstants;   // Enums have an identifier and a description (Not sure if the ordinal is documented too)
    private final Set<MetaMethod> methods;
    private final Set<SimpleArrayMap<String, String>> constructors;
    private final String code;

    public MetaModelFetcher(I input) {
        this.superclass = fetchSuperclass(input);
        this.superclasses = fetchSuperclasses(input);
        this.interfaces = fetchInterfaces(input);
        this.enumConstants = fetchEnumConstants(input);
        this.methods = fetchMethods(input);
        this.constructors = fetchConstructors(input);
        this.code = fetchCode(input);
    }

    protected abstract MetaType fetchSuperclass(I input);
    protected abstract Set<MetaType> fetchSuperclasses(I input);
    protected abstract Set<MetaInterface> fetchInterfaces(I input);
    protected abstract Set<SimpleArrayMap<String, String>> fetchEnumConstants(I input);
    protected abstract Set<MetaMethod> fetchMethods(I input);
    protected abstract Set<SimpleArrayMap<String, String>> fetchConstructors(I input);
    protected abstract String fetchCode(I input);

    public MetaType getSuperclass() {
        return this.superclass;
    }
    public Set<MetaType> getSuperclasses() {
        return this.superclasses;
    }
    public Set<SimpleArrayMap<String, String>> getConstructors() {
        return this.constructors;
    }
    public Set<SimpleArrayMap<String, String>> getEnumConstants() {
        return this.enumConstants;
    }
    public Set<MetaInterface> getInterfaces() {
        return this.interfaces;
    }
    public Set<MetaMethod> getMethods() {
        return this.methods;
    }
    public String getCode() {
        return this.code;
    }
}
