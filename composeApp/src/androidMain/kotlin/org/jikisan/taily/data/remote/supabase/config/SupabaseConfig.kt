package org.jikisan.taily.data.remote.supabase.config

import org.jikisan.taily.BuildConfig

actual object SupabaseConfig {
    actual val anonKey: String
        get() = BuildConfig.supabaseAnonKey
}