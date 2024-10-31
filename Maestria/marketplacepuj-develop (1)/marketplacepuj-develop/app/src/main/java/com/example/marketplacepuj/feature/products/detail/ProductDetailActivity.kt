package com.example.marketplacepuj.feature.products.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.navArgs
import com.example.marketplacepuj.core.kart.ShoppingCart
import com.example.marketplacepuj.feature.products.detail.view.layout.ProductDetailScreen
import com.example.marketplacepuj.feature.products.detail.view.layout.ProductProvider
import com.example.marketplacepuj.feature.products.detail.view.theme.MarketPlacePUJTheme
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product

class ProductDetailActivity : ComponentActivity() {

    private val args: ProductDetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MarketPlacePUJTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ProductDetailScreen(
                        args.product,
                        onAddToCart = { selectedQuantity: Int, sizeSelected: String? ->
                        ShoppingCart.toShoppingCartItem(
                            args.product,
                            selectedQuantity,
                            sizeSelected
                        ).let {
                            ShoppingCart.addProduct(it)
                        }
                        },
                        onBackPressed = {
                            finish()
                        })
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview(
    @PreviewParameter(ProductProvider::class) productData: Product,
) {
    MarketPlacePUJTheme {
        ProductDetailScreen(
            productData,
            onAddToCart = { _: Int, _: String? -> },
            onBackPressed = {})
    }
}
