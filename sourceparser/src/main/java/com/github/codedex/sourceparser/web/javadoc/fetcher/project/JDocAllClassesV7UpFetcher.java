package com.github.codedex.sourceparser.web.javadoc.fetcher.project;

import com.github.codedex.sourceparser.entity.project.MetaClass;
import com.github.codedex.sourceparser.entity.project.MetaInterface;
import com.github.codedex.sourceparser.entity.project.MetaPackage;
import com.github.codedex.sourceparser.entity.project.MetaPlaceholder;
import com.github.codedex.sourceparser.entity.project.model.MetaModel;
import com.github.codedex.sourceparser.entity.project.model.MetaType;
import com.github.codedex.sourceparser.entity.project.specific.MetaRoot;
import com.github.codedex.sourceparser.web.javadoc.fetcher.model.JDocAllClassesFetcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 *
 * Fetches a class structure from the "All Classes"-Document in a Javadoc.
 */

public class JDocAllClassesV7UpFetcher extends JDocAllClassesFetcher {

    private final URL urlContext;

    public JDocAllClassesV7UpFetcher(Document jDocument) throws MalformedURLException {
        this(jDocument, new URL(jDocument.location()));
    }

    public JDocAllClassesV7UpFetcher(Document jDocument, URL source) {
        super(jDocument);
        urlContext = source;
    }

    /**
     * Fetches all the types from the "All Classes"-JavaDoc document.
     * @param root the root package of the types
     * @param nestedEntities the list with references to the type
     * @param input the "All Classes" document
     * @return the root package.
     */
    protected MetaRoot getAllClasses(MetaRoot root, List<MetaType> nestedEntities, Document input) {
        if (root == null) root = new MetaRoot();
        final Elements container;
        final Element ulList = input.select("ul").first();
        if (ulList == null)
            container = new Elements(0);
        else
            container = ulList.select("li");

        for (Element row : container)
            nestedEntities.add(getSingleType(root, row));
        return root;
    }

    /**
     * Fetches a single class from a row from the "All Classes"-JavaDoc document.
     * @param root the root package of all types
     * @param row a single row containing one type
     * @return the type of the row
     */
    private MetaType getSingleType(MetaRoot root, Element row) {

        // Define given variables
        final Element documentEntityLinkReference = row.select("a").first();

        final boolean isItalic = !row.select("i").isEmpty();
        final String rowText = row.text()==null?"":row.text();
        final String relJDocURL;
        final String title;
        if (documentEntityLinkReference != null) {
            relJDocURL = documentEntityLinkReference.attr("href");
            title = documentEntityLinkReference.attr("title");
        } else {
            relJDocURL = null;
            title = null;
        }
        final String[] rowTextElements = rowText.split("\\.");

        // Define variables to initialize
        final MetaType metaType;

        final String name;
        final URL jdocURL = getURL(urlContext, relJDocURL);
        final MetaModel parent;
        final String[] packagePathElements;
        final String[] nestedPathElements;

        // packagePathElements
        if (relJDocURL != null && !relJDocURL.equals("")) {
            final int packageEndex = title.lastIndexOf("/");
            final String packagePath = title.substring(0, packageEndex);
            packagePathElements = packagePath.split("/");
        } else if (title != null && !title.endsWith(" in ")) {                                      // English, however it appears JavaDoc isn't available in other languages anyways
            final int packageIndex = title.indexOf(" in ") + 4;
            final String packagePath = title.substring(packageIndex, title.length());
            packagePathElements = packagePath.split("\\.");
        } else {
            packagePathElements = new String[0];
        }

        // nestedPathElements
        if (rowTextElements.length == 1 || rowTextElements.length == 0)
            nestedPathElements = new String[0];
        else
            nestedPathElements = Arrays.copyOf(rowTextElements, rowTextElements.length - 1);

        // name
        if (rowTextElements.length == 0)
            name = "";
        else
            name = rowTextElements[rowTextElements.length - 1];

        // parent
        parent = getParent(root, packagePathElements, nestedPathElements);

        // metaType
        if (isItalic)
            metaType = new MetaInterface(name, jdocURL, parent, null, null, null, null, null, null);
        else
            metaType = new MetaClass(name, jdocURL, parent, null, null, null, null, null, null, null, null);
        return metaType;
    }

    /**
     * Fetches a parent MetaModel
     * @param modelIterator the root MetaModel the packages get created in
     * @param packagePathName the package path
     * @param nestedPathName the nested path
     * @return the parent package at the very end (!= modelIterator parameter; == modelIterator AFTER iterating)
     */
    private MetaModel getParent(MetaModel modelIterator, String[] packagePathName, String[] nestedPathName) {
        if (packagePathName != null)
            for (String packagePathEntityName : packagePathName) {
                final MetaModel modelIteratorPointer = modelIterator;
                final Set<MetaModel> children = modelIterator.getChildren(MetaModel.Type.PACKAGE);
                for (MetaModel metaModel : children)
                    if (metaModel.getName().equals(packagePathEntityName)) {
                        modelIterator = metaModel;
                        break;
                    }
                if (modelIteratorPointer == modelIterator)                                          // This means the for loop didn't find the searched package
                    modelIterator = new MetaPackage(packagePathEntityName, null, modelIteratorPointer, null);
            }
        if (nestedPathName != null)
            for (String nestedPathEntityName : nestedPathName) {
                final MetaModel modelIteratorPointer = modelIterator;                         // Temporary storage for reference check later
                final Set<MetaModel> children = modelIterator.getChildren(MetaModel.Type.CLASS, MetaModel.Type.INTERFACE, MetaModel.Type.PLACEHOLDER);
                for (MetaModel metaModel : children)
                    if (metaModel.getName().equals(nestedPathEntityName)) {
                        modelIterator = metaModel;                                  // Safe cast, getChildren(Type.PACKAGE) called
                        break;
                    }
                if (modelIteratorPointer == modelIterator)                                      // This means the for loop didn't find the searched package
                    modelIterator = new MetaPlaceholder(nestedPathEntityName, null, modelIteratorPointer, null);
            }

        return modelIterator;
    }

    private URL getURL(URL context, String relativeURL) {
        if (relativeURL == null || relativeURL.equals(""))
            return null;
        try {
            return new URL(context, relativeURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}