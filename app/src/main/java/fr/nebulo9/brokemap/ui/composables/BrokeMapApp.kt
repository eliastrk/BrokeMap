package fr.nebulo9.brokemap.ui.composables

import fr.nebulo9.brokemap.models.AuthViewModel
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import fr.nebulo9.brokemap.models.AuthState
import fr.nebulo9.brokemap.ui.composables.screens.ErrorScreen
import fr.nebulo9.brokemap.ui.composables.screens.LoadingScreen
import fr.nebulo9.brokemap.ui.composables.screens.MainScreen

@Composable
fun BrokeMapApp() {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = remember { AuthViewModel(context) }
    val authState by authViewModel.authState.collectAsState()

    when (val state = authState) {
        is AuthState.Loading -> {
            LoadingScreen()
        }

        is AuthState.Authenticated -> {
            Log.i("API","Connected to the API.")
            MainScreen()
        }

        is AuthState.Error -> {
            Log.e("API", "Failed to connect to the API.")
            ErrorScreen(
                message = state.message,
                onRetry = { authViewModel.retry() }
            )
        }
    }
}