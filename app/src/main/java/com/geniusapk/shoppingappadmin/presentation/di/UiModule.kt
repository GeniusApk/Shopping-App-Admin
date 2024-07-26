package com.geniusapk.shoppingappadmin.presentation.di

import com.geniusapk.shoppingappadmin.data.repo.ShoppingAppRepoImpl
import com.geniusapk.shoppingappadmin.domain.repo.ShoppingAppRepo
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object UiModule {
    @Provides
    fun provideRepo(firestore: FirebaseFirestore): ShoppingAppRepo {
        return ShoppingAppRepoImpl(firestore)

    }
}