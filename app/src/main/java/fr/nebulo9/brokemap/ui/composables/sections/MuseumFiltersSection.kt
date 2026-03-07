package fr.nebulo9.brokemap.ui.composables.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.nebulo9.brokemap.ui.composables.buttons.FilterChipButton

@Composable
fun MuseumFiltersSection(
    filters: SelectedFilters,
    onFiltersChange: (SelectedFilters) -> Unit
) {

    Spacer(modifier = Modifier.height(12.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipButton(
            label = "Free only",
            selected = filters.museumFreeOnly,
            onClick = {
                onFiltersChange(
                    filters.copy(museumFreeOnly = !filters.museumFreeOnly)
                )
            }
        )
    }

    Spacer(modifier = Modifier.height(20.dp))
}