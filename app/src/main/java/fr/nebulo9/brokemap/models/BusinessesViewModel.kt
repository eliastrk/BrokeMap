package fr.nebulo9.brokemap.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.nebulo9.brokemap.api.Business
import fr.nebulo9.brokemap.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BusinessViewModel(context: Context) : ViewModel() {

    private val api = RetrofitClient.apiService

    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses: StateFlow<List<Business>> = _businesses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

//    fun loadNearbyBusinesses(latitude: Double, longitude: Double, radius: Double = 5.0) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _error.value = null
//
//            try {
//                Log.d("BusinessViewModel", "Loading businesses near ($latitude, $longitude) within ${radius}km")
//
//                val result = api.getNearbyBusinesses(latitude, longitude, radius)
//                _businesses.value = result
//
//                Log.d("BusinessViewModel", "✓ Loaded ${result.size} businesses")
//
//            } catch (e: Exception) {
//                Log.e("BusinessViewModel", "✗ Error loading businesses: ${e.message}", e)
//                _error.value = "Failed to load businesses: ${e.message}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

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