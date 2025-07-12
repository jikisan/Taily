package org.jikisan.taily.ui.screens.addpet.addpetcontent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.jikisan.taily.ui.screens.addpet.PetConstants.FRACTIONAL_PARTS_1_DECIMAL
import org.jikisan.taily.ui.screens.addpet.PetConstants.WEIGHT_UNITS
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AddPetWeightContent(viewModel: AddPetViewModel, pet: Pet?) {
    val weightRange = remember { (0..200).map { it.toString() } }
    val selectedWeight = remember { mutableStateOf(weightRange[7].toInt()) }
    val selectedFraction = remember { mutableStateOf(FRACTIONAL_PARTS_1_DECIMAL[3]) }
    val selectedUnit = remember { mutableStateOf(WEIGHT_UNITS[0]) }

    // Calculate final weight and update viewModel whenever any value changes
    val finalWeight = remember {
        derivedStateOf {
            selectedWeight.value + selectedFraction.value.toDouble()
        }
    }

    // Update viewModel when final weight or unit changes
    LaunchedEffect(finalWeight.value, selectedUnit.value) {
        viewModel.updateWeight(
            Weight(
                unit = selectedUnit.value,
                value = finalWeight.value
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AddPetHeader("Enter ${pet?.name ?: "your pet"}'s weight")

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            // Whole number picker
            CarouselPicker(
                items = weightRange,
                selectedItem = selectedWeight.value.toString(),
                onItemSelected = { selectedWeight.value = it.toInt() },
                modifier = Modifier.weight(1f)
            )

            // Decimal fraction picker
            CarouselPicker(
                items = FRACTIONAL_PARTS_1_DECIMAL,
                selectedItem = selectedFraction.value,
                onItemSelected = { selectedFraction.value = it },
                modifier = Modifier.weight(1f)
            )

            // Unit picker
            CarouselPicker(
                items = WEIGHT_UNITS,
                selectedItem = selectedUnit.value,
                onItemSelected = { selectedUnit.value = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display current weight
        Text(
            text = "${finalWeight.value} ${selectedUnit.value}",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CarouselPicker(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemHeight = 50.dp
    val initialIndex = items.indexOf(selectedItem).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    // Track the center item
    val centerItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) return@derivedStateOf initialIndex

            val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
            val centerItem = layoutInfo.visibleItemsInfo.minByOrNull { itemInfo ->
                kotlin.math.abs((itemInfo.offset + itemInfo.size / 2) - viewportCenter)
            }
            centerItem?.index ?: initialIndex
        }
    }

    // Handle selection when scrolling stops
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && centerItemIndex in items.indices) {
            val newItem = items[centerItemIndex]
            if (newItem != selectedItem) {
                onItemSelected(newItem)
            }
        }
    }

    Box(
        modifier = modifier
            .width(80.dp)
            .height(200.dp)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 75.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
        ) {
            itemsIndexed(items) { index, item ->
                val isCenter = index == centerItemIndex
                val scale = if (isCenter) 1.0f else 0.8f
                val alpha = if (isCenter) 1.0f else 0.6f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .scale(scale)
                        .alpha(alpha),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = if (isCenter) MaterialTheme.colorScheme.primary else   MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isCenter) FontWeight.Bold else FontWeight.Normal,
                            fontSize = if (isCenter) MaterialTheme.typography.headlineMedium.fontSize else MaterialTheme.typography.headlineSmall.fontSize
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }

        // Center selection indicator
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .height(itemHeight + 8.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    RoundedCornerShape(8.dp)
                )
        )
    }
}

@Preview
@Composable
fun AddPetWeightContentPreview() {

    TailyTheme {
        AddPetWeightContent(
            viewModel = koinViewModel<AddPetViewModel>(),
            pet = null
        )
    }

}

