package com.example.marketplacepuj.core.kart.entities

import java.math.BigDecimal

data class ShoppingCartItem(
    val productId: String,
    var quantity: Long,
    val price: BigDecimal,
    val classification: String? = null,
    val name: String,
    val imageUrl: String,
    val stock: Long = 0,
) {

    fun totalPrice(): BigDecimal = price.multiply(BigDecimal(quantity))
}
