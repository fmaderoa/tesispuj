package com.example.marketplacepuj.feature.products.list.view.list.products.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product

abstract class AbstractViewHolder<in T>(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var listener: OnItemClickedListener? = null

    abstract fun bind(viewModel: T)

    fun setOnItemClickListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    interface OnItemClickedListener {
        fun onClickProduct(product: Product)
    }
}
