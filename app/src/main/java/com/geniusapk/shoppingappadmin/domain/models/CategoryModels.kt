package com.geniusapk.shoppingappadmin.domain.models

data class CategoryModels(
    var name: String = "",
    val date: Long = System.currentTimeMillis(),
    val createBy : String = "",
)