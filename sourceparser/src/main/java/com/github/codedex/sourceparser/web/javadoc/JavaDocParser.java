package com.github.codedex.sourceparser.web.javadoc;

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
        // Entity = Abstract name for f.e. class, interface or enum. However, also used as describing a specific instance of many.
        // Document = HTML document which contains all classes

        final Elements documentEntities = allClassesDocument.select("ul");

        final MetaRoot packageRoot = new MetaRoot();

        for (final Element documentEntity : documentEntities) {
            // PROBLEM: The whole analysis here is based on an english Javadoc.
            // TODO: Instead, try using the URL href of each li and HTML content of it to find out about entity type, package and entity name.

            final String documentEntityDescription = documentEntity.attr("title");  // Example entry: "Annotation in org.package1.package2"
            final int packageEntityNameIndex = documentEntityDescription.lastIndexOf(" in ") + 4;

            if (packageEntityNameIndex <= 4 || documentEntityDescription.length() <= packageEntityNameIndex) continue;
            // Could log an exception, since both package name and entity name each need at least 1 character

            /* Example cases as explanation for if-statement:

            packageIndex: Index + 4 (second-last line)


            "hiThere" - Index: -1 => packageIndex: 3; length: 7
            Index too small

            " in " - Index: 0 => packageIndex: 4; length: 4
            Index too small & Length too short


            "a in " - Index: 1 => packageIndex: 5; length: 5
            Length too short

            "foo in " - Index: 2 => packageIndex: 7; length 7
            Length too short


            " in b" - Index: 0 => packageIndex: 4; length: 5
            Index too small

            " in foo" - Index: 0 => packageIndex: 4; length: 7
            Index too small


            "a in b" - Index: 1 => packageIndex: 5; length: 6
            Works out
             */

            final String packagePath = documentEntityDescription.substring(packageEntityNameIndex);
            final String[] packageEntityNames = packagePath.split("\\.");

            MetaPackage packageParentIteratorCache = packageRoot;
            for (String packageEntityName : packageEntityNames) {

                MetaPackage packageEntityCache = null;
                for (MetaModel packageEntity : packageParentIteratorCache.getChildren(MetaModel.Type.PACKAGE))
                    if (packageEntity.getName().equals(packageEntityName)) {
                        packageEntityCache = (MetaPackage) packageEntity;
                        break;
                    }

                if (packageEntityCache == null)
                    // Constructor takes care of adding newly created packageEntityCache to packageParentIteratorCache's children set
                    packageEntityCache = new MetaPackage(packageEntityName, packageParentIteratorCache);

                packageParentIteratorCache = packageEntityCache;
            }

            final int documentEntityTypeEndex = packageEntityNameIndex - 4;
            final String entityTypeAsString = documentEntityDescription.substring(0, documentEntityTypeEndex);

            final MetaType entity;     // Potentially not necessary. REMOVE IF NOT NEEDED
            switch (entityTypeAsString) {
                case "enum":
                    entity = new MetaEnum(documentEntity.text(), packageParentIteratorCache, null);
                    break;
                case "interface":
                    entity = new MetaInterface(documentEntity.text(), packageParentIteratorCache, null);
                    break;
                case "class":
                default:    // !1! Annotations and Exceptions get identified as classes. Introduce specific behaviour later on.
                    entity = new MetaClass(documentEntity.text(), packageParentIteratorCache, null);    // Unfortunately, Javadoc does not provide actual code.
                    break;
            }

            if (urlContext == null) continue;

            final URL entityURL;
            try {
                entityURL = new URL(urlContext, documentEntity.attr("href"));
            } catch (MalformedURLException e) {
                continue;
            }

            entity.setDocURL(entityURL);

            // TODO: Analyze entityURL content and extend MetaCodeModels to contain methods etc.
        }

        return packageRoot;
    }

    private String[] getPathParts(String path) {
        return path.split("\\.");
    }
}
