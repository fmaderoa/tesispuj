package com.example.marketplacepuj.feature.products.list.view.list.categories.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.databinding.CategoryItemLayoutBinding
import com.example.marketplacepuj.feature.products.list.view.list.categories.entities.Category
import com.example.marketplacepuj.feature.products.list.view.list.categories.exception.TypeNotSupportedException
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewholder.AbstractViewHolder
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewholder.CategoryViewHolder

class CategoriesTypeFactoryForList : CategoriesTypeFactory {

    override fun type(category: Category): Int = CategoryViewHolder.LAYOUT

    override fun createViewHolder(binding: ViewBinding, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CategoryViewHolder.LAYOUT -> CategoryViewHolder(binding as CategoryItemLayoutBinding)
            else -> throw TypeNotSupportedException.create(String.format("LayoutType: %d", type))
        }
    }

    override fun createViewBinding(parent: ViewGroup, type: Int): ViewBinding {
        return when (type) {
            CategoryViewHolder.LAYOUT -> {
                val binding = CategoryItemLayoutBinding.inflate(
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
