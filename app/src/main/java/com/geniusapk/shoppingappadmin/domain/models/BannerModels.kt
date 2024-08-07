package com.geniusapk.shoppingappadmin.domain.models

data class BannerModels(
    val name : String = "",
    val image : String = "",
    val date : Long = System.currentTimeMillis(),
)
