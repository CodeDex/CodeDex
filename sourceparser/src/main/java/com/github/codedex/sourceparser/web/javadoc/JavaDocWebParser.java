package com.github.codedex.sourceparser.web.javadoc;

import android.support.annotation.NonNull;

import com.github.codedex.sourceparser.entity.MetaPackage;
import com.github.codedex.sourceparser.exception.NoJavadocURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

            List<MetaPackage> metaPackages = new ArrayList<>();
            for (int a = 0; a < packages.size(); a++) {
                String[] packageNames = packages.get(a).ownText().trim().split("\\.");
                Iterator<String> packageName = Arrays.asList(packageNames).iterator();

                packages.get(a).attr("href");   // Link to appropriate
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private @NonNull MetaPackage putChildrenPackage(Iterator<String> packageName, MetaPackage parentPackage) {
        if (packageName.hasNext()) {
            String currentPackageName = packageName.next();
            for (int a = 0; a < parentPackage.getChildrenPackages().size(); a++) {
                MetaPackage childPackage = parentPackage.getChildrenPackages().get(a);
                if (childPackage.getPackageName().equals(currentPackageName)) {         // If the child package is the one we search for...
                    parentPackage.getChildrenPackages().remove(childPackage);           // Remove the original child package from parent
                    parentPackage.addChildPackage(putChildrenPackage(packageName, childPackage));   // Add the new one (Basically the same, but recursively updated)
                    return parentPackage;                                               // Return updated parent package
                }
            }
            // If it reaches this point, the package couldn't be found and needs to be created as a child package

            parentPackage.addChildPackage(putChildrenPackage(packageName, new MetaPackage(currentPackageName)));
            // What happened here:
            // The parent package got a child package, which is gonna contain its subpackages thanks to recursion (clean code nay, recursion yay)
        }
        return parentPackage;
    }
}
