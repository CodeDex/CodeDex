package com.github.codedex.sourceparser.web.javadoc.fetcher;

import android.support.v4.util.SimpleArrayMap;

import com.github.codedex.sourceparser.entity.Modifiable;
import com.github.codedex.sourceparser.entity.object.MetaMethod;
import com.github.codedex.sourceparser.entity.project.MetaInterface;
import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.fetcher.MetaModelFetcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public class MetaModelJDocV7Fetcher extends MetaModelFetcher<Document> {

    protected Modifiable.AccessModifiable.AccessModifier accessModifier;
    protected Set<Modifiable.NonAccessModifier> nonAccessModifiers;
    protected Set<MetaType> parents;
    protected Set<MetaInterface> interfaces;

    public MetaModelJDocV7Fetcher(Document jdocDocument) {  // Consider Type as potential parameter
        final Element metaInfoContainer1 = jdocDocument.select("pre").first();
        final
    }

    public Set<MetaType> getSupertypes(Document jdocDocument) {

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
