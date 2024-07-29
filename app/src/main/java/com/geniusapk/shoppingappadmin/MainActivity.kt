package com.geniusapk.shoppingappadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.geniusapk.shoppingappadmin.presentation.Navigation.App
import com.geniusapk.shoppingappadmin.presentation.screens.AddProductsScreen
import com.geniusapk.shoppingappadmin.presentation.screens.CategoryScreen
import com.geniusapk.shoppingappadmin.ui.theme.ShoppingAppAdminTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingAppAdminTheme {
                    App(modifier = Modifier)
                 //   AddProductsScreen()

            }
        }
    }
}

