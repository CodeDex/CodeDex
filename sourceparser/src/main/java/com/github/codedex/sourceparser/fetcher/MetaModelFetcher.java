package com.github.codedex.sourceparser.fetcher;

import com.github.codedex.sourceparser.entity.project.model.MetaModel.Type;

/**
 * @author Patrick "IPat" Hein
 *
 * The adapter that is the key-interface between MetaModels and custom Parsers
 * TODO: REWORK JDOC DESCRIPTION
 */

public abstract class MetaModelFetcher {
    // TODO: Abstract analyzing from MetaModelJDocV7Fetcher
    /**
     * @see com.github.codedex.sourceparser.web.javadoc.fetcher.MetaModelJDocV7Fetcher
     */

    public abstract String getName();
    public abstract Type getType();
    public abstract String getCode();
}
