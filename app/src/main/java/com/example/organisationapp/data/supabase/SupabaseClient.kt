package com.example.organisationapp.data.supabase

import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.createSupabaseClient
import com.example.organisationapp.BuildConfig

/**
 * Supabase client configuration with Google OAuth support
 */
object SupabaseClient {
    
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(GoTrue) {
            // Configuration pour deep linking
            scheme = "app"
            host = "supabase.com"
        }
    }
}
