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
    // Key: item name, Value: max accepted price for this item
    val fastfoodItemPriceCaps: Map<String, Double> = emptyMap(),

    // Bar
    val barTerrace: Boolean = false,
    // Key: alcohol name, Value: max accepted price for this alcohol
    val barAlcoholPriceCaps: Map<String, Double> = emptyMap(),

    // Dancing bar
    // Key: alcohol name, Value: max accepted price for this alcohol
    val dancingBarAlcoholPriceCaps: Map<String, Double> = emptyMap(),

    // Museum
    // null => MAX (no ticket price cap)
    val museumTicketPriceCap: Double? = null,

    // Bench
    // nothing specific yet
)
