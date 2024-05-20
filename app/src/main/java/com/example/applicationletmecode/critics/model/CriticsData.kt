package com.example.applicationletmecode.critics.model


import com.google.gson.annotations.SerializedName

data class CriticsData(
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("response")
    val response: Response,
    @SerializedName("status")
    val status: String
)