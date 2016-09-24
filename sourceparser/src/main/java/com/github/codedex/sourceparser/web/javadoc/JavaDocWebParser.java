package com.github.codedex.sourceparser.web.javadoc;

import com.github.codedex.sourceparser.exception.NoJavadocURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by IPat (Local) on 24.09.2016.
 */

public class JavaDocWebParser {
    public JavaDocWebParser(URL javadocURL) throws NoJavadocURLException {
        try {
            Document doc = Jsoup.connect(javadocURL.toString()).get();
            Elements packages = doc.select("ul[title=Packages]").first().children();
            Elements classes = doc.select("frame[name=packageFrame] ul").first().children();

            for (int a = 0; a < packages.size(); a++) {
                packages.get(a).ownText();      // Package name
                packages.get(a).attr("href");   // Link to appropriate
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
