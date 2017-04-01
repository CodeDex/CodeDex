package com.github.codedex.sourceparser.web.javadoc;

import com.github.codedex.sourceparser.entity.project.MetaPackage;
import com.github.codedex.sourceparser.entity.project.specific.MetaRoot;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private static class ClassEntityInfoContainer {
        private final MetaPackage metaPackage;
        private final String[] metaPath;
        private final URL jdocURL;
        private boolean isClass;

        ClassEntityInfoContainer(MetaPackage metaPackage, String[] metaPath, URL jdocURL, boolean isClass) {
            this.metaPackage = metaPackage;
            this.metaPath = metaPath;
            this.jdocURL = jdocURL;
            this.isClass = isClass;
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
            if (isClass)
                return MetaModel.Type.CLASS;
            else
                return MetaModel.Type.INTERFACE;
        }
    }

    private static MetaRoot parseAllClasses(Document allClassesDocument, URL urlContext) {

            // Variable word definition:
            // Entity = Abstract name for f.e. class, interface or enum. However, also used as an abstract definition for a specific instance of many.
            // Document = HTML document which contains all classes

        final Elements documentEntities = allClassesDocument.select("ul");
        final MetaRoot packageRoot = new MetaRoot();

        Set<ClassEntityInfoContainer> entityNestQueue = new HashSet<>();                                   // This queue is supposed to get worked on at the end
            // This for loop extracts all the information from the HTML document, puts them into a queue and also creates / finds the needed package for each class / interface
        for (final Element documentEntity : documentEntities) {
            // Extract package path and class JDocRelativeURL
            final String entityRelativeJDocURL = documentEntity.select("a").first().attr("href");

            // Parse package path
            final String[] pathEntityNames = entityRelativeJDocURL.split("/");
            if (pathEntityNames.length == 0) return packageRoot;

            // Find package
            MetaPackage packageIterator = packageRoot;
            for (int a = 0; a < pathEntityNames.length - 1; a++) {
                final MetaPackage packageIteratorPointer = packageIterator;                         // Temporary storage for reference check later
                for (MetaModel metaModel : packageIterator.getChildren(MetaModel.Type.PACKAGE)) {
                    if (metaModel.getName().equals(pathEntityNames[a])) {
                        packageIterator = (MetaPackage) metaModel;                                  // Safe cast, getChildren(Type.PACKAGE) called
                        break;
                    }
                }
                if (packageIteratorPointer != packageIterator) {                                    // This means the for loop didn't find the searched package
                    packageIterator = new MetaPackage(pathEntityNames[a], packageIteratorPointer);
                }
            }

            // Create JDocAbsoluteURL
            URL entityJDocURL = null;
            if (urlContext != null)
                try { entityJDocURL = new URL(urlContext, entityRelativeJDocURL);
                } catch (MalformedURLException ignored) { entityJDocURL = null; }

            // Create entity info container
            final String entityName = documentEntity.text();

            final String[] entityNestedPathEntityNames = entityName.split("\\.");
            final boolean isClass = documentEntity.select("i").isEmpty();       // This is quite an assumption. !1! Check on a few Javadoc versions for truthfulness of this.

            // Add info container to queue
            entityNestQueue.add(new ClassEntityInfoContainer(packageIterator, entityNestedPathEntityNames, entityJDocURL, isClass));
        }

        // Work with the queue
        while (!entityNestQueue.isEmpty()) {            // This might have to be worked through multiple times, as nested entities might have to wait for their parents
            boolean nestQueueChanges = false;
            final Iterator<ClassEntityInfoContainer> elementNestQueueIterator = entityNestQueue.iterator();
            while (elementNestQueueIterator.hasNext()) {
                final ClassEntityInfoContainer infoContainer = elementNestQueueIterator.next();
                MetaModel modelIterator = infoContainer.getMetaPackage();
                final String[] nestedPathEntityNames = infoContainer.getMetaPath();
                final int nestedPathEntityCount = nestedPathEntityNames.length;
                boolean parentMissingLock = false;
                for (int a = 0; a < nestedPathEntityCount - 1; a++) {
                    final MetaModel modelIteratorPointer = modelIterator;                           // Temporary storage for reference check later
                    for (MetaModel metaModel : modelIterator.getChildren(MetaModel.Type.CLASS, MetaModel.Type.INTERFACE, MetaModel.Type.PLACEHOLDER)) {
                        if (metaModel.getName().equals(nestedPathEntityNames[a])) {
                            modelIterator = metaModel;
                            break;
                        }
                    }
                    if (modelIteratorPointer == modelIterator) {
                        parentMissingLock = true;
                        break;
                    }
                }
                if (parentMissingLock) continue;

                modelIterator = MetaModel.getMetaModel(infoContainer.getType(), nestedPathEntityNames[nestedPathEntityCount - 1], modelIterator);
                modelIterator.setDocURL(infoContainer.getJDocURL());

                nestQueueChanges = true;
            }
            if (!nestQueueChanges) {                                                                // Replace first elements first unclear parent with MetaPlaceholder
                final ClassEntityInfoContainer infoContainer = entityNestQueue.iterator().next();
                MetaModel modelIterator = infoContainer.getMetaPackage();
                final String[] nestedPathEntityNames = infoContainer.getMetaPath();
                final int nestedPathEntityCount = nestedPathEntityNames.length;
                boolean addedNewPlaceholder = false;
                for (int a = 0; a < nestedPathEntityCount - 1; a++) {
                    final MetaModel modelIteratorPointer = modelIterator;                           // Temporary storage for reference check later
                    for (MetaModel metaModel : modelIterator.getChildren(MetaModel.Type.CLASS, MetaModel.Type.INTERFACE, MetaModel.Type.PLACEHOLDER)) {
                        if (metaModel.getName().equals(nestedPathEntityNames[a])) {
                            modelIterator = metaModel;
                            break;
                        }
                    }
                    if (modelIteratorPointer == modelIterator) {
                        MetaModel.getMetaModel(MetaModel.Type.PLACEHOLDER, nestedPathEntityNames[a], modelIterator);
                        addedNewPlaceholder = true;
                        break;
                    }
                }
                if (!addedNewPlaceholder)
                    throw new RuntimeException("Endless loop: Queue isn't making progress (Unreachable code segment, handled above)");
            }
        }

        return packageRoot;
    }

    private static String[] getPathParts(String path) {
        return path.split("\\.");
    }
}
