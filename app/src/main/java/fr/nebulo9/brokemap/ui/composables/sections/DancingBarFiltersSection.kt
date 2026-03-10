package fr.nebulo9.brokemap.ui.composables.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DancingBarFiltersSection(
    filters: SelectedFilters,
    uiData: FilterUiData,
    onFiltersChange: (SelectedFilters) -> Unit
) {

    Spacer(modifier = Modifier.height(12.dp))
    Text("Alcohol price caps", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (uiData.dancingBarAlcoholPriceBounds.isEmpty()) {
            Text(
                text = "No alcohol price data available yet.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            uiData.dancingBarAlcoholPriceBounds.toSortedMap().forEach { (alcohol, bounds) ->
                PriceSliderFilterRow(
                    label = alcohol,
                    bounds = bounds,
                selectedCap = filters.dancingBarAlcoholPriceCaps[alcohol],
                onCapChange = { cap ->
                    val isAtMax = cap != null && cap >= bounds.max - 0.0001
                    val updated =
                        if (cap == null || isAtMax) {
                            filters.dancingBarAlcoholPriceCaps - alcohol
                        } else {
                            filters.dancingBarAlcoholPriceCaps + (alcohol to cap)
                        }
                        onFiltersChange(filters.copy(dancingBarAlcoholPriceCaps = updated))
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}
