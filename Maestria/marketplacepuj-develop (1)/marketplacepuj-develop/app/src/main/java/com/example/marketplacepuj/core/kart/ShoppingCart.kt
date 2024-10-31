package com.example.marketplacepuj.core.kart

import com.example.marketplacepuj.core.kart.entities.ShoppingCartItem
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

object ShoppingCart {

    var products: MutableList<ShoppingCartItem> = mutableListOf()

    private var _totalQuantity = MutableStateFlow(0)
    val totalQuantity: StateFlow<Int> get() = _totalQuantity.asStateFlow()

    fun addProduct(product: ShoppingCartItem, replace: Boolean = false) {
        products.firstOrNull { it.productId == product.productId && it.classification == product.classification }
            ?.let {
                if (replace) {
                    products.remove(it)
                    products.add(product)
                } else it.quantity += product.quantity
            } ?: products.add(product)
        updateQuantity()
    }

    fun removeProduct(product: ShoppingCartItem) {
        products.remove(product)
        updateQuantity()
    }

    private fun updateQuantity() {
        _totalQuantity.value = totalQuantity()
    }

    fun clear() {
        products.clear()
        updateQuantity()
    }

    private fun totalQuantity(): Int {
        return if (products.isEmpty()) 0 else products.map { it.quantity.toInt() }
            .reduce { acc, num -> acc + num }

    }

    fun totalPrice(): BigDecimal {
        return if (products.isEmpty()) BigDecimal.ZERO else products.map { it.totalPrice() }
            .reduce { acc, num -> acc + num }
    }

    fun getQuantityByProduct(product: Product, classification: String? = null): Int {
        return products.firstOrNull {
            product.id == it.productId && (it.classification == classification || classification == null)
        }?.quantity?.toInt() ?: return 0
    }

    fun getInventoryByIdProduct(productId: String, classification: String? = null): Int {
        return products.firstOrNull {
            productId == it.productId && (it.classification == classification || classification == null)
        }?.stock?.toInt() ?: return 0
    }

    fun toShoppingCartItem(
        product: Product,
        selectedQuantity: Int,
        sizeSelected: String?,
    ): ShoppingCartItem {
        return ShoppingCartItem(
            productId = product.id,
            quantity = selectedQuantity.toLong(),
            price = product.price,
            classification = sizeSelected,
            imageUrl = product.imageUrl,
            name = product.name,
            stock = product.quantityAvailable
        )
    }
}


