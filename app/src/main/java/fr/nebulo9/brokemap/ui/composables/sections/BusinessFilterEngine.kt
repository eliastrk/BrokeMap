package fr.nebulo9.brokemap.ui.composables.sections

import fr.nebulo9.brokemap.api.BarDetail
import fr.nebulo9.brokemap.api.Business
import fr.nebulo9.brokemap.api.FastfoodDetail
import fr.nebulo9.brokemap.api.MuseumDetail
import fr.nebulo9.brokemap.api.RestaurantDetail

data class BusinessDetailsCache(
    val restaurantsById: Map<Int, RestaurantDetail> = emptyMap(),
    val fastfoodsById: Map<Int, FastfoodDetail> = emptyMap(),
    val barsById: Map<Int, BarDetail> = emptyMap(),
    val museumsById: Map<Int, MuseumDetail> = emptyMap()
)

object BusinessFilterEngine {

    fun filterBusinesses(
        businesses: List<Business>,
        filters: SelectedFilters,
        details: BusinessDetailsCache
    ): List<Business> {
        return businesses
            .asSequence()
            .filter { matchesSelectedTypes(it, filters) }
            .filter { matchesSubFilters(it, filters, details) }
            .toList()
    }

    private fun matchesSelectedTypes(business: Business, filters: SelectedFilters): Boolean {
        return filters.selectedTypes.isEmpty() || business.type_name in filters.selectedTypes
    }

    private fun matchesSubFilters(
        business: Business,
        filters: SelectedFilters,
        details: BusinessDetailsCache
    ): Boolean {
        return when (business.type_name) {
            "restaurant" -> matchesRestaurantFilters(
                restaurant = details.restaurantsById[business.id],
                filters = filters
            )

            "fastfood" -> matchesFastfoodFilters(
                fastfood = details.fastfoodsById[business.id],
                filters = filters
            )

            "bar" -> matchesBarFilters(
                bar = details.barsById[business.id],
                filters = filters
            )

            "dancing_bar" -> matchesDancingBarFilters(
                dancingBar = details.barsById[business.id],
                filters = filters
            )

            "museum" -> matchesMuseumFilters(
                museum = details.museumsById[business.id],
                filters = filters
            )

            // No sub-filters for now.
            "bench" -> true
            else -> true
        }
    }

    private fun matchesRestaurantFilters(
        restaurant: RestaurantDetail?,
        filters: SelectedFilters
    ): Boolean {
        val hasSubFilter =
            filters.restaurantStudentDiscount ||
                filters.restaurantTerrace ||
                filters.restaurantAveragePrices.isNotEmpty() ||
                filters.restaurantFoodTypes.isNotEmpty()

        if (!hasSubFilter) return true
        restaurant ?: return false

        if (filters.restaurantStudentDiscount && !restaurant.student_discount) return false
        if (filters.restaurantTerrace && !restaurant.terrace) return false

        if (filters.restaurantAveragePrices.isNotEmpty()) {
            val priceMatch = matchesAveragePriceFilter(
                apiPrice = restaurant.average_price,
                selectedPrices = filters.restaurantAveragePrices
            )
            if (!priceMatch) return false
        }

        if (filters.restaurantFoodTypes.isNotEmpty()) {
            val restaurantFoodTypes = restaurant.food_types.map { normalizeText(it.name) }.toSet()
            val expectedFoodTypes = filters.restaurantFoodTypes.map { normalizeText(it) }.toSet()
            if (restaurantFoodTypes.intersect(expectedFoodTypes).isEmpty()) return false
        }

        return true
    }

    private fun matchesFastfoodFilters(
        fastfood: FastfoodDetail?,
        filters: SelectedFilters
    ): Boolean {
        val hasSubFilter =
            filters.fastfoodStudentDiscount ||
                filters.fastfoodTerrace ||
                filters.fastfoodItemPriceCaps.isNotEmpty()

        if (!hasSubFilter) return true
        fastfood ?: return false

        if (filters.fastfoodStudentDiscount && !fastfood.student_discount) return false
        if (filters.fastfoodTerrace && !fastfood.terrace) return false

        if (filters.fastfoodItemPriceCaps.isNotEmpty()) {
            val itemsByName = fastfood.items.groupBy { normalizeText(it.name) }
            val allCapsMatched = filters.fastfoodItemPriceCaps.all { (itemName, cap) ->
                val matchingItems = itemsByName[normalizeText(itemName)] ?: return@all false
                matchingItems.any { it.price <= cap }
            }
            if (!allCapsMatched) return false
        }

        return true
    }

    private fun matchesBarFilters(
        bar: BarDetail?,
        filters: SelectedFilters
    ): Boolean {
        val hasSubFilter =
            filters.barTerrace || filters.barAlcoholPriceCaps.isNotEmpty()

        if (!hasSubFilter) return true
        bar ?: return false

        if (filters.barTerrace && !bar.terrace) return false

        if (filters.barAlcoholPriceCaps.isNotEmpty()) {
            val alcoholsByName = bar.alcohols.groupBy { normalizeText(it.name) }
            val allCapsMatched = filters.barAlcoholPriceCaps.all { (alcoholName, cap) ->
                val matchingAlcohols = alcoholsByName[normalizeText(alcoholName)] ?: return@all false
                matchingAlcohols.any { it.price <= cap }
            }
            if (!allCapsMatched) return false
        }

        return true
    }

    private fun matchesDancingBarFilters(
        dancingBar: BarDetail?,
        filters: SelectedFilters
    ): Boolean {
        if (filters.dancingBarAlcoholPriceCaps.isEmpty()) return true
        dancingBar ?: return false

        val alcoholsByName = dancingBar.alcohols.groupBy { normalizeText(it.name) }
        val allCapsMatched = filters.dancingBarAlcoholPriceCaps.all { (alcoholName, cap) ->
            val matchingAlcohols = alcoholsByName[normalizeText(alcoholName)] ?: return@all false
            matchingAlcohols.any { it.price <= cap }
        }
        return allCapsMatched
    }

    private fun matchesMuseumFilters(
        museum: MuseumDetail?,
        filters: SelectedFilters
    ): Boolean {
        val cap = filters.museumTicketPriceCap ?: return true
        museum ?: return false
        val ticketPrice = museum.ticket_price ?: 0.0
        return ticketPrice <= cap
    }

    private fun normalizeText(value: String): String {
        return value.trim().lowercase()
    }

    private fun matchesAveragePriceFilter(
        apiPrice: String,
        selectedPrices: Set<String>
    ): Boolean {
        val normalizedApiPrice = normalizeText(apiPrice)
        if (selectedPrices.any { normalizeText(it) == normalizedApiPrice }) return true

        val apiRange = extractPriceRange(apiPrice) ?: return false
        return selectedPrices.any { selected ->
            val selectedRange = extractPriceRange(selected) ?: return@any false
            rangesOverlap(apiRange, selectedRange)
        }
    }

    private fun extractPriceRange(raw: String): Pair<Double, Double>? {
        val numbers = Regex("""\d+(?:[.,]\d+)?""")
            .findAll(raw)
            .mapNotNull { it.value.replace(',', '.').toDoubleOrNull() }
            .toList()

        if (numbers.isEmpty()) return null
        if (numbers.size == 1) return numbers[0] to numbers[0]

        val low = minOf(numbers[0], numbers[1])
        val high = maxOf(numbers[0], numbers[1])
        return low to high
    }

    private fun rangesOverlap(a: Pair<Double, Double>, b: Pair<Double, Double>): Boolean {
        return a.first <= b.second && b.first <= a.second
    }
}
