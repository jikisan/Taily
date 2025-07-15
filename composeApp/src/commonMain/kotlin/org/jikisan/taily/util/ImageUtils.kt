package org.jikisan.taily.util

import androidx.compose.ui.graphics.ImageBitmap
import network.chaintech.cmpimagepickncrop.utils.ImageFileFormat
import network.chaintech.cmpimagepickncrop.utils.toByteArray

expect suspend fun readFileAsBytes(filePath: String): ByteArray?
