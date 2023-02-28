package com.example.marsphotos.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

import retrofit2.http.GET

private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
    .baseUrl(BASE_URL)
    .build()

// デリゲート先
interface  MarsApiService {
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>
}

// アプリ内でWeb接続のインスタンスはひとつでいいのでシングルトンにする。
object MarsApi{
    // 遅延初期化(必要な時に初期化)をサポートする by lazy
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}