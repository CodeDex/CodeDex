package com.github.codedex.sourceparser.exception;

import com.github.codedex.sourceparser.entity.MetaContainer;
import com.github.codedex.sourceparser.entity.MetaContainer.Type;

import java.util.List;
import java.util.Set;

/**
 * Created by IPat (Local) on 21.10.2016.
 */

public class ChildTypeException extends ClassCastException {

    private final List<MetaContainer> children;
    public List<MetaContainer> getAffectedChildren() {
        return this.children;
    }

    public ChildTypeException(List<MetaContainer> affectedChildren, Set<Type> acceptedTypes) {
        super(getErrorMsg(affectedChildren, acceptedTypes));
        this.children = affectedChildren;
    }

    private static String getErrorMsg(List<MetaContainer> affectedChildren, Set<Type> acceptedTypes) {
        StringBuilder sb = new StringBuilder("Couldn't add MetaContainers of type ");
        sb.append(affectedChildren.get(0));
        for (int a = 1; a < affectedChildren.size()-1; a++) {
            sb.append(", ");
            sb.append(affectedChildren.get(a).getType().name().toUpperCase());
        }
        sb.append(" and ");
        sb.append(affectedChildren.get(affectedChildren.size()-1).getType().toString());

        sb.append(" to parent:\nParent only accepts children of the following types:");
        for (Type acceptedType : acceptedTypes) {
            sb.append(" ");
            sb.append(acceptedType.name().toUpperCase());
        }

        sb.append(".");
        return sb.toString();
    }
}
