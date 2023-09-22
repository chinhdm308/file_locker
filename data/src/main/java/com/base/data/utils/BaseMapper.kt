package com.base.data.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.base.domain.utils.Resource

interface Mapper<R, E> {
    fun mapFromApiResponse(type: R): E
}

//abstract class Mapper<in T,E> {
//    abstract fun mapFrom(from: T): E
//}

fun <R, E> mapFromApiResponse(
    resource: Flow<Resource<R>>,
    mapper: Mapper<R, E>
): Flow<Resource<E>> {
    return resource.map {
        when (it) {
            is Resource.Success -> Resource.Success(mapper.mapFromApiResponse(it.data))
            is Resource.Error -> Resource.Error(it.message, it.code)
            is Resource.Loading -> Resource.Loading(it.loading)
        }
    }
}