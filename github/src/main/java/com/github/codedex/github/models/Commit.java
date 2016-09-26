package com.github.codedex.github.models;

/**
 * Representation of commit json object, sample: https://api.github.com/repos/FabianTerhorst/Floppy/commits
 */

public class Commit {

    private String sha;

    private CommitBody commit;

    private static class CommitBody {

        public String message;
    }

    public String getSha() {
        return sha;
    }

    public String getMessage() {
        return commit.message;
    }
}
