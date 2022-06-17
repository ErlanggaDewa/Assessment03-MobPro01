package org.d3if1053.hitungzakatpenghasilan.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if1053.hitungzakatpenghasilan.model.GoldModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://pluang.com/api/asset/gold/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GoldApiService {
    @GET("pricing?daysLimit=0")
    suspend fun getPrice(): GoldModel
}

object GoldApi {
    val service: GoldApiService by lazy {
        retrofit.create(GoldApiService::class.java)
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }
