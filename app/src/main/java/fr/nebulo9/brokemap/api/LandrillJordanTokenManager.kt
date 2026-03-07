package fr.nebulo9.brokemap.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeout

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

/*
Main class to manage the authentication token used to perform requests
to the API.
 */
class LandrillJordanTokenManager(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val EXPIRY_KEY = stringPreferencesKey("token_expiry")
        private const val TIMEOUT_MS = 5000L
    }

    suspend fun saveToken(token: String, expiryMinutes: Int = 30) {
        try {
            Log.d("TokenManager", "Attempting to save token...")
            val expiryTime = System.currentTimeMillis() + (expiryMinutes * 60 * 1000)

            withTimeout(TIMEOUT_MS) {
                context.dataStore.edit { preferences ->
                    preferences[TOKEN_KEY] = token
                    preferences[EXPIRY_KEY] = expiryTime.toString()
                }
            }

            Log.d("TokenManager", "✓ Token saved successfully, expiry: $expiryTime")
        } catch (e: TimeoutCancellationException) {
            Log.e("TokenManager", "✗ Timeout saving token", e)
            throw Exception("Failed to save token: timeout")
        } catch (e: Exception) {
            Log.e("TokenManager", "✗ Error saving token: ${e.message}", e)
            throw e
        }
    }

    suspend fun getToken(): String? {
        return try {
            Log.d("TokenManager", "Attempting to read token...")

            val token = withTimeout(TIMEOUT_MS) {
                context.dataStore.data.first()[TOKEN_KEY]
            }

            Log.d("TokenManager", "✓ Token read: ${if (token != null) "EXISTS (${token.take(20)}...)" else "NULL"}")
            token

        } catch (e: TimeoutCancellationException) {
            Log.e("TokenManager", "✗ Timeout reading token")
            null
        } catch (e: Exception) {
            Log.e("TokenManager", "✗ Error reading token: ${e.message}", e)
            null
        }
    }

    suspend fun isTokenExpired(): Boolean {
        return try {
            Log.d("TokenManager", "Checking token expiry...")

            val expiryTime = withTimeout(TIMEOUT_MS) {
                context.dataStore.data.first()[EXPIRY_KEY]?.toLongOrNull() ?: 0L
            }

            val currentTime = System.currentTimeMillis()
            val expired = currentTime >= expiryTime

            Log.d("TokenManager", "✓ Expiry check - Current: $currentTime, Expiry: $expiryTime, Expired: $expired")
            expired

        } catch (e: TimeoutCancellationException) {
            Log.e("TokenManager", "✗ Timeout checking expiry, assuming expired")
            true
        } catch (e: Exception) {
            Log.e("TokenManager", "✗ Error checking expiry: ${e.message}", e)
            true
        }
    }

    suspend fun clearToken() {
        try {
            Log.d("TokenManager", "Clearing token...")

            withTimeout(TIMEOUT_MS) {
                context.dataStore.edit { preferences ->
                    preferences.remove(TOKEN_KEY)
                    preferences.remove(EXPIRY_KEY)
                }
            }

            Log.d("TokenManager", "✓ Token cleared")
        } catch (e: TimeoutCancellationException) {
            Log.e("TokenManager", "✗ Timeout clearing token", e)
        } catch (e: Exception) {
            Log.e("TokenManager", "✗ Error clearing token: ${e.message}", e)
        }
    }
}