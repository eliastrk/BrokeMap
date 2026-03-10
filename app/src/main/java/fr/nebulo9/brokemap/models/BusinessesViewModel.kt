package fr.nebulo9.brokemap.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.nebulo9.brokemap.api.BarDetail
import fr.nebulo9.brokemap.api.Business
import fr.nebulo9.brokemap.api.FastfoodDetail
import fr.nebulo9.brokemap.api.MuseumDetail
import fr.nebulo9.brokemap.api.RestaurantDetail
import fr.nebulo9.brokemap.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class BusinessType {
    ALL, RESTAURANT, BAR, FASTFOOD, MUSEUM
}

class BusinessViewModel(context: Context) : ViewModel() {

    private val api = RetrofitClient.apiService

    // General businesses
    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses: StateFlow<List<Business>> = _businesses.asStateFlow()

    // Type-specific data
    private val _restaurants = MutableStateFlow<List<RestaurantDetail>>(emptyList())
    val restaurants: StateFlow<List<RestaurantDetail>> = _restaurants.asStateFlow()
    private val _restaurantDetailsById = MutableStateFlow<Map<Int, RestaurantDetail>>(emptyMap())
    val restaurantDetailsById: StateFlow<Map<Int, RestaurantDetail>> = _restaurantDetailsById.asStateFlow()

    private val _bars = MutableStateFlow<List<BarDetail>>(emptyList())
    val bars: StateFlow<List<BarDetail>> = _bars.asStateFlow()
    private val _barDetailsById = MutableStateFlow<Map<Int, BarDetail>>(emptyMap())
    val barDetailsById: StateFlow<Map<Int, BarDetail>> = _barDetailsById.asStateFlow()

    private val _fastfoods = MutableStateFlow<List<FastfoodDetail>>(emptyList())
    val fastfoods: StateFlow<List<FastfoodDetail>> = _fastfoods.asStateFlow()
    private val _fastfoodDetailsById = MutableStateFlow<Map<Int, FastfoodDetail>>(emptyMap())
    val fastfoodDetailsById: StateFlow<Map<Int, FastfoodDetail>> = _fastfoodDetailsById.asStateFlow()

    private val _museums = MutableStateFlow<List<MuseumDetail>>(emptyList())
    val museums: StateFlow<List<MuseumDetail>> = _museums.asStateFlow()
    private val _museumDetailsById = MutableStateFlow<Map<Int, MuseumDetail>>(emptyMap())
    val museumDetailsById: StateFlow<Map<Int, MuseumDetail>> = _museumDetailsById.asStateFlow()

    // UI state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedType = MutableStateFlow(BusinessType.ALL)
    val selectedType: StateFlow<BusinessType> = _selectedType.asStateFlow()

