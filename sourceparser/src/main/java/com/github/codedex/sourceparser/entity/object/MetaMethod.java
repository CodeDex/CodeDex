package com.github.codedex.sourceparser.entity.object;

import android.support.annotation.NonNull;

import com.github.codedex.sourceparser.entity.Modifiable;
import com.github.codedex.sourceparser.entity.project.specific.MetaThrowable;
import com.github.codedex.sourceparser.entity.project.model.MetaType;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by IPat (Local) on 27.03.2017.
 */

public class MetaMethod implements Modifiable.AccessModifiable {
    private AccessModifier accessModifier = AccessModifier.PACKAGE;
    private Set<NonAccessModifier> nonAccessModifiers = EnumSet.noneOf(NonAccessModifier.class);
    private MetaType returnType;
    private String identifier;
    private List<MetaParameter> parameters = new LinkedList<>();
    private Set<MetaThrowable> exceptions = new HashSet<>();

    public MetaMethod(MetaType returnType, String identifier) {
        this.returnType = returnType;
        this.identifier = identifier;
    }

    public MetaMethod(@NonNull AccessModifier modifier, MetaType returnType, String identifier) {
        this(returnType, identifier);
        this.accessModifier = modifier;
    }

    // setAccessModifier() not required. Changes are not needed after using Constructor (Changing means different method, if you have a different method you should create a new one)
    public AccessModifier getAccessModifier() {
        return this.accessModifier;
    }

    // setNonAccessModifiers() not required (Prevents setting set to null), use Set methods to add / remove modifiers
    public Set<NonAccessModifier> getNonAccessModifiers() {
        return nonAccessModifiers;
    }

    // @see getAccessModifier()
    public MetaType getReturnType() {
        return this.returnType;
    }

    // @see getAccessModifier()
    public String getIdentifier() {
        return this.identifier;
    }

    // @see getNonAccessModifiers()
    public List<MetaParameter> getParameters() {
        return this.parameters;
    }

    // @see getNonAccessModifiers()
    public Set<MetaThrowable> getExceptions() {
        return this.exceptions;
    }
}
