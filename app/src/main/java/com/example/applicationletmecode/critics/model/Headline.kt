package com.example.applicationletmecode.critics.model


import com.google.gson.annotations.SerializedName

data class Headline(
    @SerializedName("content_kicker")
    val contentKicker: Any,
    @SerializedName("kicker")
    val kicker: String,
    @SerializedName("main")
    val main: String,
    @SerializedName("name")
    val name: Any,
    @SerializedName("print_headline")
    val printHeadline: String,
    @SerializedName("seo")
    val seo: Any,
    @SerializedName("sub")
    val sub: Any
)