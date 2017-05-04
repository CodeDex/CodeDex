package com.github.codedex.sourceparser.entity.project.model;

import com.github.codedex.sourceparser.entity.AccessModifiable;
import com.github.codedex.sourceparser.entity.NonAccessModifiable;
import com.github.codedex.sourceparser.entity.object.MetaMethod;

import java.net.URL;
import java.util.Collections;
import java.util.Set;

import static com.github.codedex.sourceparser.Utils.checkSet;

/**
 * @author Patrick "IPat" Hein
 *
 * Word definition:
 *  Child / Parent           - Describes classes path
 *  Superclass / Subclass    - Describes classes inheritance
 *
 * MetaTypes represent the kinds of instances that can be made. Hard to explain, here are use cases:
 *
 * Any class is a MetaType, any interface is as well. Annotations, Enums and Exceptions are compiled as classes, so they're also MetaTypes.
 */
public abstract class MetaType extends MetaModel implements NonAccessModifiable, AccessModifiable {

    private final Updater updater;
    public Updater getUpdater() {
        return this.updater;
    }

    public MetaType(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children,
                       AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                       Set<MetaMethod> methods, String code) {
        this(new Updater(name, jdocURL, parent, checkSet(children),
                accessModifier, checkSet(nonAccessModifiers), checkSet(methods), code));
    }

    protected MetaType(Updater updater) {
        super(updater);
        this.updater = updater;
    }

    public AccessModifier getAccessModifier() {
        return updater.accessModifier;
    }
    public Set<NonAccessModifier> getNonAccessModifiers() {
        return updater.nonAccessModifiers;
    }
    public Set<MetaMethod> getMethods() {
        return updater.methods;
    }
    public String getCode() {
        return updater.code;
    }

    public static class Updater extends MetaModel.Updater {

        private AccessModifier accessModifier;
        private final Set<NonAccessModifier> nonAccessModifiers;
        private final Set<MetaMethod> methods;
        private String code;

        private final Set<NonAccessModifier> modNonAccessModifiers;
        private final Set<MetaMethod> modMethods;

        protected Updater(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children,
                        AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                        Set<MetaMethod> methods, String code) {
            super(name, jdocURL, parent, children);

            this.accessModifier = accessModifier;
            this.nonAccessModifiers = Collections.unmodifiableSet(nonAccessModifiers);
            this.methods = Collections.unmodifiableSet(methods);
            this.code = code;

            this.modNonAccessModifiers = nonAccessModifiers;
            this.modMethods = methods;
        }

        public void setAccessModifier(AccessModifier accessModifier) {
            this.accessModifier = accessModifier;
        }
        public Set<NonAccessModifier> getNonAccessModifiers() {
            return this.modNonAccessModifiers;
        }
        public Set<MetaMethod> getMethods() {
            return this.modMethods;
        }
        public void setCode(String code) {
            this.code = code;
        }
    }

    // TODO: Every MetaTypeFetcher happens to contain Methods in the section "Method Detail".
    // TODO: Move this section to the Fetcher. This could almost be used as a Javadoc for the appropriate class.
    // An enum also has "Enum Constant Detail".
    // There is usually a Method Summary at the top, which shouldn't be used mainly, since the bottom is more descriptive. Though, it also contains information about method inheritance.
    // Classes and enums show their inheritance tree at the top, while interfaces just show direct sub- or superinterfaces.
    // Classes and enums have a "Constructor Detail" section as well. (And also Constructor Summary, but other than Method Summary it doesn't contain any additional information).

    // DECISION: MetaEnums can probably extend from MetaClass, since they're displayed as a class themselves. Do implement custom analyzing behaviour for Enum constants, though!
    // Enums do only differ in their behaviour of extending classes.
    // (vice-versa) However, that is important during compile-time of code, not when you're already creating a Javadoc.
    // Short: not our problem :)

}
