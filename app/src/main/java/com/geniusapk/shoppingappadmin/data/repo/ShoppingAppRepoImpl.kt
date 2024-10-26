package com.geniusapk.shoppingappadmin.data.repo

import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.geniusapk.shoppingappadmin.common.ResultState
import com.geniusapk.shoppingappadmin.data.pushNotifiy.PushNotification
import com.geniusapk.shoppingappadmin.domain.models.BannerModels
import com.geniusapk.shoppingappadmin.domain.models.CategoryModels
import com.geniusapk.shoppingappadmin.domain.models.ProductsModels
import com.geniusapk.shoppingappadmin.domain.repo.ShoppingAppRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ShoppingAppRepoImpl @Inject constructor(
    private val FirebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val pushNotification: PushNotification

) :
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

    override suspend fun addProduct(productsModels: ProductsModels): Flow<ResultState<String>> =
        callbackFlow {

            trySend(ResultState.Loading)

            FirebaseFirestore.collection("Products").add(productsModels).addOnSuccessListener {
                trySend(ResultState.Success("Product Successfully Added"))
                pushNotification.sendNotificationToAllUsers(productsModels.name, productsModels.image)

                Log.d("testtag", "addProduct: ${productsModels.name}")
            }.addOnFailureListener {
                trySend(ResultState.Error("Error: ${it.localizedMessage!!}"))
            }

            awaitClose {
                close()
            }

        }

    override suspend fun uploadCategoryImage(imageUri: Uri): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseStorage.reference.child("categoryImages/${System.currentTimeMillis()}")
                .putFile(imageUri ?: Uri.EMPTY).addOnCompleteListener {
                    it.result.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                        trySend(ResultState.Success(imageUrl.toString()))
                    }
                    if (it.exception != null) {
                        trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                    }

                }
            awaitClose {
                close()
            }

        }

    override suspend fun upLoadBannerImage(imageUri: Uri): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseStorage.reference.child("bannerImages/${System.currentTimeMillis()}").putFile(
                imageUri ?: Uri.EMPTY
            ).addOnCompleteListener {
                it.result.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    trySend(ResultState.Success(imageUrl.toString()))
                }
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }

            }
            awaitClose {
                close()
            }

        }

    override suspend fun addBanner(bannerModels: BannerModels): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            FirebaseFirestore.collection("banner").add(bannerModels).addOnSuccessListener {
                trySend(ResultState.Success("Banner Added Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
            awaitClose {
                close()
            }

        }



}



