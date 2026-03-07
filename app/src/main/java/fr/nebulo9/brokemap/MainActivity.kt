package fr.nebulo9.brokemap
import fr.nebulo9.brokemap.ui.composables.buttons.FilterButton
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import fr.nebulo9.brokemap.ui.theme.BrokeMapTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import fr.nebulo9.brokemap.ui.composables.sections.FilterSection
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.init(this)
        enableEdgeToEdge()
        setContent {
            BrokeMapTheme {
                var showFilter by remember { mutableStateOf(false) }

                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
                }


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState
                        )

                        FilterButton(
                            onClick = { showFilter = true },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        )

                        if (showFilter){
                            FilterSection(
                                onDismiss = {showFilter = false}
                            )
                        }
                    }
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