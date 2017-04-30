package com.github.codedex.sourceparser.entity.project.model;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.fetcher.MetaTypeFetcher;

/**
 * @author Patrick "IPat" Hein
 *
 * Major interface for creating MetaModels.
 * @deprecated Possibly deprecated. Marking it prematurely, so it doesn't get used.
 */

public final class MetaModelBuilder {

    private MetaTypeFetcher fetcher;

    public MetaModelBuilder(MetaTypeFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public MetaModel build() {
        switch(fetcher.getType()) {
            case CLASS:
                return buildClass();
        }
        // stub
        return null;
    }

    public MetaClass buildClass() {
        // stub
        return null;
    }
}
