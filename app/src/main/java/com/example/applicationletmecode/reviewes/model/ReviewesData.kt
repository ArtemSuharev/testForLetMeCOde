package com.example.applicationletmecode.reviewes.model


import com.google.gson.annotations.SerializedName

data class ReviewesData(
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("num_results")
    val numResults: Int,
    @SerializedName("results")
    val resultReviewes: List<ResultReviewes>,
    @SerializedName("section")
    val section: String,
    @SerializedName("status")
    val status: String
)