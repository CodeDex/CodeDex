package com.github.codedex.sourceparser.web.javadoc.fetcher;

import android.support.v4.util.SimpleArrayMap;

import com.github.codedex.sourceparser.entity.object.MetaMethod;
import com.github.codedex.sourceparser.entity.project.MetaInterface;
import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.fetcher.MetaModelFetcher;

import org.jsoup.nodes.Document;

import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaModelJDocV7Fetcher extends MetaModelFetcher<Document> {
    public MetaModelJDocV7Fetcher(Document jdocDocument) {
        super(jdocDocument);
    }

    // TODO: Create helper method and temporary storage that reduces duplicate code for the following two methods
    protected MetaType fetchSuperclass(Document jdocDocument) {
        return null;
    }
    protected Set<MetaType> fetchSuperclasses(Document jdocDocument) {
        return null;
    }

    protected Set<MetaInterface> fetchInterfaces(Document jdocDocument) {
        return null;
    }

    protected Set<SimpleArrayMap<String, String>> fetchEnumConstants(Document jdocDocument) {
        return null;
    }

    protected Set<SimpleArrayMap<String, String>> fetchConstructors(Document jdocDocument) {
        return null;
    }

    protected Set<MetaMethod> fetchMethods(Document jdocDocument) {
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
