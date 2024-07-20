package com.geniusapk.shoppingappadmin.presentation.Navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    object Dashboard

    @Serializable
    object AddProduct

    @Serializable
    object Notification

    @Serializable
    object Category

    @Serializable
    object Order

}