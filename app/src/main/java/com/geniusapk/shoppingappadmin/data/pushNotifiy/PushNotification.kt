package com.geniusapk.shoppingappadmin.data.pushNotifiy

import android.content.Context
import android.util.Log
import com.geniusapk.shoppingappadmin.R
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushNotification @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {
    private val client = OkHttpClient()
    private var accessToken: String = ""
    private val coroutineScope = CoroutineScope(Dispatchers.IO)




    fun sendNotificationToAllUsers(productName: String, imageUrl: String) {
        coroutineScope.launch {
            try {
                val tokens = getTokens()
                if (tokens.isNotEmpty()) {
                    sendNotification(tokens, productName, imageUrl)
                }
            } catch (e: Exception) {
                Log.e("FCM", "Error getting user tokens: ${e.message}")
            }
        }
    }

    private suspend fun getTokens(): List<String> = withContext(Dispatchers.IO) {
        val snapshot = firebaseFirestore.collection("user_tokens").get().await()
        snapshot.mapNotNull { it.getString("token") }
    }

    init {
        coroutineScope.launch {
            updateAccessToken()
        }
    }
    private suspend fun updateAccessToken() {
        withContext(Dispatchers.IO) {
            try {
                val stream = context.resources.openRawResource(R.raw.shopping)
                val credentials = GoogleCredentials.fromStream(stream)
                    .createScoped("https://www.googleapis.com/auth/firebase.messaging")
                credentials.refresh()
                accessToken = credentials.accessToken.tokenValue
            } catch (e: Exception) {
                Log.e("FCM", "Error updating access token: ${e.message}")
            }
        }
    }

    private suspend fun sendNotification(tokens: List<String>, productName: String, imageUrl: String) {
        tokens.forEach { token ->
            val json = JSONObject().apply {
                put("message", JSONObject().apply {
                    put("token", token)
                    put("notification", JSONObject().apply {
                        put("title", "New Product Added")
                        put("body", "Check out our new product: $productName")
                        put("image", imageUrl)
                    })
                    put("data", JSONObject().apply {
                        put("product_name", productName)
                        put("image_url", imageUrl)
                    })
                })
            }

        val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .header("Authorization", "Bearer $accessToken")
            .url("https://fcm.googleapis.com/v1/projects/shopping-app-admin-d89ac/messages:send")
            .post(body)
            .build()

        try {
            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
            if (response.isSuccessful) {
                Log.d("FCM", "Notification sent successfully")
            } else {
                Log.e("FCM", "Notification sending failed: ${response.body?.string()}")
            }
        } catch (e: IOException) {
            Log.e("FCM", "Notification sending failed: ${e.message}")
        }
    }
}
}