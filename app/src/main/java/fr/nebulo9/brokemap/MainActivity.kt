package fr.nebulo9.brokemap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import fr.nebulo9.brokemap.api.RetrofitClient
import fr.nebulo9.brokemap.ui.composables.BrokeMapApp
import fr.nebulo9.brokemap.ui.composables.screens.MapScreen
import fr.nebulo9.brokemap.ui.theme.BrokeMapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.init(this)
        enableEdgeToEdge()
        setContent {
            BrokeMapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BrokeMapApp()
                }
            }
        }
    }
}
