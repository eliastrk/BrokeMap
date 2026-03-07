package fr.nebulo9.brokemap.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.nebulo9.brokemap.api.LandrillJordanTokenManager
import fr.nebulo9.brokemap.api.LoginRequest
import fr.nebulo9.brokemap.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(context: Context) : ViewModel() {

    private val tokenManager = LandrillJordanTokenManager(context)
    private val api = RetrofitClient.apiService

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        Log.d("AuthViewModel", "════════════════════════════════")
        Log.d("AuthViewModel", "ViewModel initialized")
        Log.d("AuthViewModel", "════════════════════════════════")
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "▶ Checking auth status...")

                val token = tokenManager.getToken()
                Log.d("AuthViewModel", "Token: ${if (token != null) "EXISTS" else "NULL"}")

                if (token != null && !tokenManager.isTokenExpired()) {
                    Log.d("AuthViewModel", "✓ Valid token found - authenticated")
                    _authState.value = AuthState.Authenticated
                    return@launch
                }

                Log.d("AuthViewModel", "→ No valid token, attempting auto-login...")
                automaticLogin()

            } catch (e: Exception) {
                Log.e("AuthViewModel", "✗ Error checking auth: ${e.message}", e)
                automaticLogin()
            }
        }
    }

    private suspend fun automaticLogin() {
        try {
            Log.d("AuthViewModel", "▶ Starting automatic login...")

            val response = api.login(
                LoginRequest(
                    username = "default_consumer",
                    password = "5csSfw2p0Fha3oTQUT4pqM"
                )
            )

            Log.d("AuthViewModel", "✓ Login successful")
            Log.d("AuthViewModel", "  Token: ${response.access_token.take(30)}...")

            tokenManager.saveToken(response.access_token, expiryMinutes = 30)
            Log.d("AuthViewModel", "✓ Token saved")

            _authState.value = AuthState.Authenticated
            Log.d("AuthViewModel", "✓ State set to: Authenticated")
            Log.d("AuthViewModel", "════════════════════════════════")

        } catch (e: HttpException) {
            Log.e("AuthViewModel", "✗ HTTP error: ${e.code()} - ${e.message()}")
            _authState.value = AuthState.Error(
                "Server error (${e.code()}): ${e.message()}"
            )

        } catch (e: IOException) {
            Log.e("AuthViewModel", "✗ Network error: ${e.message}")
            _authState.value = AuthState.Error(
                "Cannot connect to server"
            )

        } catch (e: Exception) {
            Log.e("AuthViewModel", "✗ Unknown error: ${e.message}", e)
            _authState.value = AuthState.Error(
                "Login failed: ${e.message ?: "Unknown error"}"
            )
        }
    }

    fun retry() {
        Log.d("AuthViewModel", "Retry requested")
        _authState.value = AuthState.Loading
        checkAuthStatus()
    }
}