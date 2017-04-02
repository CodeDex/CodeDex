package com.github.codedex.sourceparser.web.javadoc.fetcher;

import com.github.codedex.sourceparser.web.javadoc.fetcher.model.JDocAllClassesFetcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by IPat on 02.04.2017.
 */

/**
 * This Parser is usable for JDocs which use Java Version >= 7
 *
 * Sample 1:    https://hub.spigotmc.org/javadocs/bukkit/allclasses-frame.html
 */
public class JDocAllClassesV7AndAboveFetcher extends JDocAllClassesFetcher {

    public JDocAllClassesV7AndAboveFetcher(Document jdocDocument) {
        super(jdocDocument);
    }

    protected Elements prepareSharedObject(Document input) {
        final Element ulList = input.select("ul").first();
        if (ulList == null)
            return new Elements(0);
        else
            return ulList.select("li");
    }

    protected void fetch(List<JDocAllClassesEntity> buffer, Elements documentEntities) {
        for (Element documentEntity : documentEntities) {
            final Element documentEntityLinkReference = documentEntity.select("a").first();
            final String relativeJDocURL;
            if (documentEntityLinkReference != null)
                relativeJDocURL = documentEntityLinkReference.attr("href");
            else
                relativeJDocURL = null;

            final boolean isItalic = !documentEntity.select("i").isEmpty();

            final String entityName = documentEntity.text();

            buffer.add(new JDocAllClassesEntity(relativeJDocURL, isItalic, entityName));
        }
    }
}
