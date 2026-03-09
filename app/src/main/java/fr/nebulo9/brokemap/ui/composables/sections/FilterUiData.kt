package fr.nebulo9.brokemap.ui.composables.sections

data class PriceBounds(
    val min: Double,
    val max: Double
)

data class FilterUiData(
    val restaurantFoodTypes: List<String> = emptyList(),
    val restaurantAveragePrices: List<String> = emptyList(),
    val barAlcoholPriceBounds: Map<String, PriceBounds> = emptyMap(),
    val dancingBarAlcoholPriceBounds: Map<String, PriceBounds> = emptyMap(),
    val fastfoodItemPriceBounds: Map<String, PriceBounds> = emptyMap(),
    val museumTicketPriceBounds: PriceBounds? = null
)
