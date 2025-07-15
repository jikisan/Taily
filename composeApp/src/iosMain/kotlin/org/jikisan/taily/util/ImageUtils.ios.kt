package org.jikisan.taily.util

// iosMain/kotlin/FileUtils.ios.kt
import kotlinx.cinterop.*
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class)
actual suspend fun readFileAsBytes(filePath: String): ByteArray? {
    return try {
        val data = NSData.dataWithContentsOfFile(filePath)
        data?.let {
            ByteArray(it.length.toInt()) { index ->
                it.bytes!!.reinterpret<ByteVar>()[index]
            }
        }
    } catch (e: Exception) {
        println("Error reading file: ${e.message}")
        null
    }
}