package fr.nebulo9.brokemap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import fr.nebulo9.brokemap.ui.composables.screens.MapScreen
import fr.nebulo9.brokemap.ui.theme.BrokeMapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrokeMapTheme {
                MapScreen()
            }
        }
    }
}
