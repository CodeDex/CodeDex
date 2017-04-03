package com.github.codedex.sourceparser.entity.object;

import com.github.codedex.sourceparser.entity.Modifiable;
import com.github.codedex.sourceparser.entity.project.model.MetaType;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 * 
 * MetaParameter represents a parameter used by methods. If there's ever a deeper integration of these models with code, this class will extend MetaVariable (TBC)
 * A parameter can only have the final modifier.
 */
public class MetaParameter implements Modifiable {

    private Set<NonAccessModifier> modifiers = EnumSet.noneOf(NonAccessModifier.class);
    private MetaType type;
    private String identifier;

    public MetaParameter(MetaType type, String identifier) {
        this(false, type, identifier);
    }

    public MetaParameter(boolean isFinal, MetaType type, String identifier) {
        if (isFinal) this.modifiers.add(NonAccessModifier.FINAL);
        this.type = type;
        this.identifier = identifier;
    }

    public Set<NonAccessModifier> getNonAccessModifiers() {
        return this.modifiers;
    }
}
