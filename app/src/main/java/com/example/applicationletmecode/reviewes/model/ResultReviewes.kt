package com.example.applicationletmecode.reviewes.model


data class ResultReviewes(
    val `abstract`: String,
    val byline: String,
    val createdDate: String,
    val des_facet: List<String>,
    val geoFacet: List<String>,
    val item_type: String,
    val kicker: String,
    val materialTypeFacet: String,
    val multimedia: List<Multimedia>,
    val orgFacet: List<String>,
    val perFacet: List<String>,
    val published_date: String,
    val section: String,
    val shortUrl: String,
    val subsection: String,
    val title: String,
    val updatedDate: String,
    val uri: String,
    val url: String
)