package com.example.applicationletmecode.critics.model


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("docs")
    val docs: List<ResultCritics>,
    @SerializedName("meta")
    val meta: Meta
)