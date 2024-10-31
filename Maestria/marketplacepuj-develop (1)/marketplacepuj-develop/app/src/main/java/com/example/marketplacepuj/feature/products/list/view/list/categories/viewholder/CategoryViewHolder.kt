package com.example.marketplacepuj.feature.products.list.view.list.categories.viewholder

import android.view.View
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.components.view.ToggleLayout
import com.example.marketplacepuj.databinding.CategoryItemLayoutBinding
import com.example.marketplacepuj.feature.products.list.view.list.categories.entities.Category
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewmodels.CategoryViewModel


class CategoryViewHolder(private val binding: CategoryItemLayoutBinding) :
    AbstractViewHolder<CategoryViewModel>(binding),
    View.OnClickListener {

    companion object {
        var LAYOUT: Int = R.layout.category_item_layout
    }

    private var category: Category? = null

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onCategoryClicked(category!!, adapterPosition)
    }

    override fun bind(viewModel: CategoryViewModel) {
        category = viewModel.category
        binding.tvCategory.text = viewModel.category.name
        when (viewModel.isSelected) {
            true -> (itemView as ToggleLayout).toggleOn()
            false -> (itemView as ToggleLayout).toggleOff()
        }
    }
}
