package com.example.marketplacepuj.feature.products.detail.view.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.kart.ShoppingCart
import com.example.marketplacepuj.feature.products.detail.view.theme.GrayLight
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.math.abs

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBackPressed: () -> Unit,
    onAddToCart: (Int, String?) -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val endYPercentage = 3f

    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val initQuantity =
        if (product.quantitySelected.toInt() == 0) 1 else product.quantitySelected.toInt()
    var selectedQuantity by remember { mutableIntStateOf(initQuantity) }
    var availableQuantity by remember {
        mutableIntStateOf(
            abs(
                product.getFirstQuantityAvailable() - ShoppingCart.getQuantityByProduct(
                    product
                )
            )
        )
    }

    val options = product.getFormattedAvailableSizes()
    var selectedOption by remember { mutableStateOf(options.firstOrNull()) }
    var btnEnabled by remember { mutableStateOf(product.getFirstQuantityAvailable() > 0 && availableQuantity > 0) }

    BottomSheetScaffold(
        sheetContent = {
            Text(
                text = stringResource(R.string.text_select_quantity),
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items((1..availableQuantity).toList()) { number ->
                    BottomSheetListItem(
                        number = number,
                        isSelected = selectedQuantity == number,
                    ) {
                        selectedQuantity = it
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.hide()
                        }
                    }
                    if (number < availableQuantity) {
                        HorizontalDivider(
                            color = colorResource(id = R.color.toolbarTextColor),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        },
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        maxLines = 2,
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(R.color.toolbarTextColor)
                    )
                }, navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colorResource(id = R.color.toolbarTextColor)
                        )
                    }
                })
            },
            modifier = Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xffffffff), Color(0xffebebeb)),
                    startY = 0f,
                    endY = screenHeight * endYPercentage
                )
            ),
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(data = product.imageUrl),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = product.getFormattedPrice(),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.toolbarTextColor)
                    )

                    Text(
                        text = stringResource(R.string.text_categories) + ": ${product.category}, ${product.subCategory}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.basicTextColor)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    QuantityButton(
                        quantitySelected = selectedQuantity,
                        quantityAvailable = availableQuantity,
                        enable = btnEnabled
                    ) {
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.expand()
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (options.isNotEmpty()) {
                        Spinner(
                            options = product.getFormattedAvailableSizes(),
                            selectedOption = selectedOption,
                        ) {
                            selectedOption = it
                            getAvailableQuantities(product, selectedOption).let { quantity ->
                                availableQuantity = quantity
                            }
                            if (selectedQuantity > availableQuantity) {
                                selectedQuantity = availableQuantity
                            } else if (selectedQuantity == 0) {
                                selectedQuantity = 1
                            }

                            btnEnabled = availableQuantity > 0
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        enabled = btnEnabled,
                        onClick = {
                            onAddToCart(selectedQuantity, selectedOption)
                            getAvailableQuantities(product, selectedOption).let {
                                availableQuantity = it
                            }
                            if (selectedQuantity > availableQuantity) {
                                selectedQuantity = availableQuantity
                            } else if (selectedQuantity == 0) {
                                selectedQuantity = 1
                            }
                            btnEnabled = availableQuantity > 0
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = colorResource(id = R.color.grayLight)

                        )
                    ) {
                        Text(text = stringResource(R.string.text_add_to_cart))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.text_description),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.toolbarTextColor)
                    )
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(R.color.basicTextColor)
                    )
                }
            }
        }
    }
}

private fun getAvailableQuantities(product: Product, selectedOption: String?): Int {
    val quantity = product.availableSizes[selectedOption] ?: product.quantityAvailable
    return abs(
        quantity.toInt() - ShoppingCart.getQuantityByProduct(
            product,
            selectedOption
        )
    )
}

class ProductProvider : PreviewParameterProvider<Product> {
    override val values: Sequence<Product> = sequenceOf(
        Product(
            id = "1",
            name = "Camiseta Roja",
            description = "Camiseta roja de alta calidad",
            price = BigDecimal("25.99"),
            category = "Ropa",
            subCategory = "Camisetas",
            quantitySelected = 2L,
            quantityAvailable = 14L,
            availableSizes = mapOf(Pair("S", 2), Pair("M", 3), Pair("L", 4), Pair("XL", 5)),
            imageUrl = "https://mybucketmarketplacepuj.s3.us-east-2.amazonaws.com/buzo-ladrillo_J5mJt.jpg"
        )
    )
}

@Preview
@Composable
fun ProductDetailScreenWithDataPreview(
    @PreviewParameter(ProductProvider::class) productData: Product,
) {
    ProductDetailScreen(productData, onBackPressed = {}, onAddToCart = { _: Int, _: String? -> })
}

@Preview
@Composable
fun RadioGroupPreview() {
    val options = listOf("X", "S", "M", "L", "XL")
    val (selectedOption, setSelectedOption) = remember { mutableStateOf<String?>(null) }

    RadioGroup(
        options = options,
        selectedOption = selectedOption,
        onOptionSelected = { setSelectedOption(it) }
    )
}

@Composable
fun RadioGroup(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        options.forEach { option ->
            Spacer(modifier = Modifier.width(8.dp))

            val isSelected = option == selectedOption
            RadioButton(
                selected = isSelected,
                onClick = { onOptionSelected(option) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = Color.Gray
                )
            )

            Text(
                text = option,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onOptionSelected(option) },
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun Spinner(
    options: List<String>,
    selectedOption: String?,
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption ?: "",
            onValueChange = {},
            label = { Text(stringResource(R.string.text_size_availables)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = {
                        Text(option)
                    })
            }
        }
    }
}

@Preview
@Composable
fun SpinnerPreview() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var selectedOption by remember { mutableStateOf(options.first()) }

    Spinner(
        options = options,
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it }
    )
}

@Composable
fun QuantityButton(
    text: String? = null,
    quantityAvailable: Int,
    quantitySelected: Int? = null,
    enable: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        enabled = enable,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(GrayLight.value),
            contentColor = colorResource(R.color.toolbarTextColor),
            disabledContainerColor = Color.Transparent
        ),
    ) {
        val finalText = text ?: String.format(
            pluralStringResource(
                id = R.plurals.quantity_available,
                count = quantityAvailable
            ), quantitySelected, quantityAvailable
        )
        Text(
            text = finalText,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
fun QuantityButtonPreview() {
    QuantityButton(quantityAvailable = 10, quantitySelected = 5) {}
}

@Composable
fun BottomSheetListItem(
    number: Int,
    isSelected: Boolean,
    onItemClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = isSelected, onClick = { onItemClick(number) })
            .height(55.dp)
            .background(
                color = if (isSelected) Color(GrayLight.value) else Color.Transparent,
                shape = if (isSelected) RoundedCornerShape(4.dp) else RoundedCornerShape(0.dp),
            )
            .padding(start = 15.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = String.format(
                pluralStringResource(
                    id = R.plurals.quantity,
                    count = number
                ), number
            ),
            color = colorResource(id = R.color.toolbarTextColor),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetListItemPreview() {
    BottomSheetListItem(number = 1, isSelected = true, onItemClick = { })
}
