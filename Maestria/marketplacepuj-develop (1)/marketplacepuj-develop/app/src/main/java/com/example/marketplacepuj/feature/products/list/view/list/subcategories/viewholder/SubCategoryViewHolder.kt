package com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewholder

import android.view.View
import com.example.marketplacepuj.R
import com.example.marketplacepuj.databinding.SubCategoryItemLayoutBinding
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.entities.SubCategory
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewmodels.SubCategoryViewModel


class SubCategoryViewHolder(private val binding: SubCategoryItemLayoutBinding) :
    AbstractViewHolder<SubCategoryViewModel>(binding),
    View.OnClickListener {

    companion object {
        val LAYOUT: Int = R.layout.sub_category_item_layout
    }

    private lateinit var subCategory: SubCategory

    override fun onClick(v: View) {
        listener?.onSubCategoryClicked(subCategory, adapterPosition)
    }

    override fun bind(viewModel: SubCategoryViewModel) {
        subCategory = viewModel.subCategory
        binding.tvCategory.text = viewModel.subCategory.name
        when (viewModel.isSelected) {
            true -> binding.toggleLayout.toggleOn()
            false -> binding.toggleLayout.toggleOff()
        }
        binding.toggleLayout.setOnClickListener(this)
    }
}