    fun loadNearbyBusinesses(
        latitude: Double,
        longitude: Double,
        radius: Double = 5.0,
        type: BusinessType = BusinessType.ALL
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _selectedType.value = type

            try {
                when (type) {
                    BusinessType.ALL -> {
                        Log.d("BusinessViewModel", "Loading all businesses")
                        val result = api.getNearbyBusinesses(latitude, longitude, radius)
                        _businesses.value = result
                        Log.d("BusinessViewModel", "✓ Loaded ${result.size} businesses")
                    }

                    BusinessType.RESTAURANT -> {
                        Log.d("BusinessViewModel", "Loading restaurants")
                        val result = api.getNearbyRestaurants(latitude, longitude, radius)
                        _restaurants.value = result
                        Log.d("BusinessViewModel", "✓ Loaded ${result.size} restaurants")
                    }

                    BusinessType.BAR -> {
                        Log.d("BusinessViewModel", "Loading bars")
                        val result = api.getNearbyBars(latitude, longitude, radius)
                        _bars.value = result
                        Log.d("BusinessViewModel", "✓ Loaded ${result.size} bars")
                    }

                    BusinessType.FASTFOOD -> {
                        Log.d("BusinessViewModel", "Loading fastfoods")
                        val result = api.getNearbyFastfoods(latitude, longitude, radius)
                        _fastfoods.value = result
                        Log.d("BusinessViewModel", "✓ Loaded ${result.size} fastfoods")
                    }

                    BusinessType.MUSEUM -> {
                        Log.d("BusinessViewModel", "Loading museums")
                        val result = api.getNearbyMuseums(latitude, longitude, radius)
                        _museums.value = result
                        Log.d("BusinessViewModel", "✓ Loaded ${result.size} museums")
                    }
                }

            } catch (e: Exception) {
                Log.e("BusinessViewModel", "✗ Error loading: ${e.message}", e)
                _error.value = "Failed to load: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Get detail by ID
    suspend fun getRestaurantDetail(id: Int): RestaurantDetail? {
        return try {
            api.getRestaurantById(id)
        } catch (e: Exception) {
            Log.e("BusinessViewModel", "Error getting restaurant: ${e.message}")
            null
        }
    }

    fun preloadRestaurantDetails(ids: Set<Int>) {
        if (ids.isEmpty()) return

        viewModelScope.launch {
            val currentDetails = _restaurantDetailsById.value
            val missingIds = ids.filterNot { it in currentDetails.keys }

            if (missingIds.isEmpty()) return@launch

            val fetchedDetails = mutableMapOf<Int, RestaurantDetail>()

            missingIds.forEach { id ->
                val detail = getRestaurantDetail(id)
                if (detail != null) {
                    fetchedDetails[id] = detail
                }
            }

            if (fetchedDetails.isNotEmpty()) {
                _restaurantDetailsById.value = _restaurantDetailsById.value + fetchedDetails
            }
        }
    }

    fun preloadDetailsForBusinesses(businesses: List<Business>) {
        if (businesses.isEmpty()) return

        preloadRestaurantDetails(
            businesses
                .asSequence()
                .filter { it.type_name == "restaurant" }
                .map { it.id }
                .toSet()
        )
        preloadBarDetails(
            businesses
                .asSequence()
                .filter { it.type_name == "bar" || it.type_name == "dancing_bar" }
                .map { it.id }
                .toSet()
        )
        preloadFastfoodDetails(
            businesses
                .asSequence()
                .filter { it.type_name == "fastfood" }
                .map { it.id }
                .toSet()
        )
        preloadMuseumDetails(
            businesses
                .asSequence()
                .filter { it.type_name == "museum" }
                .map { it.id }
                .toSet()
        )
    }

    suspend fun getBarDetail(id: Int): BarDetail? {
        return try {
            api.getBarById(id)
        } catch (e: Exception) {
            Log.e("BusinessViewModel", "Error getting bar: ${e.message}")
            null
        }
    }

    fun preloadBarDetails(ids: Set<Int>) {
        if (ids.isEmpty()) return

        viewModelScope.launch {
            val currentDetails = _barDetailsById.value
            val missingIds = ids.filterNot { it in currentDetails.keys }

            if (missingIds.isEmpty()) return@launch

            val fetchedDetails = mutableMapOf<Int, BarDetail>()

            missingIds.forEach { id ->
                val detail = getBarDetail(id)
                if (detail != null) {
                    fetchedDetails[id] = detail
                }
            }

            if (fetchedDetails.isNotEmpty()) {
                _barDetailsById.value = _barDetailsById.value + fetchedDetails
            }
        }
    }

    suspend fun getFastfoodDetail(id: Int): FastfoodDetail? {
        return try {
            api.getFastfoodById(id)
        } catch (e: Exception) {
            Log.e("BusinessViewModel", "Error getting fastfood: ${e.message}")
            null
        }
    }

    fun preloadFastfoodDetails(ids: Set<Int>) {
        if (ids.isEmpty()) return

        viewModelScope.launch {
            val currentDetails = _fastfoodDetailsById.value
            val missingIds = ids.filterNot { it in currentDetails.keys }

            if (missingIds.isEmpty()) return@launch

            val fetchedDetails = mutableMapOf<Int, FastfoodDetail>()

            missingIds.forEach { id ->
                val detail = getFastfoodDetail(id)
                if (detail != null) {
                    fetchedDetails[id] = detail
                }
            }

            if (fetchedDetails.isNotEmpty()) {
                _fastfoodDetailsById.value = _fastfoodDetailsById.value + fetchedDetails
            }
        }
    }

    suspend fun getMuseumDetail(id: Int): MuseumDetail? {
        return try {
            api.getMuseumById(id)
        } catch (e: Exception) {
            Log.e("BusinessViewModel", "Error getting museum: ${e.message}")
            null
        }
    }

    fun preloadMuseumDetails(ids: Set<Int>) {
        if (ids.isEmpty()) return

        viewModelScope.launch {
            val currentDetails = _museumDetailsById.value
            val missingIds = ids.filterNot { it in currentDetails.keys }

            if (missingIds.isEmpty()) return@launch

            val fetchedDetails = mutableMapOf<Int, MuseumDetail>()

            missingIds.forEach { id ->
                val detail = getMuseumDetail(id)
                if (detail != null) {
                    fetchedDetails[id] = detail
                }
            }

            if (fetchedDetails.isNotEmpty()) {
                _museumDetailsById.value = _museumDetailsById.value + fetchedDetails
            }
        }
    }

    fun loadAllBusinesses() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                Log.d("BusinessViewModel", "Loading all businesses")

                val result = api.getBusinesses()
                _businesses.value = result

                Log.d("BusinessViewModel", "✓ Loaded ${result.size} businesses")

            } catch (e: Exception) {
                Log.e("BusinessViewModel", "✗ Error loading businesses: ${e.message}", e)
                _error.value = "Failed to load businesses: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
