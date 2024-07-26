package com.geniusapk.shoppingappadmin.domain.models

 data class ProductsModels (
     val name : String = "",
     val price : String = "",
     val description : String = "",
     val image : String = "",
     val category : String = "",
     val date : Long = System.currentTimeMillis(),
     val createBy : String = "",
     val availableUints : Int = 0,

 )