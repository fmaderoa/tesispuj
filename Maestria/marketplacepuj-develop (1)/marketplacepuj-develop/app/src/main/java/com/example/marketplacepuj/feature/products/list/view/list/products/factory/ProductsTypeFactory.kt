package com.example.marketplacepuj.feature.products.list.view.list.products.factory

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product
import com.example.marketplacepuj.feature.products.list.view.list.products.viewholder.AbstractViewHolder

interface ProductsTypeFactory {
    fun type(product: Product): Int
    fun createViewHolder(binding: ViewBinding, type: Int): AbstractViewHolder<*>
    fun createViewBinding(parent: ViewGroup, type: Int): ViewBinding
}
