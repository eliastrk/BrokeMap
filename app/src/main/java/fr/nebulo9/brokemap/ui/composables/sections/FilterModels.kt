package fr.nebulo9.brokemap.ui.composables.sections

data class SelectedFilters(
    val selectedTypes: Set<String> = emptySet(),

    // Restaurant
    val restaurantStudentDiscount: Boolean = false,
    val restaurantTerrace: Boolean = false,
    val restaurantAveragePrices: Set<String> = emptySet(),
    val restaurantFoodTypes: Set<String> = emptySet(),

    // Fastfood
    val fastfoodStudentDiscount: Boolean = false,
    val fastfoodTerrace: Boolean = false,
    val fastfoodItems: Set<String> = emptySet(),

    // Bar
    val barTerrace: Boolean = false,
    val barAlcohols: Set<String> = emptySet(),

    // Dancing bar
    // nothing specific yet

    // Museum
    val museumFreeOnly: Boolean = false,

    // Bench
    // nothing specific yet
)