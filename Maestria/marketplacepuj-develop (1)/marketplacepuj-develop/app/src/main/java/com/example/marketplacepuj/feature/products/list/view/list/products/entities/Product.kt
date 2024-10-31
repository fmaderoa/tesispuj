package com.example.marketplacepuj.feature.products.list.view.list.products.entities

import android.os.Parcelable
import com.example.marketplacepuj.core.utils.PriceFormat
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Product(
    var id: String,
    val name: String,
    val description: String,
    var quantityAvailable: Long,
    var quantitySelected: Long = 0,
    val price: BigDecimal,
    val category: String,
    val subCategory: String,
    val imageUrl: String,
    val availableSizes: Map<String, Long> = emptyMap(),
) : Parcelable {

    fun getFormattedPrice(): String = PriceFormat.apply(price)

    fun getFormattedAvailableSizes() = availableSizes.keys.map { it }.sorted().toList()

    fun getFirstQuantityAvailable(): Int {
        return getFormattedAvailableSizes().firstOrNull()?.let { first ->
            availableSizes[first]?.toInt() ?: quantityAvailable.toInt()
        } ?: quantityAvailable.toInt()
    }
}