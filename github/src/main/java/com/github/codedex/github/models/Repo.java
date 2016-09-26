package com.github.codedex.github.models;

import com.squareup.moshi.Json;

/**
 * Representation of repository json object, sample: https://api.github.com/users/fabianterhorst/repos
 */

public class Repo {

    private int id;
    private String name;
    @Json(name = "full_name")
    private String fullName;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }
}
