package com.geniusapk.shoppingappadmin.domain.repo

import com.geniusapk.shoppingappadmin.domain.models.CategoryModels

interface ShoppingAppRepo {

 suspend    fun addCategory(category: CategoryModels)
}