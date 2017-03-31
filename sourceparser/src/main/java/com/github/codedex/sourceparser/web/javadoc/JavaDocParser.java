package com.github.codedex.sourceparser.web.javadoc;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.specific.MetaEnum;
import com.github.codedex.sourceparser.entity.project.MetaInterface;
import com.github.codedex.sourceparser.entity.project.MetaPackage;
import com.github.codedex.sourceparser.entity.project.specific.MetaRoot;
import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaDocParser
 * @author IPat
 *
 * Parses an HTML document to an array of MetaContainers (Abstract class for Classes, Interfaces, etc.).
 */

public class JavaDocParser {

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

    private static String fetchAllClassesFilename(Document overviewDocument) {
        final Elements allClassesFrameReferences = overviewDocument.select("frame[name=packageFrame]");
        return allClassesFrameReferences.first().attr("src");
    }

    private static Document getJsoupDoc(URL path) {
        try {
            return Jsoup.connect(path.toString()).get();
        } catch (IOException e) {
            new IOException("Couldn't fetch document from URL: \"" + path + "\"", e).printStackTrace();
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
        return parse(getJsoupDoc(url), url);
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

    private static MetaRoot parseAllClasses(Document allClassesDocument, URL urlContext) {
        // Variable word definition:
        // Entity = Abstract name for f.e. class, interface or enum. However, also used as an abstract definition for a specific instance of many.
        // Document = HTML document which contains all classes
        final Elements documentEntities = allClassesDocument.select("ul");
        final MetaRoot packageRoot = new MetaRoot();

        SimpleArrayMap<String, String> elementNestQueue = new SimpleArrayMap<>();   // This queue is supposed to get worked on at the end (nested classes waiting for parent definition)
        for (final Element documentEntity : documentEntities) {
            // Extract package path and class URL
            final String entityRelativeJDocURL = documentEntity.select("a").first().attr("href");

            // Parse package path
            final String[] pathEntityNames = entityRelativeJDocURL.split("/");
            if (pathEntityNames.length == 0) return packageRoot;

            // Find package
            MetaPackage packageIterator = packageRoot;
            for (int a = 0; a < pathEntityNames.length - 1; a++) {
                final MetaPackage packageIteratorPointer = packageIterator;     // Temporary storage for reference check later
                for (MetaModel metaModel : packageIterator.getChildren(MetaModel.Type.PACKAGE)) {
                    if (metaModel.getName().equals(pathEntityNames[a])) {
                        packageIterator = (MetaPackage) metaModel;          // Safe cast, getChildren(Type.PACKAGE) called
                        break;
                    }
                }
                if (packageIteratorPointer != packageIterator) {                // This means the for loop didn't find the searched package
                    packageIterator = new MetaPackage(pathEntityNames[a], packageIteratorPointer);
                }
            }

            // Create JavadocURL
            URL entityJDocURL = null;
            if (urlContext != null)
                try { entityJDocURL = new URL(urlContext, entityRelativeJDocURL);
                } catch (MalformedURLException ignored) { entityJDocURL = null; }

            // Create entity instance
            final String entityName = documentEntity.text();
            final String[] entityNestedPathEntityNames = entityName.split("\\.");

            MetaModel modelIterator = packageIterator;
            boolean waitForQueue = false;
            final String nestedEntityNameWithoutPath = entityNestedPathEntityNames[entityNestedPathEntityNames.length - 1];
            for (int a = 0; a < entityNestedPathEntityNames.length - 1; a++) {
                final MetaModel modelIteratorPointer = modelIterator;     // Temporary storage for reference check later
                for (MetaModel metaModel : modelIterator.getChildren(MetaModel.Type.CLASS, MetaModel.Type.INTERFACE)) {
                    if (metaModel.getName().equals(entityNestedPathEntityNames[a])) {
                        modelIterator = metaModel;
                        break;
                    }
                }
                if (modelIteratorPointer != modelIterator) {                // This means the for loop didn't find the searched package
                    final int startNestedNameIndex = entityName.length() - nestedEntityNameWithoutPath.length();
                    final String nestedPathNameWithoutEntity = entityName.substring(0, startNestedNameIndex - 1);   // -1, because we want to exclude the dot in the path
                    elementNestQueue.put(nestedPathNameWithoutEntity, nestedEntityNameWithoutPath);
                    waitForQueue = true;
                    break;
                }
            }

            if (waitForQueue) continue;

            final boolean isClass = documentEntity.select("i").isEmpty();
            if (isClass)
                modelIterator = new MetaClass(nestedEntityNameWithoutPath, modelIterator);
            else
                modelIterator = new MetaInterface(nestedEntityNameWithoutPath, modelIterator);
        }

        while (!elementNestQueue.isEmpty()) {
            // Repeat the process that happened in the for-loop above, check for changes each iteration,
            // if there were none, create all the elements from the queue with the dot in their name OR create the class needed yourself (Maybe use a MetaPlaceholder?)
        }

        return packageRoot;
    }

    private String[] getPathParts(String path) {
        return path.split("\\.");
    }
}
