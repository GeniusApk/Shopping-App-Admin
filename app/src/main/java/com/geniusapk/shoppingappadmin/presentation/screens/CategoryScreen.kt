package com.geniusapk.shoppingappadmin.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.geniusapk.shoppingappadmin.presentation.ViewModels.ShoppingAppViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoryScreenPreview() {
    CategoryScreen()
}

@Composable
fun CategoryScreen(
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    var categoryName by remember { mutableStateOf("") }
    var categoryBy by remember { mutableStateOf("") }
    var categoryImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var categoryImage by remember { mutableStateOf("") }

    val state = viewModel.categoryState.value

    val context = LocalContext.current


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {

               viewModel.uploadCategoryImage(uri)
                categoryImageUri = uri

                //  productImageUri = uri


                //  productImageUri = uri.toString()
            }
        }


    val uploadImageState = viewModel.uploadCategoryImageState.value

    if (uploadImageState.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (uploadImageState.error.isNotBlank()) {
        Toast.makeText(context, uploadImageState.error, Toast.LENGTH_SHORT).show()
    } else if (uploadImageState.success.isNotBlank()) {
        Toast.makeText(context, uploadImageState.success, Toast.LENGTH_SHORT).show()
        categoryImage = uploadImageState.success
    }



    when {
        state.isLoading -> {
            CircularProgressIndicator()

        }

        state.error.isNotBlank() -> {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
        }

        state.data.isNotBlank() -> {
            Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()

        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        if (categoryImageUri != null) {
            AsyncImage(
                model = categoryImageUri,
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


        OutlinedTextField(
            value = categoryBy,
            onValueChange = { categoryBy = it },
            label = { Text("Category By") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.category.value.name = categoryName
                viewModel.category.value.createBy = categoryBy
                viewModel.category.value.categoryImage = categoryImage
                viewModel.addCategory()
                categoryName = ""
                categoryBy = ""
                categoryImageUri = null
                categoryImage = ""
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = categoryName.isNotBlank()
        ) {
            Text("Add Category")
        }

    }


}
