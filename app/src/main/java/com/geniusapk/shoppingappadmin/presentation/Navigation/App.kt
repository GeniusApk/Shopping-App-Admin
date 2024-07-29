package com.geniusapk.shoppingappadmin.presentation.Navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.geniusapk.shoppingappadmin.presentation.screens.AddProductsScreen
import com.geniusapk.shoppingappadmin.presentation.screens.CategoryScreen


@Composable
fun App(modifier: Modifier) {
val navController = rememberNavController()

    var selected by remember {
        mutableIntStateOf(0)
    }

    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", Icons.Default.Home),
        BottomNavItem("Add Product", Icons.Default.Add),
        BottomNavItem("Notification", Icons.Default.Notifications),
        BottomNavItem("Category", Icons.Default.Category),
        BottomNavItem("Order", Icons.Default.ShoppingCart),
    )
    Box(modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route

                NavigationBar{
                    bottomNavItems.forEachIndexed { index, bottomNavItem ->
                        NavigationBarItem(
                            alwaysShowLabel = true,
                            selected = selected == index,
                            onClick = {selected = index},

                            icon = {
                                Icon(imageVector =
                                bottomNavItem.icon , contentDescription = bottomNavItem.name)
                            }, label = {
                                Text(text = bottomNavItem.name)
                            }


                        )
                    }

                }



            }
        ) {innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)){
                when(selected){
                    0-> Text(text = "Dashboard")
                    1-> AddProductsScreen()
                    2-> Text(text = "Notification")
                    3-> CategoryScreen()
                    4-> Text(text = "Order")

                }
            }


    }






        }
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




data class BottomNavItem(val name : String, val icon : ImageVector, )
