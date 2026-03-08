package fr.nebulo9.brokemap.ui.composables.sections

import fr.nebulo9.brokemap.api.BarDetail
import fr.nebulo9.brokemap.api.Business
import fr.nebulo9.brokemap.api.FastfoodDetail
import fr.nebulo9.brokemap.api.MuseumDetail
import fr.nebulo9.brokemap.api.RestaurantDetail

object FilterUiDataFactory {

    fun build(
        businesses: List<Business>,
        details: BusinessDetailsCache
    ): FilterUiData {
        val restaurantDetails = businesses
            .asSequence()
            .filter { it.type_name == "restaurant" }
            .mapNotNull { details.restaurantsById[it.id] }
            .toList()

        val barDetails = businesses
            .asSequence()
            .filter { it.type_name == "bar" }
            .mapNotNull { details.barsById[it.id] }
            .toList()

        val dancingBarDetails = businesses
            .asSequence()
            .filter { it.type_name == "dancing_bar" }
            .mapNotNull { details.barsById[it.id] }
            .toList()

        val fastfoodDetails = businesses
            .asSequence()
            .filter { it.type_name == "fastfood" }
            .mapNotNull { details.fastfoodsById[it.id] }
            .toList()

        val museumDetails = businesses
            .asSequence()
            .filter { it.type_name == "museum" }
            .mapNotNull { details.museumsById[it.id] }
            .toList()

        return FilterUiData(
            restaurantFoodTypes = restaurantFoodTypes(restaurantDetails),
            restaurantAveragePrices = restaurantAveragePrices(restaurantDetails),
            barAlcoholPriceBounds = alcoholPriceBounds(barDetails),
            dancingBarAlcoholPriceBounds = alcoholPriceBounds(dancingBarDetails),
            fastfoodItemPriceBounds = fastfoodItemPriceBounds(fastfoodDetails),
            museumTicketPriceBounds = museumTicketPriceBounds(museumDetails)
        )
    }

    private fun restaurantFoodTypes(restaurants: List<RestaurantDetail>): List<String> {
        return restaurants
            .flatMap { it.food_types }
            .map { it.name.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }
            .sortedBy { it.lowercase() }
    }

    private fun restaurantAveragePrices(restaurants: List<RestaurantDetail>): List<String> {
        return restaurants
            .map { it.average_price.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }
            .sortedWith(compareBy({ extractFirstNumber(it) ?: Double.MAX_VALUE }, { it.lowercase() }))
    }

    private fun alcoholPriceBounds(bars: List<BarDetail>): Map<String, PriceBounds> {
        val grouped = bars
            .flatMap { it.alcohols }
            .groupBy { it.name.trim() }

        return grouped
            .filterKeys { it.isNotBlank() }
            .mapValues { (_, alcohols) ->
                val prices = alcohols.map { it.price }
                PriceBounds(
                    min = prices.minOrNull() ?: 0.0,
                    max = prices.maxOrNull() ?: 0.0
                )
            }
            .filterValues { it.max >= it.min }
    }

    private fun fastfoodItemPriceBounds(fastfoods: List<FastfoodDetail>): Map<String, PriceBounds> {
        val grouped = fastfoods
            .flatMap { it.items }
            .groupBy { it.name.trim() }

        return grouped
            .filterKeys { it.isNotBlank() }
            .mapValues { (_, items) ->
                val prices = items.map { it.price }
                PriceBounds(
                    min = prices.minOrNull() ?: 0.0,
                    max = prices.maxOrNull() ?: 0.0
                )
            }
            .filterValues { it.max >= it.min }
    }

    private fun museumTicketPriceBounds(museums: List<MuseumDetail>): PriceBounds? {
        val prices = museums.map { it.ticket_price ?: 0.0 }
        if (prices.isEmpty()) return null
        return PriceBounds(
            min = prices.minOrNull() ?: 0.0,
            max = prices.maxOrNull() ?: 0.0
        )
    }

    private fun extractFirstNumber(raw: String): Double? {
        return Regex("""\d+(?:[.,]\d+)?""")
            .find(raw)
            ?.value
            ?.replace(',', '.')
            ?.toDoubleOrNull()
    }
}
