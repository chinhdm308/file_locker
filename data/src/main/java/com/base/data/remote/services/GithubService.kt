package com.base.data.remote.services

import com.base.data.remote.dto.RepoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {

    @GET("users/{userName}/repos")
    fun getListRepos(@Path("userName") userName: String): Response<List<RepoDto>>

    @GET("repos/{userName}/{repoName}")
    fun getRepo(
        @Path("userName") userName: String,
        @Path("repoName") repoName: String
    ): Response<RepoDto>
}