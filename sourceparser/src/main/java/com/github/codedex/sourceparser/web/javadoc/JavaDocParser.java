package com.github.codedex.sourceparser.web.javadoc;

import com.github.codedex.sourceparser.entity.project.MetaPackage;
import com.github.codedex.sourceparser.entity.project.specific.MetaRoot;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;
import com.github.codedex.sourceparser.web.javadoc.fetcher.model.JDocAllClassesFetcher;
import com.github.codedex.sourceparser.web.javadoc.fetcher.project.JDocAllClassesV7UpFetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * JavaDocParser
 * @author Patrick "IPat" Hein
 *
 * Parses an HTML document to a container object of MetaModels (Abstract class for Classes, Interfaces, etc.).
 */

public class JavaDocParser {

    private JavaDocParser() { /* For now, this is only a static tool */ }

    /**
     * Confirms whether a document can be parsed.
     *
     * @param document that needs to be parsed
     * @return confirmation whether it can be parsed
     */
    public static boolean validate(Document document) {
        return validateAllClasses(document) || validateOverview(document);
    }

    private static boolean validateAllClasses(Document document) {
        final Elements indexContainerReferences = document.select("div[class=indexContainer]");
        final Elements h1References = document.select("h1[class=bar]");
        final Elements ulReferences = document.select("ul");
        return indexContainerReferences.size() == 1 &&
                h1References.size() == 1 &&
                // h1References.first().text().equals("All Classes") &&         // Might change per language, can't take as validation
                ulReferences.size() == 1;
    }

    private static boolean validateOverview(Document document) {
        final Elements allClassesFrameReferences = document.select("frame[name=packageFrame]");
        return allClassesFrameReferences.size() == 1 && allClassesFrameReferences.first().hasAttr("src") &&
                document.select("frame[name=packageListFrame]").size() == 1 && document.select("frame[name=classFrame]").size() == 1;
    }

    // TODO: Create static validating methods in each Fetcher class instead of here

    private static String fetchAllClassesFilename(Document overviewDocument) {
        final Elements allClassesFrameReferences = overviewDocument.select("frame[name=packageFrame]");
        return allClassesFrameReferences.first().attr("src");
    }

    private static Document getJsoupDoc(URL path) {
        try {
            return Jsoup.connect(path.toString()).get();
        } catch (IOException e) {
            new IOException("Couldn't getAllClasses document from URL: \"" + path + "\"", e).printStackTrace();
        }
        return null;
    }

    public static MetaRoot parse(String html) {
        final Document buffer = Jsoup.parse(html);
        if (validateAllClasses(buffer)) return parseAllClasses(buffer, null);
        else return null;
    }

    public static MetaRoot parse(Document document) {
        try {
            return parse(document, new URI(document.baseUri()).toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MetaRoot parse(URL url) {
        final Document doc = getJsoupDoc(url);
        return parse(doc, url);
    }

    private static MetaRoot parse(Document document, URL url) {
        if (validateAllClasses(document)) return parseAllClasses(document, url);

        // Look through all parent folders, check for Overview in each folder
        StringBuilder path = new StringBuilder(url.getPath());
        final String protocol = url.getProtocol();
        final String host = url.getHost();

        path.append("/");   // Hotfix: Do-While loop needs to execute original path at least once

        boolean isFolder = false;
        do {
            // Delete last part of URL
            path.delete(path.toString().lastIndexOf("/"), path.length());

            // Create temporary URL replacement with renewed file path
            final URL bufferURL;
            try {
                bufferURL = new URL(protocol, host, path.toString() + (isFolder ? "/" : "")); // Don't append isFolder?"/":"" to path, as it'll infinitely re-delete the last "/" then.
            } catch (MalformedURLException e) {
                e.printStackTrace();
                continue;
            }

            document = getJsoupDoc(bufferURL);

            if (validateOverview(document)) {
                try {
                    document = getJsoupDoc(new URL(bufferURL, fetchAllClassesFilename(document)));
                    if (validateAllClasses(document)) return parseAllClasses(document, url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            isFolder = true;
        } while (path.toString().contains("/"));

        return null;
    }

    private static class ClassEntityInfoContainer {
        private final MetaPackage metaPackage;
        private final String[] metaPath;
        private final URL jdocURL;
        private boolean isItalic;

        ClassEntityInfoContainer(MetaPackage metaPackage, String[] metaPath, URL jdocURL, boolean isItalic) {
            this.metaPackage = metaPackage;
            this.metaPath = metaPath;
            this.jdocURL = jdocURL;
            this.isItalic = isItalic;
        }

        MetaPackage getMetaPackage() {
            return metaPackage;
        }

        String[] getMetaPath() {
            return metaPath;
        }

        URL getJDocURL() {
            return jdocURL;
        }

        MetaModel.Type getType() {
            if (isItalic)
                return MetaModel.Type.INTERFACE;
            else
                return MetaModel.Type.CLASS;
        }
    }

    /**
     * Parses the allClassesDocument to a MetaModel structure
     * @param allClassesDocument the allClassesDocument represents the Javadoc HTML document that contains all classes.
     * @param urlContext the website the Javadoc runs on
     * @return A newly created root package containing the MetaModel structure
     */
    private static MetaRoot parseAllClasses(Document allClassesDocument, URL urlContext) {
        // TODO: Very old Javadocs don't have ul to list their classes, but uses a table and <br>s: http://static.javadoc.io/org.springframework/spring/2.0.5/allclasses-frame.html
        // Half solved, the adapter (JDocAllClassesFetcher) is ready, but the JDocAllClassV6AndBelowFetcher still needs to be created

        // This is the fetcher that has to change depending on the Javadocs version, implement a strategy pick method
        final JDocAllClassesFetcher fetcher = new JDocAllClassesV7UpFetcher(allClassesDocument, urlContext);
        return fetcher.getAllClasses();
    }
}
