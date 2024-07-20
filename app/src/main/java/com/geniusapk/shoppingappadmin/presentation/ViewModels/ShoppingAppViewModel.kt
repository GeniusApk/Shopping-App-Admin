package com.geniusapk.shoppingappadmin.presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geniusapk.shoppingappadmin.domain.models.CategoryModels
import com.geniusapk.shoppingappadmin.domain.repo.ShoppingAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShoppingAppViewModel @Inject constructor(var  ShoppingAppRepo: ShoppingAppRepo) : ViewModel() {
    val category = CategoryModels()
    fun addCategory() {
        viewModelScope.launch {
            ShoppingAppRepo.addCategory(category)

        }

    }
}