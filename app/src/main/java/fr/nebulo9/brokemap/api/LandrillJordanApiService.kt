package fr.nebulo9.brokemap.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val access_token: String)

data class User(val id: Int, val username: String)

data class Business(
    val id: Int,
    val name: String,
    val food_type: String,
    val latitude: Double,
    val longitude: Double,
    val city: String?,
    val phoneNumber: String?,
    val website: String?
)
interface LandrillJordanApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("businesses/")
    suspend fun getBusinesses(): List<Business>
}

