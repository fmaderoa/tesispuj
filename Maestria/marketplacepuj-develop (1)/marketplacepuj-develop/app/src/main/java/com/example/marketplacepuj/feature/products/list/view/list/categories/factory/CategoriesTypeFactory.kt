package com.example.marketplacepuj.feature.products.list.view.list.categories.factory

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.feature.products.list.view.list.categories.entities.Category
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewholder.AbstractViewHolder

interface CategoriesTypeFactory {
    fun type(category: Category): Int
    fun createViewHolder(binding: ViewBinding, type: Int): AbstractViewHolder<*>
    fun createViewBinding(parent: ViewGroup, type: Int): ViewBinding
}
