package fr.nebulo9.brokemap.ui.composables.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import fr.nebulo9.brokemap.ui.composables.buttons.PlaceTypeButton
import androidx.compose.foundation.lazy.grid.items


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    onDismiss: () -> Unit
) {

    val scope = rememberCoroutineScope()
    var selectedPlaceTypes by remember {mutableStateOf(setOf<String>())}
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false   // 🔥 THIS enables drag height behavior
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape

                        ).padding(5.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text("Type of places")
                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(placeTypes) { type ->
                        val selected = type.id in selectedPlaceTypes

                        PlaceTypeButton(
                            label = type.label,
                            icon = type.icon,
                            selected = selected,
                            onClick = {
                                selectedPlaceTypes =
                                    if (selected) selectedPlaceTypes - type.id
                                    else selectedPlaceTypes + type.id
                            }
                        )
                    }
                }
            }
        }
    }
}
