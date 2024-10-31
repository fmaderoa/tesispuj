package com.example.marketplacepuj.feature.products.list.view.list.products.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.databinding.ProductItemLayoutBinding
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product
import com.example.marketplacepuj.feature.products.list.view.list.products.exception.TypeNotSupportedException
import com.example.marketplacepuj.feature.products.list.view.list.products.viewholder.AbstractViewHolder
import com.example.marketplacepuj.feature.products.list.view.list.products.viewholder.ProductViewHolder

class ProductsTypeFactoryForList : ProductsTypeFactory {

    override fun type(product: Product): Int = ProductViewHolder.LAYOUT

    override fun createViewHolder(binding: ViewBinding, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductViewHolder.LAYOUT -> ProductViewHolder(binding as ProductItemLayoutBinding)
            else -> throw TypeNotSupportedException.create(String.format("LayoutType: %d", type))
        }
    }

    override fun createViewBinding(parent: ViewGroup, type: Int): ViewBinding {
        return when (type) {
            ProductViewHolder.LAYOUT -> {
                val binding = ProductItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding
            }

            else -> throw TypeNotSupportedException.create(
                String.format(
                    "ViewBindingType: %d",
                    type
                )
            )
        }
    }
}
