package fr.nebulo9.brokemap.ui.composables.sections

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MuseumFiltersSection(
    filters: SelectedFilters,
    uiData: FilterUiData,
    onFiltersChange: (SelectedFilters) -> Unit
) {

    Spacer(modifier = Modifier.height(12.dp))

    Text("Ticket price cap", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))

    uiData.museumTicketPriceBounds?.let { bounds ->
        PriceSliderFilterRow(
            label = "Ticket",
            bounds = bounds,
            selectedCap = filters.museumTicketPriceCap,
            onCapChange = { cap ->
                val isAtMax = cap != null && cap >= bounds.max - 0.0001
                onFiltersChange(
                    filters.copy(museumTicketPriceCap = if (isAtMax) null else cap)
                )
            }
        )
    } ?: Text(
        text = "No ticket price data available yet.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(20.dp))
}
