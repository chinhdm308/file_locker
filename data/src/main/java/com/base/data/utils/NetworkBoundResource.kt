package com.base.data.utils

import com.base.data.di.IoDispatcher
import com.base.domain.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.squareup.moshi.Moshi
import timber.log.Timber

class NetworkBoundResource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val moshi: Moshi
) {
    suspend fun <ResultType> downloadData(api: suspend () -> Response<ResultType>): Flow<Resource<ResultType>> {
        return withContext(ioDispatcher) {
            flow {
                emit(Resource.Loading(true))
                val response: Response<ResultType> = api()
                emit(Resource.Loading(false))
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Resource.Success(data = it))
                    } ?: emit(Resource.Error(message = "Unknown error occurred", code = 0))
                } else {
                    emit(
                        Resource.Error(
                            message = parserErrorBody(response.errorBody()),
                            code = response.code()
                        )
                    )
                }

            }.catch { error ->
                Timber.e(error.localizedMessage)
                emit(Resource.Loading(false))
                delay(5)
                emit(Resource.Error(message = message(error), code = code(error)))
            }
        }
    }

    private fun parserErrorBody(response: ResponseBody?): String {
        val jsonAdapter = moshi.adapter(Map::class.java)
        return response?.let {
            val errorMessage: String? =
                jsonAdapter.fromJson(it.toString())?.get("message")?.toString()
            errorMessage?.ifEmpty { "Whoops! Something went wrong. Please try again." }
            errorMessage
        } ?: "Whoops! Unknown error occurred. Please try again"
    }

    private fun message(throwable: Throwable?): String {
        val jsonAdapter = moshi.adapter(Map::class.java)
        when (throwable) {
            is SocketTimeoutException -> return "Whoops! Connection time out. Please try again"
            is IOException -> return "Whoops! No Internet Connection. Please try again"
            is HttpException -> return try {
                val errorJsonString: String? = throwable.response()?.errorBody()?.string()
                val errorMessage: String? =
                    errorJsonString?.let { jsonAdapter.fromJson(it)?.get("message")?.toString() }
                errorMessage?.ifEmpty { "Whoops! Something went wrong. Please try again." }
                    ?: "Whoops! Unknown error occurred. Please try again"
            } catch (e: Exception) {
                "Whoops! Unknown error occurred. Please try again"
            }
        }
        return "Whoops! Unknown error occurred. Please try again"
    }

    private fun code(throwable: Throwable?): Int {
        return if (throwable is HttpException) (throwable).code()
        else 0
    }
}