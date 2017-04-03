package com.github.codedex.sourceparser.web.javadoc.fetcher;

import android.support.v4.util.SimpleArrayMap;

import com.github.codedex.sourceparser.fetcher.ModelFetcher;

import org.jsoup.nodes.Document;

import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public class ModelJDocV7Fetcher extends ModelFetcher<Document> {
    public ModelJDocV7Fetcher(Document jdocDocument) {
        super(jdocDocument);
    }

    protected SimpleArrayMap<String, String> fetchSuperclass(Document jdocDocument) {
        return null;
    }

    protected Set<SimpleArrayMap<String, String>> fetchInterfaces(Document jdocDocument) {
        return null;
    }

    protected Set<SimpleArrayMap<String, String>> fetchEnumConstants(Document jdocDocument) {
        return null;
    }

    protected Set<SimpleArrayMap<String, String>> fetchMethods(Document jdocDocument) {
        return null;
    }

    protected Set<SimpleArrayMap<String, String>> fetchConstructors(Document jdocDocument) {
        return null;
    }

    /**
     * Code cannot be fetched from a Javadoc
     * @param jdocDocument The Javadoc that the code can't be fetched from (bummer!)
     * @return null, since there is no code
     */
    protected String fetchCode(Document jdocDocument) {
        return null;
    }
}
