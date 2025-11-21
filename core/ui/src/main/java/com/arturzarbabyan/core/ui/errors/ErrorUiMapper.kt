package com.arturzarbabyan.core.ui.errors

import com.arturzarbabyan.core.network.errors.NetworkException

fun Throwable.toUIMessage(): String {
    return when (this) {
        is NetworkException.NetworkUnavailable ->
            "No internet connection. Please check your network."

        is NetworkException.HttpError -> when (code) {
            401 -> "Authorization error. Invalid API key."
            404 -> "Requested resource not found."
            in 500..599 -> "Server is temporarily unavailable."
            else -> "Request failed with status code $code."
        }

        is NetworkException.Unknown ->
            "Something went wrong. Please try again."

        else -> message ?: "Unexpected error. Please try again."
    }
}
