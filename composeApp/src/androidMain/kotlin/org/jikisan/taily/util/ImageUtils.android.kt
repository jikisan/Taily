package org.jikisan.taily.util

import java.io.File

actual suspend fun readFileAsBytes(filePath: String): ByteArray? {
    return try {
        File(filePath).readBytes()
    } catch (e: Exception) {
        println("Error reading file: ${e.message}")
        null
    }
}