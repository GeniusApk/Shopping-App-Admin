package com.geniusapk.shoppingappadmin.presentation.di

import com.geniusapk.shoppingappadmin.data.pushNotifiy.PushNotification
import com.geniusapk.shoppingappadmin.data.repo.ShoppingAppRepoImpl
import com.geniusapk.shoppingappadmin.domain.repo.ShoppingAppRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object UiModule {
    @Provides
    fun provideRepo(firestore: FirebaseFirestore , firebaseStorage: FirebaseStorage  , pushNotification: PushNotification): ShoppingAppRepo {
        return ShoppingAppRepoImpl(firestore , firebaseStorage  , pushNotification )
    }
}