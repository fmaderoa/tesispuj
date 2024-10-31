package com.example.marketplacepuj.feature.products.list.view.list.subcategories.factory

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.entities.SubCategory
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewholder.AbstractViewHolder

interface SubCategoryTypeFactory {
    fun type(subCategory: SubCategory): Int
    fun createViewHolder(binding: ViewBinding, type: Int): AbstractViewHolder<*>
    fun createViewBinding(parent: ViewGroup, type: Int): ViewBinding
}
