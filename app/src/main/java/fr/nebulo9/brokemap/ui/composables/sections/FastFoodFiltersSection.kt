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
fun FastFoodFiltersSection(
    filters: SelectedFilters,
    onFiltersChange: (SelectedFilters) -> Unit
) {
    val itemOptions = listOf("Burger", "Fries", "Wrap", "Tacos", "Pizza Slice")

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
    Text("Items", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemOptions.forEach { item ->
            val selected = item in filters.fastfoodItems
            FilterChipButton(
                label = item,
                selected = selected,
                onClick = {
                    val newSet =
                        if (selected) filters.fastfoodItems - item
                        else filters.fastfoodItems + item

                    onFiltersChange(filters.copy(fastfoodItems = newSet))
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}