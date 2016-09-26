package com.github.codedex.github.api;

import com.github.codedex.github.models.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Api interface for https://developer.github.com/v3/
 */

public interface GitHubService {

    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: CodeDex"
    })
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);
}
