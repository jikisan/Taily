package org.jikisan.taily.data.remote.supabase

import platform.Foundation.NSBundle
import platform.Foundation.NSDictionary
import platform.Foundation.dictionaryWithContentsOfFile

actual object SupabaseConfig {
    actual val anonKey: String
        get() = getStringResource(
            filename = "Secrets",
            fileType = "plist",
            valueKey = "supabaseAnonKey"
        ) ?: ""
}

internal fun getStringResource(
    filename: String,
    fileType: String,
    valueKey: String,
): String? {
    val result = NSBundle.mainBundle.pathForResource(filename, fileType)?.let {
        val map = NSDictionary.dictionaryWithContentsOfFile(it)
        map?.get(valueKey) as? String
    }
    return result
}

