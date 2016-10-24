package com.github.codedex.sourceparser.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.exception.ChildTypeException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class for container for any metadata, f.e. source code in the example of
 * MetaClass or other MetaContainers in MetaPackage.
 */

public abstract class MetaContainer {

    public MetaContainer(@NonNull String name, @NonNull ParentReferrer parentRef, Type... acceptedChildTypes) {
        this.name = name;

        this.parentRef = parentRef;

        this.acceptedChildTypes = new HashSet<>();
        Collections.addAll(this.acceptedChildTypes, acceptedChildTypes);

        this.children = new ArrayList<>();
    }

    private final String name;
    public @NonNull String getName() {
        return this.name;
    }

    private URL docURL;
    public void setDocURL(URL docURL) {
        this.docURL = docURL;
    }
    public @Nullable URL getDocURL() {
        return this.docURL;
    }

    private ParentReferrer parentRef;
    public MetaContainer getParent() {
        return parentRef.getParent();
    }
    public interface ParentReferrer {
        @NonNull MetaContainer getParent(); // This is gonna be a package most in the cases, but sometimes classes are nested in f.e. classes... maybe worth researching, !4!
    }

    private Set<Type> acceptedChildTypes;
    public Set<Type> getAcceptedChildTypes() {
        return acceptedChildTypes;
    }
    private List<MetaContainer> children;
    public void addChild(MetaContainer... children) {
        List<MetaContainer> errorChildStack = new ArrayList<>();
        for (MetaContainer child : children)
            if (acceptedChildTypes.contains(child.getType()))
                this.children.add(child);
            else
                errorChildStack.add(child);
        if (errorChildStack.size() > 0)
            throw new ChildTypeException(errorChildStack, getAcceptedChildTypes());
    }
    public List<MetaContainer> getChildren(Type... types) {
        List<MetaContainer> result = new ArrayList<>();
        for (Type type : types)
            for (MetaContainer child : this.children)
                if (type.equals(child.getType()))
                    result.add(child);
        return result;
    }

    public abstract Type getType();
    public enum Type {
        PACKAGE,
        CLASS,
        INTERFACE,
        EXCEPTION
    } // !2! Add annotation as type?
}
