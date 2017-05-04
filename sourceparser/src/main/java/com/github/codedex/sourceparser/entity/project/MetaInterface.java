package com.github.codedex.sourceparser.entity.project;

import com.github.codedex.sourceparser.entity.object.MetaMethod;
import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;

import java.net.URL;
import java.util.Collections;
import java.util.Set;

import static com.github.codedex.sourceparser.Utils.checkSet;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaInterface extends MetaType {

    private final Updater updater;
    public Updater getUpdater() {
        return this.updater;
    }

    public MetaInterface(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children,
                         AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                         Set<MetaMethod> methods, String code, Set<MetaType> superclasses) {
        this(new Updater(name, jdocURL, parent, children, accessModifier, nonAccessModifiers, methods, code, checkSet(superclasses)));
    }

    protected MetaInterface(Updater updater) {
        super(updater);
        this.updater = updater;
    }

    public Type getType() {
        return Type.INTERFACE;
    }
    public Set<MetaType> getSuperclasses() {
        return updater.superclasses;
    }

    public static class Updater extends MetaType.Updater {

        private final Set<MetaType> superclasses;

        private final Set<MetaType> modSuperclasses;

        protected Updater(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children,
                          AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                          Set<MetaMethod> methods, String code, Set<MetaType> superclasses) {
            super(name, jdocURL, parent, children, accessModifier, nonAccessModifiers, methods, code);

            this.superclasses = Collections.unmodifiableSet(superclasses);

            this.modSuperclasses = superclasses;
        }

        public Set<MetaType> getSuperclasses() {
            return this.modSuperclasses;
        }
    }
}
