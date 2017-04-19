package com.github.codedex.sourceparser.entity.project.model;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.fetcher.MetaModelFetcher;

/**
 * @author Patrick "IPat" Hein
 *
 * Major interface for creating MetaModels.
 */

public final class MetaModelBuilder {

    private MetaModelFetcher fetcher;

    public MetaModelBuilder(MetaModelFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public MetaModel build() {
        switch(fetcher.getType()) {
            case CLASS:
                return buildClass();
        }
    }

    public MetaClass buildClass() {

    }
}
