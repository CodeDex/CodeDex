package com.github.codedex.sourceparser.fetcher;

import com.github.codedex.sourceparser.entity.project.model.MetaModel;

/**
 * @author Patrick "IPat" Hein
 */

public final class MetaModelPlaceholderFetcher extends MetaModelFetcher {

    private final String name;
    private final MetaModel.Type type;

    public MetaModelPlaceholderFetcher(String name, MetaModel.Type type) {
        if (name == null)
            name = "";
        this.name = name;
        if (type == null)
            type = MetaModel.Type.PLACEHOLDER;
        this.type = type;
    }

    public MetaModelPlaceholderFetcher() {
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
