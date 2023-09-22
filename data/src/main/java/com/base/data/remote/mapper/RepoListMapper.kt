package com.base.data.remote.mapper

import com.base.data.remote.dto.RepoDto
import com.base.data.utils.Mapper
import com.base.domain.models.Repo
import javax.inject.Inject

class RepoListMapper @Inject constructor(private val repoDtoMapper: RepoDtoMapper) {
    fun mapFrom(userName: String) = object : Mapper<List<RepoDto>, List<Repo>> {
        override fun mapFromApiResponse(type: List<RepoDto>): List<Repo> {
            return type.map {
                repoDtoMapper.mapFrom(it, userName)
            }
        }
    }
}