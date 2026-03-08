package fr.nebulo9.brokemap.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val access_token: String)

data class Business(
    val id: Int,
    val name: String,
    val type_name: String,
    val latitude: Double?,
    val longitude: Double?,
    val city: String?,
    val phone: String?,
    val website: String?,
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
    val city: String?,
    val phone: String?,
    val website: String?,
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
    val city: String?,
    val phone: String?,
    val website: String?,
    val distance: Double?,
    val description: String?,
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
    val city: String?,
    val phone: String?,
    val website: String?,
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
    val city: String?,
    val phone: String?,
    val website: String?,
    val distance: Double?,
    val ticket_price: Double?
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
