package com.github.codedex.sourceparser.entity.project;

import android.support.annotation.NonNull;

import com.github.codedex.sourceparser.entity.object.MetaConstructor;
import com.github.codedex.sourceparser.fetcher.MetaTypeFetcher;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaClass extends com.github.codedex.sourceparser.entity.project.model.MetaType {

    private Set<MetaConstructor> constructors;
    private com.github.codedex.sourceparser.entity.project.model.MetaType superclass;
    private Set<MetaInterface> implementedInterfaces = new HashSet<>();

    protected MetaClass(@NonNull Type type, MetaTypeFetcher fetcher) {
        super(type, fetcher);
        this.superclass = fetcher.getSuperclass();
    }

    public MetaClass(MetaTypeFetcher fetcher) {
        this(Type.CLASS, fetcher);
    }
}
