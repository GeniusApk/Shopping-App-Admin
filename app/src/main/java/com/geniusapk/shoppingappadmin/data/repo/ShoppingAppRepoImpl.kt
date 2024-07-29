package com.geniusapk.shoppingappadmin.data.repo

import com.geniusapk.shoppingappadmin.common.ResultState
import com.geniusapk.shoppingappadmin.domain.models.CategoryModels
import com.geniusapk.shoppingappadmin.domain.models.ProductsModels
import com.geniusapk.shoppingappadmin.domain.repo.ShoppingAppRepo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ShoppingAppRepoImpl @Inject constructor(private val FirebaseFirestore: FirebaseFirestore) :
    ShoppingAppRepo {
    override suspend fun addCategory(category: CategoryModels): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            FirebaseFirestore.collection("categories").add(category).addOnSuccessListener {
                trySend(ResultState.Success("Category Added Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))

            }
            awaitClose {
                close()
            }

        }


    override suspend fun getCategories(): Flow<ResultState<List<CategoryModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        FirebaseFirestore.collection("categories").get()
            .addOnSuccessListener { querySnapshot ->

                val categories = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(CategoryModels::class.java)
                }
                trySend(ResultState.Success(categories))
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Error(exception.toString()))
            }
        awaitClose { close() }

    }

    override suspend fun addProduct(productsModels: ProductsModels): Flow<ResultState<String>> = callbackFlow{

        trySend(ResultState.Loading)

        FirebaseFirestore.collection("Products").add(productsModels).addOnSuccessListener {
            trySend(ResultState.Success("Product Successfully Added"))
        }.addOnFailureListener {
            trySend(ResultState.Error("Error: ${it.localizedMessage!!}"))
        }

        awaitClose {
            close()
        }

    }
}