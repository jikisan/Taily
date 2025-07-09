package org.jikisan.taily.data.remote.supabase.config

import org.jikisan.taily.data.remote.supabase.getStringResource

actual object SupabaseConfig {
    actual val anonKey: String
        get() = getStringResource(
            filename = "Secrets",
            fileType = "plist",
            valueKey = "supabaseAnonKey"
        ) ?: ""
}