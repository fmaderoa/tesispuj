package com.example.marketplacepuj.feature.products.list.view.list.categories.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.feature.products.list.view.list.categories.entities.Category

abstract class AbstractViewHolder<in T>(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var listener: OnItemClickedListener? = null

    abstract fun bind(viewModel: T)

    fun setOnItemClickListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    interface OnItemClickedListener {
        fun onCategoryClicked(categories: Category, adapterPosition: Int)
    }
}
