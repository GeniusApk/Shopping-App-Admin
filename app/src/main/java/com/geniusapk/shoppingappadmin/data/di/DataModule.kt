package com.geniusapk.shoppingappadmin.data.di

import android.content.Context
import com.geniusapk.shoppingappadmin.data.pushNotifiy.PushNotification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDataRepository(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()

    }

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()

    }

    @Provides
    fun providePushNotification(
        FirebaseFirestore: FirebaseFirestore,
        @ApplicationContext context: Context
    ): PushNotification {
        return PushNotification(FirebaseFirestore, context)
    }


}