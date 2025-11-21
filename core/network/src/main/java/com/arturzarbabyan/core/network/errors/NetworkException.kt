package com.arturzarbabyan.core.network.errors

sealed class NetworkException(
    message: String?,
    cause: Throwable? = null
) : Exception(message, cause) {

    class HttpError(
        val code: Int,
        val errorBody: String?,
        message: String?,
        cause: Throwable? = null
    ) : NetworkException(message, cause)

    class NetworkUnavailable(
        cause: Throwable? = null
    ) : NetworkException("No internet connection", cause)

    class Unknown(
        cause: Throwable? = null
    ) : NetworkException(cause?.message ?: "Unknown error", cause)
}
