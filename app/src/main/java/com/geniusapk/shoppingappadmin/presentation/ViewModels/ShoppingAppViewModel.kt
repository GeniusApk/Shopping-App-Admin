package com.geniusapk.shoppingappadmin.presentation.ViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geniusapk.shoppingappadmin.common.ResultState
import com.geniusapk.shoppingappadmin.domain.models.CategoryModels
import com.geniusapk.shoppingappadmin.domain.repo.ShoppingAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShoppingAppViewModel @Inject constructor(var ShoppingAppRepo: ShoppingAppRepo) : ViewModel() {
    val category = mutableStateOf(CategoryModels())

    private val _categoryState: MutableState<CategoryState> = mutableStateOf(CategoryState())
    val categoryState = _categoryState

    private val _getCategoryState: MutableState<GetCategoryState> =
        mutableStateOf(GetCategoryState())
    val getCategoryState = _getCategoryState


    fun getCategories() {
        viewModelScope.launch {
            ShoppingAppRepo.getCategories().collectLatest {
                when (it) {
                    is ResultState.Success<*> -> {
                        _getCategoryState.value = _getCategoryState.value.copy(
                            categories = it.data as List<CategoryModels>,
                            isLoading = false,

                            )
                    }

                    is ResultState.Error -> {
                        _getCategoryState.value = _getCategoryState.value.copy(
                            error = it.exception.message ?: "Unknown error",
                            isLoading = false

                        )

                    }

                    ResultState.Loading -> {
                        _getCategoryState.value =
                            _getCategoryState.value.copy(
                                isLoading = true
                            )

                    }
                }
            }
        }
    }


    fun addCategory() {
        viewModelScope.launch {
            ShoppingAppRepo.addCategory(category.value).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        categoryState.value = CategoryState(error = it.exception.message.toString())
                    }

                    ResultState.Loading -> {
                        categoryState.value = CategoryState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        categoryState.value = CategoryState(data = it.data)
                        category.value.name = ""
                        category.value.createBy = ""
                    }


                }
            }


        }

    }


}


data class GetCategoryState(
    val categories: List<CategoryModels> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)


data class CategoryState(
    val data: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)


data class AddProductsState(
    val data: String = "",
    val isLoading: Boolean = false,
    val error: String = ""

)