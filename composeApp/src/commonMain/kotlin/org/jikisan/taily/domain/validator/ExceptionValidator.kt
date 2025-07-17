package org.jikisan.taily.domain.validator

fun isNetworkError(message: String): Boolean {
    return  message.contains("network") ||
            message.contains("connection") ||
            message.contains("timeout") ||
            message.contains("internet connection appears to be offline") ||
            message.contains("unable to resolve host") ||
            message.contains("no address associated with hostname")
}