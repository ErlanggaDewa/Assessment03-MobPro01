package org.d3if1053.hitungzakatpenghasilan.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if1053.hitungzakatpenghasilan.model.UserModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.github.com/users/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface UserApiService {
    @GET("erlanggadewa")
    suspend fun getUser(): UserModel
}

object UserApi {
    val service: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}

