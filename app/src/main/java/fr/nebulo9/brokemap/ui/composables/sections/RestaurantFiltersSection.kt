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
fun RestaurantFiltersSection(
    filters: SelectedFilters,
    onFiltersChange: (SelectedFilters) -> Unit
) {
    val averagePriceOptions = listOf("0-10 EUR", "10-20 EUR", "20-30 EUR")
    val foodTypeOptions = listOf("Lebanese", "Italian", "Burger", "Pizza", "Asian")

    Spacer(modifier = Modifier.height(12.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipButton(
            label = "Student discount",
            selected = filters.restaurantStudentDiscount,
            onClick = {
                onFiltersChange(
                    filters.copy(
                        restaurantStudentDiscount = !filters.restaurantStudentDiscount
                    )
                )
            }
        )

        FilterChipButton(
            label = "Terrace",
            selected = filters.restaurantTerrace,
            onClick = {
                onFiltersChange(
                    filters.copy(
                        restaurantTerrace = !filters.restaurantTerrace
                    )
                )
            }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    Text("Average price", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        averagePriceOptions.forEach { price ->
            val selected = price in filters.restaurantAveragePrices
            FilterChipButton(
                label = price,
                selected = selected,
                onClick = {
                    val newSet =
                        if (selected) filters.restaurantAveragePrices - price
                        else filters.restaurantAveragePrices + price

                    onFiltersChange(filters.copy(restaurantAveragePrices = newSet))
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
    Text("Food type", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        foodTypeOptions.forEach { food ->
            val selected = food in filters.restaurantFoodTypes
            FilterChipButton(
                label = food,
                selected = selected,
                onClick = {
                    val newSet =
                        if (selected) filters.restaurantFoodTypes - food
                        else filters.restaurantFoodTypes + food

                    onFiltersChange(filters.copy(restaurantFoodTypes = newSet))
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}