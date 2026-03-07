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
    PlaceType("restaurant", "Restaurant", Icons.Default.Place),
    PlaceType("bar", "Bar", Icons.Default.Place),
    PlaceType("dancingBar", "Dancing Bar", Icons.Default.Place),
    PlaceType("Museum", "Museum", Icons.Default.Place),
    PlaceType("bench", "Bench", Icons.Default.Place),
    PlaceType("fastFood", "Fast Food", Icons.Default.Place),
)