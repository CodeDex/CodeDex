package com.github.codedex.sourceparser.entity.project;

import com.github.codedex.sourceparser.entity.object.MetaConstructor;
import com.github.codedex.sourceparser.entity.object.MetaMethod;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;
import com.github.codedex.sourceparser.entity.project.model.MetaType;

import java.net.URL;
import java.util.Collections;
import java.util.Set;

import static com.github.codedex.sourceparser.Utils.checkSet;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaClass extends MetaType {

    private final Updater updater;
    public Updater getUpdater() {
        return this.updater;
    }

    public MetaClass(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children,
                        AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                        Set<MetaMethod> methods, String code, MetaType superclass,
                        Set<MetaConstructor> constructors, Set<MetaInterface> implementedInterfaces) {
        this(new Updater(name, jdocURL, parent, children, accessModifier, nonAccessModifiers, methods, code,
                superclass, checkSet(constructors), checkSet(implementedInterfaces)));
    }

    protected MetaClass(Updater updater) {
        super(updater);
        this.updater = updater;
    }

    public Type getType() {
        return Type.CLASS;
    }
    public MetaType getSuperclass() {
        return updater.superclass;
    }
    public Set<MetaConstructor> getConstructors() {
        return updater.constructors;
    }
    public Set<MetaInterface> getImplementedInterfaces() {
        return updater.implementedInterfaces;
    }

    public static class Updater extends MetaType.Updater {

        private MetaType superclass;
        private final Set<MetaConstructor> constructors;
        private final Set<MetaInterface> implementedInterfaces;

        private final Set<MetaConstructor> modConstructors;
        private final Set<MetaInterface> modImplementedInterfaces;

        protected Updater(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children,
                          AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                          Set<MetaMethod> methods, String code, MetaType superclass,
                          Set<MetaConstructor> constructors, Set<MetaInterface> implementedInterfaces) {
            super(name, jdocURL, parent, children, accessModifier, nonAccessModifiers, methods, code);

            this.superclass = superclass;
            this.constructors = Collections.unmodifiableSet(constructors);
            this.implementedInterfaces = Collections.unmodifiableSet(implementedInterfaces);

            this.modConstructors = constructors;
            this.modImplementedInterfaces = implementedInterfaces;
        }

        public void setSuperclass(MetaType superclass) {
            this.superclass = superclass;
        }
        public Set<MetaConstructor> getConstructors() {
            return this.modConstructors;
        }
        public Set<MetaInterface> getImplementedInterfaces() {
            return this.modImplementedInterfaces;
        }
    }
}
