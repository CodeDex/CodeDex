package com.github.codedex.sourceparser.fetcher;

import android.support.v4.util.SimpleArrayMap;

import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public abstract class ModelFetcher<I> {

    private final SimpleArrayMap<String, String> superclass;
    private final Set<SimpleArrayMap<String, String>> interfaces;
    private final Set<SimpleArrayMap<String, String>> enumConstants;   // Enums have an identifier and a description (Not sure if the ordinal is documented too)
    private final Set<SimpleArrayMap<String, String>> methods;
    private final Set<SimpleArrayMap<String, String>> constructors;
    private final String code;

    public ModelFetcher(I input) {
        this.superclass = fetchSuperclass(input);
        this.interfaces = fetchInterfaces(input);
        this.enumConstants = fetchEnumConstants(input);
        this.methods = fetchMethods(input);
        this.constructors = fetchConstructors(input);
        this.code = fetchCode(input);
    }

    protected abstract SimpleArrayMap<String, String> fetchSuperclass(I input);
    protected abstract Set<SimpleArrayMap<String, String>> fetchInterfaces(I input);
    protected abstract Set<SimpleArrayMap<String, String>> fetchEnumConstants(I input);
    protected abstract Set<SimpleArrayMap<String, String>> fetchMethods(I input);
    protected abstract Set<SimpleArrayMap<String, String>> fetchConstructors(I input);
    protected abstract String fetchCode(I input);

    public SimpleArrayMap<String, String> getSuperclass() {
        return this.superclass;
    }
    public Set<SimpleArrayMap<String, String>> getConstructors() {
        return this.constructors;
    }
    public Set<SimpleArrayMap<String, String>> getEnumConstants() {
        return this.enumConstants;
    }
    public Set<SimpleArrayMap<String, String>> getInterfaces() {
        return this.interfaces;
    }
    public Set<SimpleArrayMap<String, String>> getMethods() {
        return this.methods;
    }
    public String getCode() {
        return this.code;
    }
}
