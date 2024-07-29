package com.geniusapk.shoppingappadmin.domain.repo

import com.geniusapk.shoppingappadmin.common.ResultState
import com.geniusapk.shoppingappadmin.domain.models.CategoryModels
import com.geniusapk.shoppingappadmin.domain.models.ProductsModels
import kotlinx.coroutines.flow.Flow

interface ShoppingAppRepo {

 suspend    fun addCategory(category: CategoryModels): Flow<ResultState<String>>
 //suspend fun addProducts(product: ProductsModels): Flow<ResultState<String>>
 suspend fun getCategories(): Flow<ResultState<List<CategoryModels>>>

 suspend fun addProduct(productsModels: ProductsModels):Flow<ResultState<String>>

}