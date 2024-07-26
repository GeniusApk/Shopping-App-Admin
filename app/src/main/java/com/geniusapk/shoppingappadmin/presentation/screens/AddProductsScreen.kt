package com.geniusapk.shoppingappadmin.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.geniusapk.shoppingappadmin.presentation.ViewModels.AddProductsState
import com.geniusapk.shoppingappadmin.presentation.ViewModels.CategoryState
import com.geniusapk.shoppingappadmin.presentation.ViewModels.ShoppingAppViewModel
import com.google.api.LogDescriptor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductsScreen(
    ViewModel: ShoppingAppViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        ViewModel.getCategories()

    }


    val state = ViewModel.getCategoryState.value
    val context = LocalContext.current




    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var availableUnits by remember { mutableStateOf("") }
    var createdBy by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        state.error.isNotBlank() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Error: ${state.error}")
            }
        }
        else -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Add Products",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Description") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = image,
                    onValueChange = { image = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Image URL") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    modifier = Modifier.fillMaxWidth(),
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {
                            category = it
                        },

                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        modifier = Modifier.fillMaxWidth(),
                        onDismissRequest = { expanded = false }
                    ) {
                        state.categories.forEach { categoryItem ->
                            DropdownMenuItem(
                                text = { Text(categoryItem.name) },
                                onClick = {
                                    category = categoryItem.name
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = availableUnits,
                    onValueChange = { availableUnits = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Available Units") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = createdBy,
                    onValueChange = { createdBy = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Created By") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (name.isNotBlank() && price.isNotBlank() && description.isNotBlank() &&
                            image.isNotBlank() && category.isNotBlank() &&
                            availableUnits.isNotBlank() && createdBy.isNotBlank()
                        ) {

                            Toast.makeText(context, "Product Added Successfully", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Add Product")
                }
            }
        }
    }
}