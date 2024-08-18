package com.geniusapk.shoppingappadmin.presentation.ViewModels

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geniusapk.shoppingappadmin.common.ResultState
import com.geniusapk.shoppingappadmin.domain.models.BannerModels
import com.geniusapk.shoppingappadmin.domain.models.CategoryModels
import com.geniusapk.shoppingappadmin.domain.models.ProductsModels
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

    private val _addProductState : MutableState<AddProductState> = mutableStateOf(AddProductState())
    val addProductState : MutableState<AddProductState> = _addProductState


    private val _uploadCategoryImageState  = mutableStateOf(UploadCategoryImageState())
    val uploadCategoryImageState : MutableState<UploadCategoryImageState> = _uploadCategoryImageState


    private val _upLoadBannerState = mutableStateOf(UploadBannerState())
    val uploadBannerState : MutableState<UploadBannerState> = _upLoadBannerState

    private val _addBannerState = mutableStateOf(AddBannerState())
    val addBannerState : MutableState<AddBannerState> = _addBannerState


    fun resetCategoryState() {
        _categoryState.value = CategoryState()
    }


    fun resetUploadCategoryImageState() {
        _uploadCategoryImageState.value = UploadCategoryImageState()
    }

    fun resetUploadBannerState() {
        _upLoadBannerState.value = UploadBannerState()
    }


    fun resetAddBannerState() {
        _addBannerState.value = AddBannerState()
    }

    fun addBanner(bannerModels: BannerModels){
        viewModelScope.launch {
            ShoppingAppRepo.addBanner(bannerModels).collectLatest {
                when(it){
                    is ResultState.Error -> {
                        _addBannerState.value = _addBannerState.value.copy(
                            loading = false,
                            error = it.exception
                        )
                    }
                    is ResultState.Loading -> {
                        _addBannerState.value = _addBannerState.value.copy(
                            loading = true
                        )
                    }
                    is ResultState.Success -> {
                        _addBannerState.value = _addBannerState.value.copy(
                            loading = false,
                            success = it.data
                        )

                    }
                }
            }

        }
    }


    fun resetAddProductState() {
        _addProductState.value = AddProductState()
    }

    fun uploadBanner(imageUri: Uri){
        viewModelScope.launch {
            ShoppingAppRepo.upLoadBannerImage(imageUri).collectLatest {
                when(it){
                    is ResultState.Error -> {
                        _upLoadBannerState.value = _upLoadBannerState.value.copy(
                            loading = false,
                            error = it.exception
                        )
                    }
                    is ResultState.Loading -> {
                        _upLoadBannerState.value = _upLoadBannerState.value.copy(
                            loading = true
                        )
                    }
                    is ResultState.Success -> {
                        _upLoadBannerState.value = _upLoadBannerState.value.copy(
                            loading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }


    fun uploadCategoryImage(imageUri : Uri){
        viewModelScope.launch {
            ShoppingAppRepo.uploadCategoryImage(imageUri).collectLatest {
                when(it){
                    is ResultState.Error -> {
                        _uploadCategoryImageState.value = _uploadCategoryImageState.value.copy(
                            loading = false,
                            error = it.exception
                        )
                    }
                    is ResultState.Loading -> {
                        _uploadCategoryImageState.value = _uploadCategoryImageState.value.copy(
                            loading = true
                        )

                    }
                    is ResultState.Success -> {
                        _uploadCategoryImageState.value = _uploadCategoryImageState.value.copy(
                            loading = false,
                            success = it.data
                        )
                    }
                }
            }
        }
    }



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
                            error = it.exception ?: "Unknown error",
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


    fun addCategory( categories: CategoryModels) {
        viewModelScope.launch {
            ShoppingAppRepo.addCategory(categories).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        categoryState.value = CategoryState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        categoryState.value = CategoryState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        categoryState.value = CategoryState(data = it.data)
                        category.value.name = ""
                        category.value.createBy = ""
                        category.value.categoryImage = ""


                    }


                }
            }


        }

    }


    fun addProduct(productsModels: ProductsModels){
        viewModelScope.launch {

            ShoppingAppRepo.addProduct(productsModels = productsModels).collectLatest {

                when(it){
                    is ResultState.Error -> {
                        _addProductState.value = AddProductState(error = it.exception)
                    }
                    ResultState.Loading -> {
                        _addProductState.value = AddProductState(loading = true)
                    }
                    is ResultState.Success -> {
                        _addProductState.value = AddProductState(success = it.data)
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


data class AddProductState(
    var loading: Boolean = false,
    var success: String = "",
    var error: String = ""
)

data class UploadCategoryImageState(
    var loading: Boolean = false,
    var success: String = "",
    var error: String = ""
)

data class UploadBannerState(
    var loading: Boolean = false,
    var success: String = "",
    var error: String = ""
)

data class AddBannerState(
    var loading: Boolean = false,
    var success: String = "",
    var error: String = ""
)