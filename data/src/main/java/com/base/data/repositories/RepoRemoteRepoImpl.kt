package com.base.data.repositories

import com.base.data.remote.mapper.RepoListMapper
import com.base.data.remote.services.GithubService
import com.base.data.utils.NetworkBoundResource
import com.base.data.utils.mapFromApiResponse
import com.base.domain.models.Repo
import com.base.domain.repositories.RepoRemoteRepository
import com.base.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepoRemoteRepoImpl @Inject constructor(
    private val githubService: GithubService,
    private val networkBoundResources: NetworkBoundResource,
    private val repoListMapper: RepoListMapper
) : RepoRemoteRepository {
    override suspend fun getListRepo(userName: String): Flow<Resource<List<Repo>>> {
        return mapFromApiResponse(
            resource = networkBoundResources.downloadData {
                githubService.getListRepos(userName)
            },
            mapper = repoListMapper.mapFrom(userName)
        )
    }

    override fun sortListRepo(list: List<Repo>): List<Repo> {
        return list.sortedWith { o1, o2 ->
            when {
                o1.id < o2.id -> 1
                o1.id == o2.id -> 0
                else -> -1
            }
        }
    }

    override fun saveListRepo(repoList: List<Repo>) {
        TODO("Not yet implemented")
    }

    override fun getRepo(name: String, userName: String): Flow<Resource<Repo>> {
        TODO("Not yet implemented")
    }

    override fun saveRepo(repo: Repo) {
        TODO("Not yet implemented")
    }

}