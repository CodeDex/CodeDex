package com.github.codedex.sourceparser.web.javadoc.fetcher.model;

import com.github.codedex.sourceparser.fetcher.IterableFetcher;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author Patrick "IPat" Hein
 */

public abstract class JDocAllClassesFetcher extends IterableFetcher<Document, JDocAllClassesFetcher.JDocAllClassesEntity, Elements> {

    public JDocAllClassesFetcher(Document jdocDocument) {
        super(jdocDocument);
    }

    public static class JDocAllClassesEntity {
        private final String relativeJDocURL;
        private final boolean isItalic;
        private final String entityName;
        private final String[] packagePathEntities;

        public JDocAllClassesEntity(String relativeJDocURL, boolean isItalic, String entityName, String titleAttr) {
            this.relativeJDocURL = relativeJDocURL;
            this.isItalic = isItalic;
            this.entityName = entityName;

            if (relativeJDocURL != null && relativeJDocURL.length() >= 1) {
                this.packagePathEntities = relativeJDocURL.split("/");
            } else {
                if (titleAttr == null)
                    titleAttr = "";
                final int startOfPackage = titleAttr.lastIndexOf(" ") + 1;  // English-based
                if (startOfPackage == 0) {
                    if (titleAttr.length() > 0) {
                        this.packagePathEntities = titleAttr.split("\\.");
                    } else
                        this.packagePathEntities = null;    // Really trying to avoid this. But honestly, if this is reached, did the user even use the Validator?
                }
                else {
                    final String packagePath = titleAttr.substring(startOfPackage, titleAttr.length());
                    this.packagePathEntities = packagePath.split("\\.");
                }
            }
        }

        public String getRelativeJDocURL() {
            return this.relativeJDocURL;
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
