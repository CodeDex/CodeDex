package com.github.codedex.sourceparser.web.javadoc.fetcher.model;

import com.github.codedex.sourceparser.IterableFetcher;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by IPat on 02.04.2017.
 */

public abstract class JDocAllClassesFetcher extends IterableFetcher<Document, JDocAllClassesFetcher.JDocAllClassesEntity, Elements> {

    public JDocAllClassesFetcher(Document jdocDocument) {
        super(jdocDocument);
    }

    public static class JDocAllClassesEntity {
        private final String relativeJavadocURL;
        private final boolean isItalic;
        private final String entityName;
        private final String[] packagePathEntities;

        public JDocAllClassesEntity(String relativeJdocURL, boolean isItalic, String entityName) {
            this.relativeJavadocURL = relativeJdocURL;
            this.isItalic = isItalic;
            this.entityName = entityName;

            if (relativeJdocURL != null && relativeJdocURL.length() >= 1) {
                packagePathEntities = relativeJavadocURL.split("/");
            } else {
                // get from title attribute, if that one is empty make the relativeJdocURL null
            }
        }

        public String getRelativeJavadocURL() {
            return this.relativeJavadocURL;
        }

        public boolean isItalic() {
            return this.isItalic;
        }

        public String getEntityName() {
            return this.entityName;
        }

        public String[] getPackagePathEntities() {
            return this.packagePathEntities;
        }
    }

    // This might NOT be true for the JDocAllClassesLowVersionFetcher. TODO: Check on truthfulness
    protected int size(Elements sharedObject) {
        return sharedObject.size();
    }
}
