package com.base.data.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkEvent @Inject constructor() {

    private val _networkState = MutableSharedFlow<NetworkState>()
    val networkState = _networkState.asSharedFlow()

    fun publish(event: NetworkState) = CoroutineScope(Dispatchers.Main).launch {
        _networkState.emit(event)
    }
}

sealed class NetworkState {
    object AvailableInternet : NetworkState()
    object NoInternet : NetworkState()
    object ServerNotAvailable : NetworkState() // 503
    object NotFound : NetworkState() // 404
    object Forbidden : NetworkState() // 403
    object BadRequest : NetworkState() // 400
    object UnAuthorized : NetworkState() // 401
    object ConnectionLost : NetworkState()
    object Error : NetworkState()
    object Initialize : NetworkState()
    data class Generic(val apiException: Exception) : NetworkState()
    object ConnectExceptionPostMethod : NetworkState()
    object ConnectExceptionGetMethod : NetworkState()
}
