package com.github.codedex.sourceparser.web.javadoc;

import com.github.codedex.sourceparser.entity.OldMetaContainer;
import com.github.codedex.sourceparser.exception.NoJavadocURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import static com.github.codedex.sourceparser.entity.OldMetaContainer.Type.PACKAGE;
import static com.github.codedex.sourceparser.entity.OldMetaContainer.createContainer;

/**
 * Created by IPat (Local) on 24.09.2016.
 */

public class JavaDocWebParser {
    public static OldMetaContainer getMetaData(URL javadocURL) throws NoJavadocURLException {

        OldMetaContainer rootPackage = OldMetaContainer.getNewRootPackage();  // Contains complete metadata about javadocs
        try {
            Document doc = Jsoup.connect(javadocURL.toString()).get();  // Fetch HTML document

            // Fetch packages from HTML document into OldMetaContainer object
            Elements packages = doc.select("ul[title=Packages]").first().children();
            for (int a = 0; a < packages.size(); a++) {
                String[] packageNames = packages.get(a).ownText().trim().split("\\.");
                Iterator<String> packageName = Arrays.asList(packageNames).iterator();
                rootPackage = createContainer(PACKAGE, packageName, rootPackage, new URL(javadocURL, packages.get(a).attr("href")));

                // Check the javadoc link the package offers for every interface, class, enum etc.
                // and add it to the package

                if (!doc.select("ul[title=Classes]").isEmpty()) {
                    Elements classes = doc.select("ul[title=Classes]").first().children();
                    for (Element class_ : classes) {
                        String[] classPath = class_.ownText().trim().split("\\.");
                        // createClass function for creating path as packages before actual class
                    }
                }

                // LIST OF POSSIBLE (remaining-to-code) ENTITY TYPES:

                // ul title="Interfaces"
                // ul title="Enums"
                // ul title="Annotation Types"
                // ul title="Exceptions"
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return rootPackage;
    }

    // ! v
    // IDEA: Rename OldMetaContainer to OldMetaContainer with own enum describing whether its a class, an interface or a package.
    // ! ^
}
