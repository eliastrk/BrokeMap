package fr.nebulo9.brokemap.ui.composables.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.nebulo9.brokemap.ui.composables.buttons.FilterChipButton

@Composable
fun FastFoodFiltersSection(
    filters: SelectedFilters,
    uiData: FilterUiData,
    onFiltersChange: (SelectedFilters) -> Unit
) {
    Spacer(modifier = Modifier.height(12.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipButton(
            label = "Student discount",
            selected = filters.fastfoodStudentDiscount,
            onClick = {
                onFiltersChange(
                    filters.copy(
                        fastfoodStudentDiscount = !filters.fastfoodStudentDiscount
                    )
                )
            }
        )

        FilterChipButton(
            label = "Terrace",
            selected = filters.fastfoodTerrace,
            onClick = {
                onFiltersChange(
                    filters.copy(
                        fastfoodTerrace = !filters.fastfoodTerrace
                    )
                )
            }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    Text("Item price caps", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (uiData.fastfoodItemPriceBounds.isEmpty()) {
            Text(
                text = "No item price data available yet.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            uiData.fastfoodItemPriceBounds.toSortedMap().forEach { (item, bounds) ->
                PriceSliderFilterRow(
                    label = item,
                    bounds = bounds,
                selectedCap = filters.fastfoodItemPriceCaps[item],
                onCapChange = { cap ->
                    val isAtMax = cap != null && cap >= bounds.max - 0.0001
                    val updated =
                        if (cap == null || isAtMax) {
                            filters.fastfoodItemPriceCaps - item
                        } else {
                            filters.fastfoodItemPriceCaps + (item to cap)
                        }
                        onFiltersChange(filters.copy(fastfoodItemPriceCaps = updated))
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}
