package com.github.codedex.github.models;

import java.util.Map;

/**
 *  Representation of gist json object, sample: https://api.github.com/users/FabianTerhorst/gists
 */

public class Gist {

    private String url;

    private String id;

    private Map<String, GistFile> files;

    private static class GistFile {

        String filename;

        String type;

        String language;

        String raw_url;

        int size;
    }
}
