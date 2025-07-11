package org.jikisan.taily.data.remote.supabase.storage

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.UploadStatus
import io.ktor.http.ContentType
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow
import org.jikisan.cmpecommerceapp.util.Constant
import org.jikisan.taily.data.remote.supabase.config.SupabaseConfig

data class UserFile(
    val id: String,
    val name: String,
    val size: Long,
    val type: String,
    val url: String,
    val createdAt: String
)

class StorageManager(private val supabaseClient: SupabaseClient) {

    private val storage = supabaseClient.storage
    private val bucket = storage.from(Constant.PET_PROFILE_PICTURE_BUCKET)
    suspend fun uploadFile(
        userId: String,
        fileData: ByteArray,
    ): Result<String> {
        return try {
            val filePath = "$userId/${userId}_profile_photo"
            var uploadSuccess = false

            bucket.uploadAsFlow(filePath, fileData){
                upsert = true
            }.collect { status ->
                when(status) {
                    is UploadStatus.Progress -> {
                        println("Progress: ${status.totalBytesSend.toFloat() / status.contentLength * 100}%")
                    }
                    is UploadStatus.Success -> {
                        println("Success")
                        uploadSuccess = true
                    }
                }
            }

            if (uploadSuccess) {
                val publicUrl = bucket.publicUrl(filePath)
                Result.success(publicUrl)
            } else {
                Result.failure(Exception("Upload did not complete successfully"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserFiles(userId: String): Result<List<UserFile>> {
        return try {
            val files = bucket.list(userId)
            val userFiles = files.map { file ->
                UserFile(
                    id = file.id ?: "",
                    name = file.name,
                    size = file.metadata?.size?.toLong() ?: 0L,
                    type = getFileType(file.name),
                    url = bucket.publicUrl("$userId/${file.name}"),
                    createdAt = file.createdAt.toString() ?: ""
                )
            }
            Result.success(userFiles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFile(userId: String, fileName: String): Result<Unit> {
        return try {
            val filePath = "$userId/$fileName"
            bucket.delete(filePath)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadFile(userId: String, fileName: String): Result<ByteArray> {
        return try {
            val filePath = "$userId/$fileName"
            val fileData = bucket.downloadAuthenticated(filePath)
            Result.success(fileData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getFileType(fileName: String): String {
        return when (fileName.substringAfterLast('.').lowercase()) {
            "jpg", "jpeg", "png", "gif", "bmp", "webp" -> "Image"
            "pdf" -> "PDF"
            "doc", "docx" -> "Word Document"
            "txt" -> "Text"
            else -> "Document"
        }
    }

    fun getMaxFileSize(fileType: String): Long {
        return when (fileType.lowercase()) {
            "image" -> 10 * 1024 * 1024L // 10MB
            "pdf" -> 50 * 1024 * 1024L // 50MB
            "word document" -> 25 * 1024 * 1024L // 25MB
            else -> 25 * 1024 * 1024L // 25MB default
        }
    }
}