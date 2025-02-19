package ru.korostylev.easycalories.api

import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import ru.korostylev.easycalories.dto.FoodItemFromDB

private const val BASE_URL = "https://easycalories.ru"
//private const val BASE_URL = "http://10.0.2.2:8000/"
//private const val BASE_URL = "https://easycalories.na4u.ru"

class TokenIntercepter: Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val newRequest = chain.request().newBuilder()
//                server easycalories token
            .addHeader("Authorization", "Token 85b8e4c07d1d75d43937d066591e35d5f5439d38")
//                local token
//            .addHeader("Authorization", "Token 0c5ec45eb189f14d0e748ba4fce532ac692c23bc")
//          na4.ru token
//            .addHeader("Authorization", "Token 7a22c7ce4ed220aa5d77d9a16adc9ec3c47ff19b")
            .build()
        return chain.proceed(newRequest)
    }
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(TokenIntercepter())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface APIService {
    @GET("foods")
    suspend fun getAll(): Response<List<FoodItemFromDB>>

    @POST("/foods/")
    suspend fun save(@Body foodItemFromDB: FoodItemFromDB): Response<FoodItemFromDB>

    @DELETE("/foods/{id}/")
    suspend fun removeFoodById(@Path("id") id: Int): Response<Unit>

    @PATCH("/foods/{id}/")
    suspend fun edit(@Path("id") id: Int, @Body foodItemFromDB: FoodItemFromDB): Response<FoodItemFromDB>

    @Multipart
    @POST("media/")
    suspend fun upload(@Part media: MultipartBody.Part): Response<String>
}

object FoodsApi {
    val service: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}