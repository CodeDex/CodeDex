package com.github.codedex.sourceparser.web.javadoc.fetcher;

import android.support.v4.util.SimpleArrayMap;

import com.github.codedex.sourceparser.entity.NonAccessModifiable;
import com.github.codedex.sourceparser.entity.object.MetaMethod;
import com.github.codedex.sourceparser.entity.project.MetaInterface;
import com.github.codedex.sourceparser.fetcher.MetaTypeFetcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaModelJDocV7FetcherFetcher extends MetaTypeFetcher {

    protected NonAccessModifiable.AccessModifiable.AccessModifier accessModifier;
    protected Set<NonAccessModifiable.NonAccessModifier> nonAccessModifiers;
    protected Set<com.github.codedex.sourceparser.entity.project.model.MetaType> parents;
    protected Set<MetaInterface> interfaces;

    public MetaModelJDocV7FetcherFetcher(Document jdocDocument) {  // Consider Type as potential parameter
        final Element metaInfoContainer1 = jdocDocument.select("pre").first();
        final
    }

    public Set<com.github.codedex.sourceparser.entity.project.model.MetaType> getSupertypes(Document jdocDocument) {

    }

    public Set<MetaInterface> getInterfaces(Document jdocDocument) {
        return null;
    }

    public Set<SimpleArrayMap<String, String>> getEnumConstants(Document jdocDocument) {
        return null;
    }

    public Set<SimpleArrayMap<String, String>> getConstructors(Document jdocDocument) {
        return null;
    }

    public Set<MetaMethod> getMethods(Document jdocDocument) {
        return null;
    }

    /**
     * Code cannot be fetched from a Javadoc
     *
     * @return null, since there is no code
     */
    public String getCode() {
        return null;
    }
}
