package com.example.applicationletmecode.reviewes.apis

import com.example.applicationletmecode.reviewes.model.ReviewesData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ApiServiceReviewes {

    @GET("svc/topstories/v2/world.json?api-key=GW5a0tJfWOcfQ7k3dpQizIsrmpZ33Bmm")
    fun getAllData(): Call<ReviewesData>

}