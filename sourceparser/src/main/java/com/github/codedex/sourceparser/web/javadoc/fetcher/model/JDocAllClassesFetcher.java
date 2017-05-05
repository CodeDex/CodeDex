package com.github.codedex.sourceparser.web.javadoc.fetcher.model;

import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.entity.project.specific.MetaRoot;
import com.github.codedex.sourceparser.fetcher.IterableFetcher;

import org.jsoup.nodes.Document;

import java.util.List;

/**
 * @author Patrick "IPat" Hein
 */

public abstract class JDocAllClassesFetcher extends IterableFetcher<Document, MetaType> {

    private final MetaRoot root = new MetaRoot();

    public JDocAllClassesFetcher(Document jdocDocument) {
        super(jdocDocument);
    }

    public MetaRoot getAllClasses() {
        return this.root;
    }

    protected void fetch(List<MetaType> buffer, Document input) {
        getAllClasses(root, buffer, input);
    }

    protected abstract MetaRoot getAllClasses(MetaRoot root, List<MetaType> nestedEntities, Document input);
}
