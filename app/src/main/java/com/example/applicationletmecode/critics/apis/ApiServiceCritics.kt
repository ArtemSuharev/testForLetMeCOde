package com.example.applicationletmecode.critics.apis

import com.example.applicationletmecode.critics.model.CriticsData
import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceCritics {

    @GET("svc/search/v2/articlesearch.json?fq=section_name%3A%22Movies%22%20AND%20type_of_material%3A%22Review%22&api-key=GW5a0tJfWOcfQ7k3dpQizIsrmpZ33Bmm")
    fun getAllData(): Call<CriticsData>

}