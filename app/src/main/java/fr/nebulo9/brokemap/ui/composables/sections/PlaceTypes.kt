package fr.nebulo9.brokemap.ui.composables.sections

import android.graphics.drawable.Icon
import android.media.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import fr.nebulo9.brokemap.R

data class PlaceType(
    val id: String,
    val label: String,
    val icon: ImageVector
)

val placeTypes = listOf(
    PlaceType("restaurant", "Restaurant", Icons.Default.Restaurant),
    PlaceType("bar", "Bar", Icons.Default.LocalBar),
    PlaceType("dancing_bar", "Dancing Bar", Icons.Default.Nightlife),
    PlaceType("museum", "Museum", Icons.Default.Museum),
    PlaceType("bench", "Bench", Icons.Default.Chair),
    PlaceType("fastfood", "Fast Food", Icons.Default.Fastfood),
)