package com.example.marketplacepuj.feature.cart.view.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.kart.ShoppingCart
import com.example.marketplacepuj.core.kart.entities.ShoppingCartItem
import com.example.marketplacepuj.core.utils.PriceFormat
import com.example.marketplacepuj.feature.products.detail.view.layout.BottomSheetListItem
import com.example.marketplacepuj.feature.products.detail.view.layout.QuantityButton
import com.example.marketplacepuj.feature.products.detail.view.theme.MarketPlacePUJTheme
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    products: List<ShoppingCartItem>,
    onDeleteClick: (ShoppingCartItem) -> Unit,
    createOrder: () -> Unit = {},
    showSuccessDialog: MutableState<Boolean>,
    onDismissSuccessDialog: () -> Unit = {},
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val endYPercentage = 3f

    val items = remember { mutableStateListOf(*products.toTypedArray()) }
    val totalPrice = PriceFormat.apply(ShoppingCart.totalPrice())
    var total by remember { mutableStateOf(totalPrice) }

    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val coroutineScope = rememberCoroutineScope()

    var selectedQuantity by remember { mutableIntStateOf(-1) }
    var availableQuantity by remember { mutableIntStateOf(-1) }
    var selectedItem by remember { mutableIntStateOf(-1) }
    var selectedIndex = -1

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
                        selectedItem = selectedIndex
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xff6a48bd), Color(0xff54aec5)),
                        startY = 0f,
                        endY = screenHeight * endYPercentage
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(items.size) { index ->
                    val product = items[index]

                    if (selectedItem == index) {
                        items[index].quantity = selectedQuantity.toLong()
                        ShoppingCart.addProduct(product, true)
                    }

                    CartItem(product, onItemClick = {
                        onDeleteClick(product)
                        items.remove(product)
                        total = PriceFormat.apply(ShoppingCart.totalPrice())
                    }, onClickQuantity = { _, available ->
                        availableQuantity = available
                        selectedIndex = index
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.expand()
                        }
                    })

                }
                item {
                    TotalElement(total = total, enabled = items.isNotEmpty(), createOrder)
                }
            }
        }
    }

    if (showSuccessDialog.value) {
        PurchaseSuccessDialog(
            onClickAccept = {
                showSuccessDialog.value = false
                onDismissSuccessDialog()
            }
        )
    }

}

@Preview
@Composable
fun CartScreenWithDataPreview(
    @PreviewParameter(CartItemProvider::class) products: ShoppingCartItem,
) {
    MarketPlacePUJTheme {
        val lists = listOf(products, products, products)
        CartScreen(
            products = lists,
            onDeleteClick = { },
            showSuccessDialog = remember { mutableStateOf(false) })
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CartItem(
    product: ShoppingCartItem,
    onItemClick: () -> Unit,
    onClickQuantity: (Int, Int) -> Unit = { _: Int, _: Int -> },
) {
    val selected = product.quantity.toInt()
    val available = abs(
        ShoppingCart.getInventoryByIdProduct(
            product.productId,
            product.classification
        )
    )

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.white),
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(product.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .absolutePadding(left = 8.dp, top = 8.dp, bottom = 8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            product.classification?.let { classification ->
                Text(
                    text = "Talla: $classification",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            QuantityButton(
                text = stringResource(R.string.text_quantity, selected),
                quantitySelected = selected,
                quantityAvailable = available
            ) {
                onClickQuantity(selected, available)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = PriceFormat.apply(product.price),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            onClick = onItemClick,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun CartItemPreview(
    @PreviewParameter(CartItemProvider::class) product: ShoppingCartItem,
) {
    CartItem(product = product, onItemClick = { })
}

class CartItemProvider : PreviewParameterProvider<ShoppingCartItem> {
    override val values = sequenceOf(
        ShoppingCartItem(
            productId = "1",
            quantity = 2L,
            price = BigDecimal("25.99"),
            classification = "XXS",
            name = "Camiseta Roja",
            imageUrl = "https://mybucketmarketplacepuj.s3.us-east-2.amazonaws.com/buzo-ladrillo_J5mJt.jpg",
        )
    )
}

@Composable
fun TotalElement(total: String, enabled: Boolean = true, createOrder: () -> Unit = {}) {
    val openPurchaseDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Texto "Total:"
        Text(
            color = Color(0xff79b0d0),
            text = "Total:",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
        // Valor del total
        Text(
            text = total,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Botón "Pagar"
        Button(
            enabled = enabled,
            onClick = {
                openPurchaseDialog.value = true
            },
            modifier = Modifier.align(Alignment.End),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(text = stringResource(R.string.text_pay))
        }

        if (openPurchaseDialog.value) {
            PurchaseDialog(openDialogCustom = openPurchaseDialog, createOrder)
        }
    }
}

@Preview
@Composable
fun TotalElementPreview(@PreviewParameter(TotalProvider::class) total: String) {
    MarketPlacePUJTheme {
        TotalElement(total = total)
    }
}

class TotalProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("$50.00")
}
