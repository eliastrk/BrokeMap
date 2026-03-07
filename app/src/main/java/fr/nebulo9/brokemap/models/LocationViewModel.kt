package fr.nebulo9.brokemap.models

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.nebulo9.brokemap.services.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * The view model to ensure permissions to access the location are granted and
 * storing the obtained location.
 */
class LocationViewModel(context: Context): ViewModel() {
    private val locationService = LocationService(context)

    private val mutableLocation = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = mutableLocation.asStateFlow()

    private val mutablePermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = mutablePermission.asStateFlow()

    fun setPermissionGranted(granted: Boolean) {
        mutablePermission.value = granted
        if (granted) startLocationUpdates()
    }

    fun startLocationUpdates() {
        viewModelScope.launch {
            locationService.getLocationUpdates()
                .catch { e -> e.printStackTrace() }
                .collect { mutableLocation.value = it }
        }
    }
}