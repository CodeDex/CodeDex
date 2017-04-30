package com.github.codedex.sourceparser.entity.object;

import com.github.codedex.sourceparser.entity.AccessModifiable;
import com.github.codedex.sourceparser.entity.MetaMutable;
import com.github.codedex.sourceparser.entity.NonAccessModifiable;
import com.github.codedex.sourceparser.entity.project.specific.MetaThrowable;
import com.github.codedex.sourceparser.entity.project.model.MetaType;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 *
 * Modifiable attribute container
 *
 * Dependencies that need to be made MetaMutable yet: TODO
 * @see MetaType
 * @see MetaParameter
 * @see MetaThrowable
 */

public class MetaMethod implements MetaMutable, NonAccessModifiable, AccessModifiable {

    // TODO: The whole fucking thing. Please rethink the structure properly. A for effort though.

    // Honestly though. The current structure needs to be redone all the time and it gets annoying.
    // It needs a remake. It needs to be redone.

    // It needs the fetcher - or should be accessible to others to be modifiable

    // Problem is potentially solvable by having backup references which refer to this object and
    // replace all method return values to the new object created by a replace() method

    // TODO: Remove Updater and MetaMutable - DONT create a replace(fetcher) method. fetcher functionalities shouldnt be implemented here,
    // The fetcher is supposed to manage new objects and connections, not the object itself. the old object needs to provide connections
    // once deactivated. either the fetcher then notifies the connected objects of the new object (really preferred, but how?)
    // or the old objects methods simply return the new objects values (unpreferred)
    // - attributes final
    // - replacable
    // - refreshable

    // You can't have final attributes and expect a refreshable object. This doesn't work.
    // Decide for ONE option and design your MetaModel appropriately to it.

    // Note: They have to be refreshable, but they don't need to be final. The choice is obvious.

    // Think of how to properly design the object without it being too complicated:
    // Setters for each attribute. Reminder: DONT create a set(fetcher) method. the fetcher has a "MetaModel createModel(?)" method.

    protected final Updater updater;

    public MetaMethod(AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                      MetaType returnType, String identifier,
                      List<MetaParameter> parameters, Set<MetaThrowable> exceptions) {

        this.updater = new Updater(accessModifier, nonAccessModifiers, returnType, identifier, parameters, exceptions);
    }

    public AccessModifier getAccessModifier() {
        return updater.accessModifier;
    }
    public Set<NonAccessModifier> getNonAccessModifiers() {
        return updater.nonAccessModifiers;
    }
    public MetaType getReturnType() {
        return updater.returnType;
    }
    public String getIdentifier() {
        return updater.identifier;
    }
    public List<MetaParameter> getParameters() {
        return updater.parameters;
    }
    public Set<MetaThrowable> getExceptions() {
        return updater.exceptions;
    }

    public Updater getUpdater() {
        return this.updater;
    }

    /**
     * @see com.github.codedex.sourceparser.entity.MetaMutable.MetaUpdater
     * The Updater is implemented as an information reference and interface to it at the same time.
     */
    public class Updater implements MetaUpdater {

        private final Set<NonAccessModifier> nonAccessModifiers;
        private final List<MetaParameter> parameters;
        private final Set<MetaThrowable> exceptions;
        private AccessModifier accessModifier;
        private MetaType returnType;
        private String identifier;

        // There is no reason for any user of the interface to change the Sets or Lists reference pointers.
        private final Set<NonAccessModifier> modNonAccessModifiers;
        private final List<MetaParameter> modParameters;
        private final Set<MetaThrowable> modExceptions;

        protected Updater(AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                        MetaType returnType, String identifier,
                        List<MetaParameter> parameters, Set<MetaThrowable> exceptions) {
            this.nonAccessModifiers = Collections.unmodifiableSet(nonAccessModifiers);
            this.parameters = Collections.unmodifiableList(parameters);
            this.exceptions = Collections.unmodifiableSet(exceptions);
            this.accessModifier = accessModifier;
            this.returnType = returnType;
            this.identifier = identifier;

            this.modNonAccessModifiers = nonAccessModifiers;
            this.modParameters = parameters;
            this.modExceptions = exceptions;
        }

        public Set<NonAccessModifier> getNonAccessModifiers() {
            return this.modNonAccessModifiers;
        }
        public List<MetaParameter> getParameters() {
            return this.modParameters;
        }
        public Set<MetaThrowable> getExceptions() {
            return this.modExceptions;
        }
        public void setAccessModifier(AccessModifier modifier) {
            this.accessModifier = modifier;
        }
        public void setReturnType(MetaType type) {
            this.returnType = type;
        }
        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
    }
}
