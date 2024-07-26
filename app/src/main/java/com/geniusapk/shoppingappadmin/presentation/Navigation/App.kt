package com.geniusapk.shoppingappadmin.presentation.Navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun App(modifier: Modifier) {
val navController = rememberNavController()
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        NavHost(navController = navController, startDestination = Routes.Dashboard){
            composable<Routes.Dashboard>{

            }
            composable<Routes.AddProduct>{

            }
            composable<Routes.Notification>{

            }
            composable<Routes.Category>{

            }
            composable<Routes.Order>{

            }

        }

    }
}