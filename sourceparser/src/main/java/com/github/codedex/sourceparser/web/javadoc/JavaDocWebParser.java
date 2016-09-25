package com.github.codedex.sourceparser.web.javadoc;

import android.support.annotation.NonNull;

import com.github.codedex.sourceparser.entity.MetaPackage;
import com.github.codedex.sourceparser.exception.NoJavadocURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by IPat (Local) on 24.09.2016.
 */

public class JavaDocWebParser {
    public JavaDocWebParser(URL javadocURL) throws NoJavadocURLException {

        // Create packages
        try {
            Document doc = Jsoup.connect(javadocURL.toString()).get();
            Elements packages = doc.select("ul[title=Packages]").first().children();
            Elements classes = doc.select("frame[name=packageFrame] ul").first().children();

            MetaPackage rootPackage = MetaPackage.getNewRootPackage();  // Contains complete metadata about javadocs
            for (int a = 0; a < packages.size(); a++) {
                String[] packageNames = packages.get(a).ownText().trim().split("\\.");
                Iterator<String> packageName = Arrays.asList(packageNames).iterator();
                rootPackage = insertPackage(packageName, rootPackage, new URL(javadocURL, packages.get(a).attr("href")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private @NonNull MetaPackage insertPackage(Iterator<String> packageName, MetaPackage parentPackage, URL docURL) {
        if (packageName.hasNext()) {
            String currentPackageName = packageName.next();
            for (int a = 0; a < parentPackage.getChildrenPackages().size(); a++) {
                MetaPackage childPackage = parentPackage.getChildrenPackages().get(a);
                if (childPackage.getPackageName().equals(currentPackageName)) {         // If the child package is the one we search for...
                    parentPackage.getChildrenPackages().remove(childPackage);           // Remove the original child package from parent
                    parentPackage.addChildPackage(insertPackage(packageName, childPackage, docURL));   // Add the new one (Basically the same, but recursively updated)
                    return parentPackage;                                               // Return updated parent package
                }
            }
            // If it reaches this point, the package couldn't be found and needs to be created as a child package

            parentPackage.addChildPackage(insertPackage(packageName, new MetaPackage(currentPackageName), docURL));
            // What happened here:
            // The parent package got a child package, which is gonna contain its subpackages thanks to recursion (clean code nay, recursion yay)
            return parentPackage;
        }
        parentPackage.setDocURL(docURL);
        return parentPackage;
    }
}
