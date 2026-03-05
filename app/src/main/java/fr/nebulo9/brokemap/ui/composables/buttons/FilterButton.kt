package fr.nebulo9.brokemap.ui.composables.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList

@Composable
fun FilterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        modifier = modifier
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filtres"
            )
        }
    }
}