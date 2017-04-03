package com.github.codedex.sourceparser.entity.project.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;

import com.github.codedex.sourceparser.entity.Modifiable;
import com.github.codedex.sourceparser.entity.object.MetaMethod;
import com.github.codedex.sourceparser.fetcher.ModelFetcher;

import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 *
 * Word definition:
 *  Child / Parent           - Describes classes path
 *  Superclass / Subclass    - Describes classes inheritance
 *
 * MetaTypes represent the kinds of instances that can be made. Hard to explain, here's an example:
 *
 * Any class is a MetaType, any interface is as well. Annotations, Enums and Exceptions are compiled as classes, so they're also MetaTypes.
 */
public abstract class MetaType extends MetaModel implements Modifiable.AccessModifiable {

    private String code;
    private Set<MetaMethod> methods;
    private MetaType superclass;

    protected MetaType(@NonNull Type type, @NonNull String name, @Nullable MetaModel parent, MetaType superclass, @Nullable String code) {
        super(type, name, parent);
        this.superclass = superclass;
        this.code = code;
    }

    public void setCode(@Nullable String code) {
        this.code = code;
    }
    @Nullable public String getCode() {
        return this.code;
    }

    public MetaType getSuperclass() {
        return this.superclass;
    }

    public <T> void buildFromFetcher(ModelFetcher<T> fetcher) {
        final String code = fetcher.getCode();
        Set<SimpleArrayMap<String, String>> methods = fetcher.getMethods();
        final SimpleArrayMap<String, String> superclass = fetcher.getSuperclass();

        this.code = (code == null ? "" : code);

        if (methods != null)
            for (SimpleArrayMap<String, String> methodInfoMap : methods) {
                // TODO: Analyze methodInfoMap for information about a single new method
            }

        if (superclass == null)
            this.superclass = getDefaultSuperclass();
        else {
            // TODO: Analyze superclassInfoMap for information about the super class
        }
    }

    protected abstract MetaType getDefaultSuperclass();

    // TODO: Every MetaType happens to contain Methods in the section "Method Detail".
    // An enum also has "Enum Constant Detail".
    // There is usually a Method Summary at the top, which shouldn't be used mainly, since the bottom is more descriptive. Though, it also contains information about method inheritance.
    // Classes and enums show their inheritance tree at the top, while interfaces just show direct sub- or superinterfaces.
    // Classes and enums have a "Constructor Detail" section as well. (And also Constructor Summary, but other than Method Summary it doesn't contain any additional information).

    // DECISION: MetaEnums can probably extend from MetaClass, since they're displayed as a class themselves. Do implement custom analyzing behaviour for Enum constants, though!
    // Enums do only differ in their behaviour of extending classes.
    // (vice-versa) However, that is important during compile-time of code, not when you're already creating a Javadoc.
    // Short: not our problem :)

}
