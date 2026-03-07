package fr.nebulo9.brokemap.ui.composables.sections

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import fr.nebulo9.brokemap.ui.composables.buttons.PlaceTypeButton
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextAlign



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    filters: SelectedFilters,
    onFiltersChange: (SelectedFilters) -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false   // THIS enables drag height behavior
    )
    val listState = rememberLazyListState()
    var lastSelectedTypes by remember { mutableStateOf(filters.selectedTypes) }

    LaunchedEffect(filters.selectedTypes) {

        val newType =
            filters.selectedTypes.firstOrNull { it !in lastSelectedTypes }

        if (newType != null) {

            val index = when (newType) {
                "restaurant" -> 2
                "fastfood" -> if ("restaurant" in filters.selectedTypes) 3 else 2
                "bar" -> 2 + filters.selectedTypes.indexOf(newType)
                "museum" -> 2 + filters.selectedTypes.indexOf(newType)
                "bench" -> 2 + filters.selectedTypes.indexOf(newType)
                "dancing_bar" -> 2 + filters.selectedTypes.indexOf(newType)
                else -> 2
            }

            listState.animateScrollToItem(index)
        }

        lastSelectedTypes = filters.selectedTypes
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null
    ) {

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
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
            }

            item {
                Text(
                    text = "Type of places",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 96.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp, max = 260.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    userScrollEnabled = false
                ) {
                    items(placeTypes) { type ->
                        val selected = type.id in filters.selectedTypes

                        PlaceTypeButton(
                            label = type.label,
                            icon = type.icon,
                            selected = selected,
                            onClick = {
                                val newTypes =
                                    if (selected) filters.selectedTypes - type.id
                                    else filters.selectedTypes + type.id

                                onFiltersChange(filters.copy(selectedTypes = newTypes))
                            }
                        )
                    }
                }
            }

            if ("restaurant" in filters.selectedTypes) {
                item {
                    FilterCard("Restaurant") {
                        RestaurantFiltersSection(filters, onFiltersChange)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

            if ("fastfood" in filters.selectedTypes) {
                item {
                    FilterCard("Fast Food") {
                        FastFoodFiltersSection(filters, onFiltersChange)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

            if ("bar" in filters.selectedTypes) {
                item {
                    FilterCard("Bar") {
                        BarFiltersSection(filters, onFiltersChange)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

            if ("dancing_bar" in filters.selectedTypes) {
                item {
                    FilterCard("Dancing Bar") {
                        DancingBarFiltersSection()
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

            if ("museum" in filters.selectedTypes) {
                item {
                    FilterCard("Museum") {
                        MuseumFiltersSection(filters, onFiltersChange)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

            if ("bench" in filters.selectedTypes) {
                item {
                    FilterCard("Bench") {
                        BenchFiltersSection()
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }

            }


                }

            }
        }


@Composable
fun FilterCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            content()
        }
    }
}
