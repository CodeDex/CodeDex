package com.github.codedex.sourceparser.entity.project.specific;

import com.github.codedex.sourceparser.entity.object.MetaConstructor;
import com.github.codedex.sourceparser.entity.object.MetaMethod;
import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.MetaInterface;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;
import com.github.codedex.sourceparser.entity.project.model.MetaType;

import java.net.URL;
import java.util.Set;

import static com.github.codedex.sourceparser.Utils.checkSet;

/**
 * @author Patrick "IPat" Hein
 */

public final class MetaEnum extends MetaClass {

    private final Updater updater;
    public Updater getUpdater() {
        return this.updater;
    }

    public MetaEnum(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children,
                     AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                     Set<MetaMethod> methods, String code, MetaType superclass,
                     Set<MetaConstructor> constructors, Set<MetaInterface> implementedInterfaces) {
        this(new Updater(name, jdocURL, parent, children, accessModifier, nonAccessModifiers, methods, code,
                superclass, checkSet(constructors), checkSet(implementedInterfaces)));
    }

    protected MetaEnum(Updater updater) {
        super(updater);
        this.updater = updater;
    }

    public static class Updater extends MetaClass.Updater {
        protected Updater(String name, URL jdocURL, MetaModel parent, Set<MetaModel> children,
                          AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                          Set<MetaMethod> methods, String code, MetaType superclass,
                          Set<MetaConstructor> constructors, Set<MetaInterface> implementedInterfaces) {
            super(name, jdocURL, parent, children, accessModifier, nonAccessModifiers, methods, code, superclass, constructors, implementedInterfaces);
        }
    }
}
