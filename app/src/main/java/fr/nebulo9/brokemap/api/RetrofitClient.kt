package fr.nebulo9.brokemap.api

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://landrill-jordan.nebulo9.fr/api/"

    private var tokenManager: LandrillJordanTokenManager? = null

    fun init(context: Context) {
        tokenManager = LandrillJordanTokenManager(context)
    }

    private val authInterceptor = Interceptor { chain ->
        val token = runBlocking { tokenManager?.getToken() }

        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        chain.proceed(request)
    }

    private val unauthorizedInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())

        if (response.code == 401) {
            // Token expired - clear it
            runBlocking { tokenManager?.clearToken() }
        }

        response
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(unauthorizedInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val apiService: LandrillJordanApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LandrillJordanApiService::class.java)
    }

    private fun createTempApiService(): LandrillJordanApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LandrillJordanApiService::class.java)
    }
}