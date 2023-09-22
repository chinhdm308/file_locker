package com.base.data.remote.interceptors

import android.content.Context
import com.base.data.remote.NetworkEvent
import com.base.data.remote.NetworkState
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.nio.charset.Charset
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(
    @ApplicationContext val context: Context,
    private val networkEvent: NetworkEvent
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
//        if (!context.isConnected) {
//            networkEvent.publish(NetworkState.NO_INTERNET)
//        } else {
        try {
            val response = chain.proceed(request)
            val responseBody = response.body
            val source = responseBody?.source()
            source?.request(Long.MAX_VALUE)
            val buffer = source?.buffer
            val responseBodyString = buffer?.clone()?.readString(Charset.forName("UTF-8"))
            when (response.code) {
                in 200..299 -> {}
                HttpURLConnection.HTTP_UNAVAILABLE -> networkEvent.publish(NetworkState.ServerNotAvailable) // 503
                HttpURLConnection.HTTP_UNAUTHORIZED -> networkEvent.publish(NetworkState.UnAuthorized) //   401
                HttpURLConnection.HTTP_NOT_FOUND -> networkEvent.publish(NetworkState.NotFound) // 404
                HttpURLConnection.HTTP_FORBIDDEN -> networkEvent.publish(NetworkState.Forbidden) // 403
                HttpURLConnection.HTTP_BAD_REQUEST -> networkEvent.publish(NetworkState.BadRequest) // 400
                else -> {
//                    networkEvent.publish(
//                        NetworkState.Generic(gson.fromJson(responseBodyString, ApiException::class.java))
//                    )
                }
            }
            return response
        } catch (e: SocketTimeoutException) {
            networkEvent.publish(NetworkState.ConnectionLost)
        } catch (e: ConnectException) {
            when (request.method) {
                "GET" -> {
                    networkEvent.publish(NetworkState.ConnectExceptionGetMethod)
                }

                "POST" -> {
                    networkEvent.publish(NetworkState.ConnectExceptionPostMethod)
                }
            }
        } catch (e: Exception) {
            if ("Canceled" != e.message) {
                networkEvent.publish(NetworkState.Error)
            }
        }
//        }
        return chain.proceed(request)
    }
}
