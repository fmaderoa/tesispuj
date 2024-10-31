package com.example.marketplacepuj.feature.products.list.view.list.subcategories.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.databinding.SubCategoryItemLayoutBinding
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.entities.SubCategory
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.exception.TypeNotSupportedException
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewholder.AbstractViewHolder
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewholder.SubCategoryViewHolder

class SubCategoryTypeFactoryForList : SubCategoryTypeFactory {

    override fun type(subCategory: SubCategory): Int = SubCategoryViewHolder.LAYOUT

    override fun createViewHolder(binding: ViewBinding, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SubCategoryViewHolder.LAYOUT -> SubCategoryViewHolder(binding as SubCategoryItemLayoutBinding)
            else -> throw TypeNotSupportedException.create(String.format("LayoutType: %d", type))
        }
    }

    override fun createViewBinding(parent: ViewGroup, type: Int): ViewBinding {
        return when (type) {
            SubCategoryViewHolder.LAYOUT -> {
                val binding = SubCategoryItemLayoutBinding.inflate(
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
