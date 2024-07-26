package com.geniusapk.shoppingappadmin.data.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDataRepository(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()

    }

}