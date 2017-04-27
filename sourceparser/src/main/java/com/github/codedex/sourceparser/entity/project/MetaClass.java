package com.github.codedex.sourceparser.entity.project;

import android.support.annotation.NonNull;

import com.github.codedex.sourceparser.entity.object.MetaConstructor;
import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.fetcher.MetaModelFetcher;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaClass extends MetaType {

    private Set<MetaConstructor> constructors;
    private MetaType superclass;
    private Set<MetaInterface> implementedInterfaces = new HashSet<>(0);

    protected MetaClass(@NonNull Type type, MetaModelFetcher fetcher) {
        super(type, fetcher);
        this.superclass = fetcher.getSuperclass();
    }

    public MetaClass(MetaModelFetcher fetcher) {
        this(Type.CLASS, fetcher);
    }
}
