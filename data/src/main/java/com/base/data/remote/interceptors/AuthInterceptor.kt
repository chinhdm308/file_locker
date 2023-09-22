package com.base.data.remote.interceptors

import android.text.TextUtils
import com.base.data.local.datastore.DataStoreRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            val token = dataStoreRepository.getToken()
            var request = chain.request()
            request =
                if (!TextUtils.isEmpty(token) && !request.url.equals("BuildConfig.API_HOST/register")) {
                    chain.request()
                        .newBuilder()
                        .header("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                        .newBuilder()
                        .header("Content-Type", "application/json")
                        .build()
                }
            chain.proceed(request)
        }
    }
}
