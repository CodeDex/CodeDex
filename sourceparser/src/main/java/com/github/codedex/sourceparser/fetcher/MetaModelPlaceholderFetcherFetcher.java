package com.github.codedex.sourceparser.fetcher;

import com.github.codedex.sourceparser.entity.project.model.MetaModel;

/**
 * @author Patrick "IPat" Hein
 */

public final class MetaModelPlaceholderFetcherFetcher extends MetaTypeFetcher {

    private final String name;
    private final MetaModel.Type type;

    public MetaModelPlaceholderFetcherFetcher(String name, MetaModel.Type type) {
        if (name == null)
            name = "";
        this.name = name;
        if (type == null)
            type = MetaModel.Type.PLACEHOLDER;
        this.type = type;
    }

    public MetaModelPlaceholderFetcherFetcher() {
        this("", MetaModel.Type.PLACEHOLDER);
    }

    public MetaModel.Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return null;
    }
}
