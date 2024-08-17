package com.geniusapk.shoppingappadmin.data.pushNotifiy

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.geniusapk.shoppingappadmin.R
import okhttp3.OkHttpClient
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject


class PushNotification @Inject constructor(
    private val FirebaseFirestore: FirebaseFirestore,
    @ApplicationContext private val context: Context // Injecting the application context

) {

    private val client = OkHttpClient()
    private lateinit var accessToken: String




     fun sendNotificationToAllUsers(productName: String, imageUrl: String) {
        FirebaseFirestore.collection("user_tokens").get()
            .addOnSuccessListener { documents ->
                val tokens = documents.mapNotNull { it.getString("token") }
                if (tokens.isNotEmpty()) {
                    sendNotification(tokens, productName, imageUrl)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FCM", "Error getting user tokens: ${exception.message}")
            }
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            updateAccessToken()
        }
    }



    private suspend  fun updateAccessToken() {
        val stream = context.resources.openRawResource(R.raw.shopping)
        val credentials = GoogleCredentials.fromStream(stream)
            .createScoped("https://www.googleapis.com/auth/firebase.messaging")
        credentials.refresh()
        accessToken = credentials.accessToken.tokenValue
    }

    private fun sendNotification(tokens: List<String>, productName: String, imageUrl: String) {
        val json = JSONObject().apply {
            put("message", JSONObject().apply {
                put("token", tokens.first()) // Send to first token for simplicity, ideally you'd implement batching for multiple tokens
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

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Notification sending failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("FCM", "Notification sent successfully")
                } else {
                    Log.e("FCM", "Notification sending failed: ${response.body?.string()}")
                }
            }
        })
    }
}