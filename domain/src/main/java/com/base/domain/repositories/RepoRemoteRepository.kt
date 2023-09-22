package com.base.domain.repositories

import com.base.domain.models.Repo
import com.base.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RepoRemoteRepository {
    //region LIST REPO
    suspend fun getListRepo(userName: String): Flow<Resource<List<Repo>>>

    fun sortListRepo(list: List<Repo>): List<Repo>

    fun saveListRepo(repoList: List<Repo>)
    //endregion

    //region REPO
    fun getRepo(name: String, userName: String): Flow<Resource<Repo>>

    fun saveRepo(repo: Repo)

}