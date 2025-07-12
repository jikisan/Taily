package org.jikisan.taily.ui.screens.addpet.addpetcontent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.coroutines.launch
import network.chaintech.cmpimagepickncrop.CMPImagePickNCropDialog
import network.chaintech.cmpimagepickncrop.imagecompress.compressImage
import network.chaintech.cmpimagepickncrop.imagecropper.ImageAspectRatio
import network.chaintech.cmpimagepickncrop.imagecropper.rememberImageCropper
import network.chaintech.cmpimagepickncrop.utils.ImageFileFormat
import network.chaintech.cmpimagepickncrop.utils.ImagePickerDialogStyle
import network.chaintech.cmpimagepickncrop.utils.toByteArray
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.data.local.mockdata.MockData
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.add_photo_alternate_24px
import taily.composeapp.generated.resources.selfie_dog

@Composable
fun AddPetProfilePhotoContent(viewModel: AddPetViewModel, pet: Pet?) {

    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var openImagePicker by remember { mutableStateOf(false) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }

    val imageCropper = rememberImageCropper()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        AddPetHeader("Let's see that face!")

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(5)
                )
        ) {

            CMPImagePickNCropDialog(
                imageCropper = imageCropper,
                openImagePicker = openImagePicker,
                defaultAspectRatio = ImageAspectRatio(16, 9),
                imagePickerDialogStyle = ImagePickerDialogStyle(
                    title = "Choose from option",
                    txtCamera = "From Camera",
                    txtGallery = "From Gallery",
                    txtCameraColor = Color.DarkGray,
                    txtGalleryColor = Color.DarkGray,
                    cameraIconTint = Color.DarkGray,
                    galleryIconTint = Color.DarkGray,
                    backgroundColor = Color.White
                ),
                autoZoom = true,
                imagePickerDialogHandler = { openImagePicker = it },
                selectedImageCallback = {
                    selectedImage = it

                    selectedImage?.let { image ->
                        viewModel.updatePhotoImageBitmap(image)
                    }

                    imageByteArray = it.toByteArray(
                        format = ImageFileFormat.JPEG,
                        quality = 0.6f
                    )

                    imageByteArray.let { byteArray ->
                        viewModel.updatePetPhotoByteArray(byteArray!!)
                    }

                },
                selectedImageFileCallback = { sharedImage ->
                    // You can still use the compressed file if needed,
                    // but we'll rely on the ImageBitmap for now
                    scope.launch {
                        val compressedFilePath = compressImage(
                            sharedImage = sharedImage,
                            targetFileSize = 200 * 1024
                        )

                    }
                }
            )

            pet?.let {
                it.imageBitmap?.let { image ->
                    Image(
                        bitmap = image,
                        contentDescription = "Pet photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } ?: run {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.selfie_dog),
                            contentDescription = "Add photo",
                            modifier = Modifier.size(150.dp)
                        )
                        Text(
                            text = "Add Photo",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Column {
                Button(
                    modifier = Modifier.padding(16.dp).clip(RoundedCornerShape(5)),
                    elevation = ButtonDefaults.buttonElevation(15.dp),
                    onClick = { openImagePicker = true },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.add_photo_alternate_24px),
                        contentDescription = "Pick Photo",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pick a photo")
                }

//                Button(
//                    modifier = Modifier.padding(16.dp),
//                    elevation = ButtonDefaults.buttonElevation(15.dp),
//                    enabled = imageByteArray != null,
//                    onClick = {
//                        scope.launch {
//                            viewModel.uploadPetProfilePhoto()
//                        }
//                    }
//                ) {
//                    Text("Upload photo to supabase")
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Icon(
//                        painter = painterResource(Res.drawable.add_photo_alternate_24px),
//                        contentDescription = "Upload photo to supabase",
//                    )
//                }
            }
        }
    }
}

@Preview
@Composable
fun AddPetProfilePhotoContentPreviewWithData() {
    TailyTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {

            AddPetProfilePhotoContent(
                viewModel = koinViewModel<AddPetViewModel>(),
                pet = MockData.mockPets.first()
            )
        }
    }
}

@Preview
@Composable
fun AddPetProfilePhotoContentPreviewWithNoData() {
    TailyTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {

            AddPetProfilePhotoContent(
                viewModel = koinViewModel<AddPetViewModel>(),
                pet = MockData.mockPets[1]
            )
        }
    }
}