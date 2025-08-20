package com.cryptointel

import com.cryptointel.model.AddressProfile
import com.cryptointel.model.SignalResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("address/{chain}/{addr}/profile")
    suspend fun getProfile(
        @Path("chain") chain: String,
        @Path("addr") addr: String
    ): AddressProfile

    @GET("signals/{asset}")
    suspend fun getSignal(@Path("asset") asset: String): SignalResponse

    companion object {
        fun create(baseUrl: String): ApiService {
            val log = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(log)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            val r = Retrofit.Builder()
                .baseUrl(if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return r.create(ApiService::class.java)
        }
    }
}