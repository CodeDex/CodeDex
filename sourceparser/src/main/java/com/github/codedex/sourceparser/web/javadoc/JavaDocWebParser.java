package com.github.codedex.sourceparser.web.javadoc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.codedex.sourceparser.entity.MetaContainer;
import com.github.codedex.sourceparser.exception.NoJavadocURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import static com.github.codedex.sourceparser.entity.MetaContainer.Type.PACKAGE;

/**
 * Created by IPat (Local) on 24.09.2016.
 */

public class JavaDocWebParser {
    public static MetaContainer getMetaData(URL javadocURL) throws NoJavadocURLException {

        MetaContainer rootPackage = MetaContainer.getNewRootPackage();  // Contains complete metadata about javadocs
        try {
            Document doc = Jsoup.connect(javadocURL.toString()).get();  // Fetch HTML document

            // Fetch packages from HTML document into MetaContainer object
            Elements packages = doc.select("ul[title=Packages]").first().children();
            for (int a = 0; a < packages.size(); a++) {
                String[] packageNames = packages.get(a).ownText().trim().split("\\.");
                Iterator<String> packageName = Arrays.asList(packageNames).iterator();
                rootPackage = insertPackage(packageName, rootPackage, new URL(javadocURL, packages.get(a).attr("href")));

                // Check the javadoc link the package offers for every interface, class, enum etc.
                // and add it to the package
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return rootPackage;
    }

    // ! v
    // IDEA: Rename MetaContainer to MetaContainer with own enum describing whether its a class, an interface or a package.
    // ! ^

    private static @NonNull MetaContainer insertPackage(Iterator<String> name, MetaContainer parent, @Nullable URL docURL) {
        if (name == null) return MetaContainer.getNewRootPackage();
        if (parent == null) parent = MetaContainer.getNewRootPackage();
        if (name.hasNext()) {
            String currentName = name.next();
            for (int a = 0; a < parent.getChildPackages().size(); a++) {
                MetaContainer child = parent.getChildPackages().get(a);
                if (child.getName().equals(currentName)) {         // If the child package is the one we search for...
                    parent.getChildPackages().remove(child);           // Remove the original child package from parent
                    parent.addChild(insertPackage(name, child, docURL));   // Add the new one (Basically the same, but recursively updated)
                    return parent;                                               // Return updated parent package
                }
            }
            // If it reaches this point, the package couldn't be found and needs to be created as a child package

            parent.addChild(insertPackage(name, new MetaContainer(PACKAGE, currentName), docURL));
            // What happened here:
            // The parent package got a child package, which is gonna contain its subpackages thanks to recursion (clean code nay, recursion yay)
            return parent;
        }
        parent.setDocURL(docURL);
        return parent;
    }
}
