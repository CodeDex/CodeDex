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
 * Immutable dependencies that need to be made immutable yet: TODO
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

    // TODO: Remove InfoContainer and MetaMutable - DONT create a replace(fetcher) method. fetcher functionalities shouldnt be implemented here,
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

    private InfoContainer info;

    public MetaMethod(AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                      MetaType returnType, String identifier,
                      List<MetaParameter> parameters, Set<MetaThrowable> exceptions) {

        this.info = new InfoContainer(accessModifier, nonAccessModifiers, returnType, identifier, parameters, exceptions);
    }

    public AccessModifier getAccessModifier() {
        return info.accessModifier;
    }
    public Set<NonAccessModifier> getNonAccessModifiers() {
        return info.nonAccessModifiers;
    }
    public MetaType getReturnType() {
        return info.returnType;
    }
    public String getIdentifier() {
        return info.identifier;
    }
    public List<MetaParameter> getParameters() {
        return info.parameters;
    }
    public Set<MetaThrowable> getExceptions() {
        return info.exceptions;
    }

    public InfoContainer getContainer() {

    }

    private static class InfoContainer implements MetaInfo {

        private final AccessModifier accessModifier;
        private final Set<NonAccessModifier> nonAccessModifiers;
        private final MetaType returnType;
        private final String identifier;
        private final List<MetaParameter> parameters;
        private final Set<MetaThrowable> exceptions;

        private InfoContainer(AccessModifier accessModifier, Set<NonAccessModifier> nonAccessModifiers,
                             MetaType returnType, String identifier,
                             List<MetaParameter> parameters, Set<MetaThrowable> exceptions) {
            this.nonAccessModifiers = Collections.unmodifiableSet(nonAccessModifiers);
            this.parameters = Collections.unmodifiableList(parameters);
            this.exceptions = Collections.unmodifiableSet(exceptions);
            this.accessModifier = accessModifier;
            this.returnType = returnType;
            this.identifier = identifier;
        }

        public AccessModifier getAccessModifier() {
            return this.accessModifier;
        }
        public Set<NonAccessModifier> getNonAccessModifiers() {
            return this.nonAccessModifiers;
        }
        public MetaType getReturnType() {
            return this.returnType;
        }
        public String getIdentifier() {
            return this.identifier;
        }
        public List<MetaParameter> getParameters() {
            return this.parameters;
        }
        public Set<MetaThrowable> getExceptions() {
            return this.exceptions;
        }

        @Override
        public void replace() {

        }
    }
}
