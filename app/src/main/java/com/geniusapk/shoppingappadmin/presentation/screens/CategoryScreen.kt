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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.geniusapk.shoppingappadmin.domain.models.BannerModels
import com.geniusapk.shoppingappadmin.domain.models.CategoryModels
import com.geniusapk.shoppingappadmin.presentation.ViewModels.ShoppingAppViewModel


@Composable
fun CategoryScreen(
    viewModel: ShoppingAppViewModel = hiltViewModel(),
) {
    var categoryName by remember { mutableStateOf("") }
    var categoryBy by remember { mutableStateOf("") }
    var categoryImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var categoryImage by remember { mutableStateOf("") }

    val state = viewModel.categoryState.value

    val context = LocalContext.current


    var bannerImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var bannerImage by remember { mutableStateOf("") }
    var bannerName by remember { mutableStateOf("") }


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {

                viewModel.uploadCategoryImage(uri)
                categoryImageUri = uri

                //  productImageUri = uri


                //  productImageUri = uri.toString()
            }
        }



    val uploadbannerImageState = viewModel.uploadBannerState.value

    val addBannerState = viewModel.addBannerState.value

    val launcher2 =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {

                viewModel.uploadBanner(uri)
                bannerImageUri = uri

                //  productImageUri = uri


                //  productImageUri = uri.toString()
            }
        }


    val uploadImageState = viewModel.uploadCategoryImageState.value


    // when i was using separate state for uploading image and adding category then it will giving too much toast
    // now this totaly file

    when {
        state.isLoading || uploadImageState.loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.error.isNotBlank() -> {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
        }
        uploadImageState.error.isNotBlank() -> {
            Toast.makeText(context, uploadImageState.error, Toast.LENGTH_SHORT).show()
        }
        state.data.isNotBlank() -> {
            Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
            categoryName = ""
            categoryBy = ""
            categoryImageUri = null
            categoryImage = ""


        }
        uploadImageState.success.isNotBlank() -> {
            categoryImage = uploadImageState.success
        }
    }




    when{
        uploadbannerImageState.loading || addBannerState.loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uploadbannerImageState.error.isNotBlank() || addBannerState.error.isNotBlank() -> {
            Toast.makeText(context, uploadbannerImageState.error, Toast.LENGTH_SHORT).show()
        }
        addBannerState.success.isNotBlank() -> {
            Toast.makeText(context, addBannerState.success, Toast.LENGTH_SHORT).show()
            bannerImageUri = null
            bannerImage = ""
        }
        uploadbannerImageState.success.isNotBlank() -> {
            bannerImage = uploadbannerImageState.success
        }

    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                if (categoryName.isNotBlank() && categoryBy.isNotBlank()) {


                    viewModel.addCategory(
                        categories = CategoryModels(
                            name = categoryName,
                            createBy = categoryBy,
                            categoryImage = categoryImage

                        )
                    )
                    categoryName = ""
                    categoryBy = ""
                    categoryImageUri = null
                    categoryImage = ""
                } else {
                    Toast.makeText(context, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
                }


            },
            modifier = Modifier.fillMaxWidth(),
            enabled = categoryName.isNotBlank()
        ) {
            Text("Add Category")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // now this is for banner
        Text(text = "Add Banner")


        if (bannerImageUri != null) {
            AsyncImage(
                model = bannerImageUri,
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
                        launcher2.launch(
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
        OutlinedTextField(
            value = bannerName,
            onValueChange = { bannerName = it },
            label = { Text("Banner Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )



        Button(onClick = {
            val banner = BannerModels(
                name = bannerName,
                image = bannerImage

            )
            viewModel.addBanner(banner)

        }) {
            Text(text = "Add Banner")
        }









    }


}
