package org.jikisan.taily.ui.screens.addpet.addpetcontent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.Uri
import coil3.compose.rememberAsyncImagePainter
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.data.local.mockdata.MockData
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.add_photo_alternate_24px
import taily.composeapp.generated.resources.home_icon
import taily.composeapp.generated.resources.sefie_dog_2
import taily.composeapp.generated.resources.selfie_dog
import taily.composeapp.generated.resources.stethoscope_24px

@Composable
fun AddPetProfilePhotoContent(viewModel: AddPetViewModel, pet: Pet?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Let’s See That Face!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(24.dp))

//        Card(
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//        ) {
//            Box(
//                modifier = Modifier
//                    .height(250.dp)
//                    .aspectRatio(3f / 4f),
//                contentAlignment = Alignment.Center
//            ) {
//
//                var photoUri = remember { mutableStateOf<Uri?>(null) }
//
//                pet?.let {
//
//                    if (it.photoUrl.isNotEmpty()) {
//                        // Placeholder for actual image
//                        Image(
//                            painter = rememberAsyncImagePainter(photoUri),
//                            contentDescription = "Pet photo",
//                            modifier = Modifier
//                                .height(250.dp)
//                                .aspectRatio(3f / 4f)
//                        )
//                    } else {
////                        Column(
////                            horizontalAlignment = Alignment.CenterHorizontally
////                        ) {
////                            Icon(
////                                painter = painterResource(Res.drawable.home_icon),
////                                contentDescription = "Add photo",
////                            )
////                            Text(
////                                text = "Add Photo",
////                                style = MaterialTheme.typography.bodySmall,
////                                textAlign = TextAlign.Center
////                            )
////                        }
//                    }
//                }
//
//            }
//        }

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
            var photoUri = remember { mutableStateOf<Uri?>(null) }

            pet?.let {

                if (it.photoUrl.isNotEmpty()) {
                    // Placeholder for actual image
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Pet photo",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
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
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                elevation = ButtonDefaults.buttonElevation(15.dp),
                onClick = {}
            ) {
                Text("Pick a photo")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(Res.drawable.add_photo_alternate_24px),
                    contentDescription = "Pick Photo",
                )
            }

//            OutlinedButton(
//                onClick = { /* Handle click */ },
//                modifier = Modifier.padding(16.dp),
//                shape = RoundedCornerShape(50.dp),
//                border = BorderStroke(
//                    width = 2.dp,
//                    color = MaterialTheme.colorScheme.primary
//                ),
//            ) {
//                Text("Pick a photo")
//                Spacer(modifier = Modifier.width(8.dp))
//                Icon(
//                    painter = painterResource(Res.drawable.add_photo_alternate_24px),
//                    contentDescription = "Pick Photo",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }
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
                viewModel = AddPetViewModel(),
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
                viewModel = AddPetViewModel(),
                pet = MockData.mockPets[1]
            )
        }
    }
}