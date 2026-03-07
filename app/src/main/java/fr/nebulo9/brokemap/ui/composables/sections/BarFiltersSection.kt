package fr.nebulo9.brokemap.ui.composables.sections

import androidx.compose.foundation.layout.Arrangement
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
fun BarFiltersSection(
    filters: SelectedFilters,
    onFiltersChange: (SelectedFilters) -> Unit
) {
    val alcoholOptions = listOf("Beer", "Wine", "Vodka", "Whisky", "Cocktail")

    Spacer(modifier = Modifier.height(12.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipButton(
            label = "Terrace",
            selected = filters.barTerrace,
            onClick = {
                onFiltersChange(
                    filters.copy(barTerrace = !filters.barTerrace)
                )
            }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    Text("Alcohols", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        alcoholOptions.forEach { alcohol ->
            val selected = alcohol in filters.barAlcohols
            FilterChipButton(
                label = alcohol,
                selected = selected,
                onClick = {
                    val newSet =
                        if (selected) filters.barAlcohols - alcohol
                        else filters.barAlcohols + alcohol

                    onFiltersChange(filters.copy(barAlcohols = newSet))
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}