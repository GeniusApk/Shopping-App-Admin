package com.geniusapk.shoppingappadmin.utils

import com.google.firebase.storage.FirebaseStorage
import android.net.Uri

fun uploadImageToDatabase(
    uri : Uri,
    imageUrl : (String)-> Unit
) {
    FirebaseStorage.getInstance().reference.child("productImages/${System.currentTimeMillis()}")
        .putFile(uri).addOnCompleteListener {
            it.result.storage.downloadUrl.addOnSuccessListener {task->
                imageUrl(task.toString())
            }

        }

}