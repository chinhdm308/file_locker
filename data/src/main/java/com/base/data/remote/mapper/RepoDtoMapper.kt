package com.base.data.remote.mapper

import com.base.data.remote.dto.RepoDto
import com.base.data.utils.Mapper
import com.base.domain.models.Repo
import javax.inject.Inject

class RepoDtoMapper @Inject constructor() : Mapper<RepoDto, Repo> {
    override fun mapFromApiResponse(type: RepoDto): Repo {
        return Repo(
            id = type.id,
            name = type.name,
            description = type.description,
            url = type.url
        )
    }

    fun mapFrom(type: RepoDto, userName: String): Repo {
        return Repo(
            id = type.id,
            name = type.name,
            description = type.description,
            url = type.url,
            userName = userName
        )
    }
}