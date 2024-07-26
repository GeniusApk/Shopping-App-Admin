package com.geniusapk.shoppingappadmin.common

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val exception: Exception) : ResultState<Nothing>()
    object  Loading : ResultState<Nothing>()
}