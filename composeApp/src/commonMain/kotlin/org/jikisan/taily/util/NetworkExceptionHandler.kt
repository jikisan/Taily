package org.jikisan.taily.util

import io.github.aakira.napier.Napier
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG

object NetworkExceptionHandler {

    fun handleNetworkException(exception: Throwable?) {
        when (exception) {
            is ConnectTimeoutException -> {
                val error = "No internet connection. Please check your network."
                Napier.e(exception.message.toString())
                throw Exception(error)
            }

            is SocketTimeoutException -> {
                val error = "Request timed out. Please try again."
                Napier.e(exception.message.toString())
                throw Exception(error)
            }

            else -> {
                val message = exception?.message?.lowercase() ?: ""
                when {
                    message.contains("unable to resolve host", true) ||
                            message.contains("no address associated with hostname", true) ||
                            message.contains("internet connection appears to be offline", true) ||
                            message.contains("network is unreachable", true) -> {
                        val error = "No internet. Check network."
                        Napier.e("$TAG $message")
                        throw Exception(error)
                    }

                    message.contains("network", true) ||
                            message.contains("connection", true) -> {
                        val error = "Network error. Check connection."
                        Napier.e("$TAG $message")
                        throw Exception(error)
                    }

                    message.contains("timeout", true) -> {
                        val error = "Request timed out. Try again."
                        Napier.e("$TAG $message")
                        throw Exception(error)
                    }

                    else -> {
                        val error = "An unexpected error occurred. Please try again later."
                        Napier.e("$TAG $message")
                        throw Exception(error)
                    }
                }

            }
        }
        Napier.e("$TAG Get All Pets By User ID Failed")
    }
}