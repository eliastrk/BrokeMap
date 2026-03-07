package fr.nebulo9.brokemap.ui.composables.sections

import android.media.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import fr.nebulo9.brokemap.R

data class PlaceType(
    val id: String,
    val label: String,
    val icon: Image
)

val placeTypes = listOf(
    PlaceType("restaurant", "Restaurant", Icons.Default.Restaurant),
    PlaceType("Musem", "m", ),
    PlaceType("bar", "Bar", Icons.Default.LocalBar),
    PlaceType("park", "Park", Icons.Default.Park),
    PlaceType("gym", "Gym", Icons.Default.FitnessCenter),
    PlaceType("shop", "Shop", Icons.Default.Store),
)