package com.school.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object Repository {

    suspend fun getPosts() = NetworkSource.getPosts()

    object NetworkSource {
        private interface IPostApi {
            @GET("/posts")
            //fun getPosts(): Call<List<MainActivity.Adapter.Item>>
            suspend fun getPosts(): Response<List<MainActivity.Adapter.Item>>
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        private val postApi = retrofit.create(IPostApi::class.java)

        suspend fun getPosts() = postApi.getPosts()
    }
}
