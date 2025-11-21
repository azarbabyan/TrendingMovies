package com.arturzarbabyan.core.network.helper

import com.arturzarbabyan.core.network.errors.NetworkException
import retrofit2.HttpException
import java.io.IOException

suspend inline fun <T> safeApiCall(
    crossinline block: suspend () -> T
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: IOException) {
        Result.failure(NetworkException.NetworkUnavailable(e))
    } catch (e: HttpException) {
        val body = try {
            e.response()?.errorBody()?.string()
        } catch (_: Exception) {
            null
        }
        Result.failure(
            NetworkException.HttpError(
                code = e.code(),
                errorBody = body,
                message = e.message(),
                cause = e
            )
        )
    } catch (e: Throwable) {
        Result.failure(NetworkException.Unknown(e))
    }
}
