package com.geniusapk.shoppingappadmin.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.geniusapk.shoppingappadmin.domain.models.ProductsModels
import com.geniusapk.shoppingappadmin.presentation.ViewModels.AddProductState
import com.geniusapk.shoppingappadmin.presentation.ViewModels.ShoppingAppViewModel
import com.geniusapk.shoppingappadmin.utils.uploadImageToDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductsScreen(
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        viewModel.getCategories()

    }


    val getCategorystate = viewModel.getCategoryState.value

    val AddProductState = viewModel.addProductState.value

    val context = LocalContext.current


    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
  //  var productImage by rememberSaveable { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var productImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var availableUnits by remember { mutableStateOf("") }
    var createdBy by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var finalPrice by remember { mutableStateOf("") }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
//            if (uri != null) {
//                uploadImageToDatabase(uri = uri) {
//                    productImage = it
//                }
            productImageUri = uri


            //  productImageUri = uri.toString()
            }



    when {
        AddProductState.loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {
                CircularProgressIndicator()
            }
        }

        AddProductState.success.isNotBlank() -> {
            Toast.makeText(context, AddProductState.success, Toast.LENGTH_SHORT).show()
            name = ""
            price = ""
            description = ""
            productImageUri = null
            category = ""
            availableUnits = ""
            createdBy = ""
            finalPrice = ""

            viewModel.resetAddProductState()


        }

        AddProductState.error.isNotBlank() -> {
            Toast.makeText(context, AddProductState.error, Toast.LENGTH_SHORT).show()
        }


    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding( horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (productImageUri != null) {
            AsyncImage(
                model = productImageUri,
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(shape = RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        launcher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Image Icon"
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = "Add a Photo")
                }
            }
        }



        Text(
            text = "Add Products",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = finalPrice,
                onValueChange = { finalPrice = it },
                label = { Text("Final Price") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

        }




        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth()
                .height(100.dp),
            label = { Text("Description") }
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
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = { expanded = false }
            ) {
                getCategorystate.categories.forEach { categoryItem ->
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
        val availableUnitsInt = availableUnits.toIntOrNull()

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (name.isNotBlank() && price.isNotBlank() && description.isNotBlank() &&
                    productImageUri != null && category.isNotBlank() &&
                    availableUnits.isNotBlank() && createdBy.isNotBlank() && finalPrice.isNotBlank()
                ) {


                    uploadImageToDatabase(productImageUri!!) { imageUrl ->
                        viewModel.addProduct(
                            productsModels = ProductsModels(
                                category = category,
                                name = name,
                                price = price,
                                availableUints = availableUnitsInt!!,
                                description = description,
                                image = imageUrl,
                                createBy = createdBy,
                                finalPrice = finalPrice

                            )
                        )
                    }

                } else {
                    Toast.makeText(context, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Add Product")
        }
    }
}

