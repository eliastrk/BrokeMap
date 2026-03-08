package fr.nebulo9.brokemap.ui.composables.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import fr.nebulo9.brokemap.R
import fr.nebulo9.brokemap.models.BusinessViewModel
import fr.nebulo9.brokemap.models.LocationViewModel
import fr.nebulo9.brokemap.ui.composables.BitmapFromVector
import fr.nebulo9.brokemap.ui.composables.buttons.FilterButton
import fr.nebulo9.brokemap.ui.composables.sections.BusinessDetailsCache
import fr.nebulo9.brokemap.ui.composables.sections.BusinessDetailsSheet
import fr.nebulo9.brokemap.ui.composables.sections.BusinessFilterEngine
import fr.nebulo9.brokemap.ui.composables.sections.FilterUiDataFactory
import fr.nebulo9.brokemap.ui.composables.sections.FilterSection
import kotlinx.coroutines.delay
import fr.nebulo9.brokemap.ui.composables.sections.SelectedFilters
import fr.nebulo9.brokemap.api.Business

/***
 * Main Composable of the BrokeMap app containing displaying a Google Maps composable.
 */
@Composable
fun MapScreen() {
    val context = LocalContext.current

    val businessViewModel: BusinessViewModel = viewModel { BusinessViewModel(context) }

    val businesses by businessViewModel.businesses.collectAsState()
    val restaurantDetailsById by businessViewModel.restaurantDetailsById.collectAsState()
    val fastfoodDetailsById by businessViewModel.fastfoodDetailsById.collectAsState()
    val barDetailsById by businessViewModel.barDetailsById.collectAsState()
    val museumDetailsById by businessViewModel.museumDetailsById.collectAsState()
    val isLoading by businessViewModel.isLoading.collectAsState()
    val error by businessViewModel.error.collectAsState()
    var hasLoadedBusinesses by remember { mutableStateOf(false) }

    var showFilter by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf(SelectedFilters()) }
    var selectedBusiness by remember { mutableStateOf<Business?>(null) }

    val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_hide)

    val viewModel = remember { LocationViewModel(context) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        viewModel.setPermissionGranted(granted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val location by viewModel.location.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(50.6292, 3.0573), // Lille
            12f
        )
    }
    var currentZoom by remember { mutableStateOf(13f) }
    var currentRadius by remember { mutableStateOf(5.0) }
    fun getCurrentRadius(zoom: Float): Double {
        return when {
            zoom >= 18f -> 0.5   // Very zoomed in: 500m
            zoom >= 16f -> 1.0   // Zoomed in: 1km
            zoom >= 14f -> 2.5   // Medium: 2.5km
            zoom >= 12f -> 5.0   // Normal: 5km
            zoom >= 10f -> 10.0  // Zoomed out: 10km
            zoom >= 8f -> 25.0   // Very zoomed out: 25km
            else -> 50.0         // Extremely zoomed out: 50km
        }
    }

    LaunchedEffect(location) {
        if (location != null && !hasLoadedBusinesses) {
            businessViewModel.loadAllBusinesses()
            hasLoadedBusinesses = true
        }
        location?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(location!!.latitude, location!!.longitude),10f)
        }
    }

    LaunchedEffect(businesses) {
        businessViewModel.preloadDetailsForBusinesses(businesses)
    }


    LaunchedEffect(cameraPositionState.position.zoom) {
        val newZoomValue = cameraPositionState.position.zoom
        val newRadiusValue = getCurrentRadius(newZoomValue)

        if (kotlin.math.abs(newZoomValue - currentZoom) > 1f || newRadiusValue != currentRadius) {
            currentZoom = newZoomValue
            currentRadius = newRadiusValue

            delay(500)

            if (cameraPositionState.position.zoom == newZoomValue) {
                val center = cameraPositionState.position.target

            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        val detailsCache = BusinessDetailsCache(
            restaurantsById = restaurantDetailsById,
            fastfoodsById = fastfoodDetailsById,
            barsById = barDetailsById,
            museumsById = museumDetailsById
        )
        val filteredBusinesses = BusinessFilterEngine.filterBusinesses(
            businesses = businesses,
            filters = filters,
            details = detailsCache
        )
        val filterUiData = FilterUiDataFactory.build(
            businesses = businesses,
            details = detailsCache
        )

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapStyleOptions = mapStyleOptions, isMyLocationEnabled = true),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true
            )
        ) {
            filteredBusinesses.forEach { business ->
                    if (business.latitude != null && business.longitude != null) {
                        AdvancedMarker(
                            state = MarkerState(
                                position = LatLng(business.latitude, business.longitude)
                            ),
                            title = business.name,
                            snippet = business.city,
                            onClick = {
                                selectedBusiness = business
                                true
                            },
                            icon = when (business.type_name) {
                                "bar" -> BitmapFromVector(LocalContext.current, R.drawable.beer)
                                "restaurant" -> BitmapFromVector(LocalContext.current, R.drawable.fork_spoon)
                                "fastfood" -> BitmapFromVector(LocalContext.current, R.drawable.fastfood)
                                else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                            }
                        )
                    }
                }
        }

        FilterButton(
            onClick = { showFilter = true },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        if (showFilter) {
            FilterSection(
                filters = filters,
                uiData = filterUiData,
                onFiltersChange = { filters = it },
                onDismiss = { showFilter = false }
            )
        }

        selectedBusiness?.let { business ->
            BusinessDetailsSheet(
                business = business,
                details = detailsCache,
                onDismiss = { selectedBusiness = null }
            )
        }

    }
}
