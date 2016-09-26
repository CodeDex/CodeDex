package com.github.codedex.github;

import com.github.codedex.github.api.GitHubService;
import com.github.codedex.github.models.Commit;
import com.github.codedex.github.models.Gist;
import com.github.codedex.github.models.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Github api client
 */

public class Github {

    private GitHubService service;

    public Github() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();
        service = retrofit.create(GitHubService.class);
    }

    public Call<List<Repo>> getRepos(String user) {
        return service.listRepos(user);
    }

    public Call<Repo> getRepo(String user, String repo) {
        return service.repo(user, repo);
    }

    public Call<List<Commit>> getRepoCommits(String user, String repo) {
        return service.repoCommits(user, repo);
    }

    public Call<List<Gist>> getGists(String user) {
        return service.listGists(user);
    }
}
