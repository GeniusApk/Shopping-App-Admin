package com.geniusapk.shoppingappadmin.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.geniusapk.shoppingappadmin.presentation.ViewModels.ShoppingAppViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoryScreenPreview() {
    CategoryScreen()
}

@Composable
fun CategoryScreen(
    ViewModel: ShoppingAppViewModel = hiltViewModel()
) {
    var categoryName by remember { mutableStateOf("") }
    val state = ViewModel.categoryState.value

    val context = LocalContext.current


    when {
        state.isLoading -> {
            CircularProgressIndicator()

        }

        state.error.isNotBlank() -> {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
        }

        state.data.isNotBlank() -> {
            Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
            categoryName = ""


        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Add New Category",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )



        OutlinedTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text("Category Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                ViewModel.category.value.name = categoryName
                ViewModel.category.value.createBy = "aakib"
                ViewModel.addCategory()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = categoryName.isNotBlank()
        ) {
            Text("Add Category")
        }

    }


}
