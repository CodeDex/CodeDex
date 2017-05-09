package com.github.codedex.sourceparser.javacode.model;

import com.github.codedex.sourceparser.entity.project.specific.MetaRoot;
import com.github.codedex.sourceparser.web.javadoc.JavaDocParser;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.Assert.*;

/**
 * @author Patrick "IPat" Hein
 */

public class ModelTest {
    @Test
    public void java7() throws MalformedURLException {
        final MetaRoot root = JavaDocParser.parse(new URL("https://docs.oracle.com/javase/7/docs/api/allclasses-noframe.html"));
        String rootTree = root.toStringTree();
        System.out.println(rootTree);

        assertFalse("Not null", root == null);
        assertTrue(rootTree.contains("\n|org\n"));
    }
}
