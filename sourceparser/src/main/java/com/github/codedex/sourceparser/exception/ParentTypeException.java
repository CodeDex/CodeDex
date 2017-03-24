package com.github.codedex.sourceparser.exception;

import com.github.codedex.sourceparser.entity.model.MetaModel;
import com.github.codedex.sourceparser.entity.model.MetaModel.Type;

import java.util.Set;

/**
 * Created by IPat (Local) on 21.10.2016.
 */

/**
 * @deprecated I decided to ignore parent types as a normal javadoc should have regulated that already.
 */
public class ParentTypeException extends Exception {

    private final MetaModel child;
    private final MetaModel parent;
    public MetaModel getAffectedChild() {
        return this.child;
    }
    public MetaModel getAffectedParent() {
        return this.parent;
    }

    public ParentTypeException(MetaModel affectedChild, MetaModel affectedParent) {
        // super(getErrorMsg(affectedChild, affectedParent.getAcceptedParentTypes()));
        this.child = affectedChild;
        this.parent = affectedParent;
    }

    private static String getErrorMsg(MetaModel affectedChild, Set<Type> acceptedTypes) {
        StringBuilder sb = new StringBuilder("Couldn't assign MetaModel of type ");
        sb.append(affectedChild.getType());
        sb.append(" a new parent:\nChild only accepts parents of the following types:");
        for (Type acceptedType : acceptedTypes) {
            sb.append(" ");
            sb.append(acceptedType.name().toUpperCase());
        }
        return sb.toString();
    }
}
