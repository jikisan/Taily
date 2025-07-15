package org.jikisan.taily.util

import androidx.compose.ui.graphics.ImageBitmap
import network.chaintech.cmpimagepickncrop.utils.ImageFileFormat
import network.chaintech.cmpimagepickncrop.utils.toByteArray

expect suspend fun readFileAsBytes(filePath: String): ByteArray?

// Add this function to your composable or create a utility file
fun resizeImageBitmap(bitmap: ImageBitmap, maxWidth: Int = 800, maxHeight: Int = 600): ImageBitmap {
    val originalWidth = bitmap.width
    val originalHeight = bitmap.height

    if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
        return bitmap
    }

    // Calculate scale to fit within bounds
    val scale = minOf(
        maxWidth.toFloat() / originalWidth,
        maxHeight.toFloat() / originalHeight
    )

    val newWidth = (originalWidth * scale).toInt()
    val newHeight = (originalHeight * scale).toInt()

    // Convert to ByteArray, then back to smaller ImageBitmap
    val byteArray = bitmap.toByteArray(ImageFileFormat.JPEG, quality = 0.8f)

    // You'll need to find the right conversion method for your CMP version
    // This is the part that's tricky without the right imports
    return bitmap // placeholder - this still needs proper resizing
}

//fun ImageBitmap.resize(maxWidth: Int = 800, maxHeight: Int = 600): ImageBitmap {
//    val originalWidth = this.width
//    val originalHeight = this.height
//
//    if (originalWidth <= maxWidth && originalHeight <= maxHeight) {
//        return this
//    }
//
//    val scale = minOf(
//        maxWidth.toFloat() / originalWidth,
//        maxHeight.toFloat() / originalHeight
//    )
//
//    val newWidth = (originalWidth * scale).toInt()
//    val newHeight = (originalHeight * scale).toInt()
//
//    // You'll need to implement platform-specific resizing here
//    // For now, return the original (this will still cause the error)
//    return this
//}