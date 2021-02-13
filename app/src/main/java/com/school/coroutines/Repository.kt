package com.school.coroutines

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object Repository {

    suspend fun getPosts() = NetworkSource.getPosts()

    object NetworkSource {
        //suspend fun getPosts(): Response<List<MainActivity.Adapter.Item>>
        private interface IPostApi {
            //@GET("weather")
            //suspend fun getPosts(@Query("q")cityname: String, @Query("appid") apikey: String): Response<List<MainActivity.Adapter.Item>>
            @GET("main")
            suspend fun getPosts(@Query("q")cityname: String, @Query("appid") apikey: String): Response<List<MainActivity.Adapter.Item>>
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        private val retrofit = Retrofit.Builder()
            //.baseUrl("https://jsonplaceholder.typicode.com")
                //weather?q={city name}&appid={API key}
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        private val postApi = retrofit.create(IPostApi::class.java)

        suspend fun getPosts() = postApi.getPosts("London", "b210ab49a6dd9ace993f860bddb9ef5b")
    }
}
