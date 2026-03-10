package fr.nebulo9.brokemap.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val access_token: String)

data class OpeningSchedule(
    @SerializedName(value = "day_of_week", alternate = ["day", "weekday", "day_name", "jour"])
    val dayOfWeek: String? = null,
    @SerializedName(value = "open_time", alternate = ["opening_time", "opens_at", "open", "opening"])
    val openTime: String? = null,
    @SerializedName(value = "close_time", alternate = ["closing_time", "closes_at", "close", "closing"])
    val closeTime: String? = null
)

data class Business(
    val id: Int,
    val name: String,
    val type_name: String,
    val latitude: Double?,
    val longitude: Double?,
    @SerializedName(value = "address", alternate = ["street", "street_address"])
    val address: String? = null,
    @SerializedName(value = "postal_code", alternate = ["postcode", "zip_code", "zipcode", "zip"])
    val postal_code: String? = null,
    val city: String?,
    @SerializedName(value = "country", alternate = ["country_name"])
    val country: String? = null,
    val phone: String?,
    val website: String?,
    val is_open: Boolean? = null,
    @SerializedName(value = "schedule", alternate = ["schedules", "opening_hours", "hours"])
    val schedule: List<OpeningSchedule>? = null,
    val distance: Double? = null
)

// Restaurant models
data class FoodType(
    val id: Int,
    val name: String
)

data class RestaurantDetail(
    val id: Int,
    val name: String,
    val latitude: Double?,
    val longitude: Double?,
    @SerializedName(value = "address", alternate = ["street", "street_address"])
    val address: String? = null,
    @SerializedName(value = "postal_code", alternate = ["postcode", "zip_code", "zipcode", "zip"])
    val postal_code: String? = null,
    val city: String?,
    @SerializedName(value = "country", alternate = ["country_name"])
    val country: String? = null,
    val phone: String?,
    val website: String?,
    val is_open: Boolean? = null,
    @SerializedName(value = "schedule", alternate = ["schedules", "opening_hours", "hours"])
    val schedule: List<OpeningSchedule>? = null,
    val distance: Double?,
    val student_discount: Boolean,
    val terrace: Boolean,
    val average_price: String,
    val food_types: List<FoodType>
)

// Bar models
data class Alcohol(
    val id: Int,
    val name: String,
    val price: Double
)

data class BarDetail(
    val id: Int,
    val name: String,
    val latitude: Double?,
    val longitude: Double?,
    @SerializedName(value = "address", alternate = ["street", "street_address"])
    val address: String? = null,
    @SerializedName(value = "postal_code", alternate = ["postcode", "zip_code", "zipcode", "zip"])
    val postal_code: String? = null,
    val city: String?,
    @SerializedName(value = "country", alternate = ["country_name"])
    val country: String? = null,
    val phone: String?,
    val website: String?,
    val is_open: Boolean? = null,
    @SerializedName(value = "schedule", alternate = ["schedules", "opening_hours", "hours"])
    val schedule: List<OpeningSchedule>? = null,
    val distance: Double?,
    val description: String?,
    val student_discount: Boolean? = null,
    val terrace: Boolean,
    val alcohols: List<Alcohol>
)

// Fastfood models
data class MenuItem(
    val id: Int,
    val name: String,
    val price: Double
)

data class FastfoodDetail(
    val id: Int,
    val name: String,
    val latitude: Double?,
    val longitude: Double?,
    @SerializedName(value = "address", alternate = ["street", "street_address"])
    val address: String? = null,
    @SerializedName(value = "postal_code", alternate = ["postcode", "zip_code", "zipcode", "zip"])
    val postal_code: String? = null,
    val city: String?,
    @SerializedName(value = "country", alternate = ["country_name"])
    val country: String? = null,
    val phone: String?,
    val website: String?,
    val is_open: Boolean? = null,
    @SerializedName(value = "schedule", alternate = ["schedules", "opening_hours", "hours"])
    val schedule: List<OpeningSchedule>? = null,
    val distance: Double?,
    val student_discount: Boolean,
    val terrace: Boolean,
    val items: List<MenuItem>
)

// Museum models
data class MuseumDetail(
    val id: Int,
    val name: String,
    val latitude: Double?,
    val longitude: Double?,
    @SerializedName(value = "address", alternate = ["street", "street_address"])
    val address: String? = null,
    @SerializedName(value = "postal_code", alternate = ["postcode", "zip_code", "zipcode", "zip"])
    val postal_code: String? = null,
    val city: String?,
    @SerializedName(value = "country", alternate = ["country_name"])
    val country: String? = null,
    val phone: String?,
    val website: String?,
    val is_open: Boolean? = null,
    @SerializedName(value = "schedule", alternate = ["schedules", "opening_hours", "hours"])
    val schedule: List<OpeningSchedule>? = null,
    val distance: Double?,
    val ticket_price: Double?,
    val student_discount: Boolean? = null
)

interface LandrillJordanApiService {

    // Auth
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // General businesses
    @GET("businesses/")
    suspend fun getBusinesses(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<Business>

    @GET("businesses/nearby")
    suspend fun getNearbyBusinesses(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double = 5.0,
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<Business>

    @GET("businesses/{id}")
    suspend fun getBusinessById(@Path("id") id: Int): Business

    // Restaurants
    @GET("restaurants/nearby")
    suspend fun getNearbyRestaurants(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double = 5.0,
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<RestaurantDetail>

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: Int): RestaurantDetail

    // Bars
    @GET("bars/nearby")
    suspend fun getNearbyBars(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double = 5.0,
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<BarDetail>

    @GET("bars/{id}")
    suspend fun getBarById(@Path("id") id: Int): BarDetail

    // Fastfoods
    @GET("fastfoods/nearby")
    suspend fun getNearbyFastfoods(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double = 5.0,
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<FastfoodDetail>

    @GET("fastfoods/{id}")
    suspend fun getFastfoodById(@Path("id") id: Int): FastfoodDetail

    // Museums
    @GET("museums/nearby")
    suspend fun getNearbyMuseums(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double = 5.0,
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<MuseumDetail>

    @GET("museums/{id}")
    suspend fun getMuseumById(@Path("id") id: Int): MuseumDetail
}
