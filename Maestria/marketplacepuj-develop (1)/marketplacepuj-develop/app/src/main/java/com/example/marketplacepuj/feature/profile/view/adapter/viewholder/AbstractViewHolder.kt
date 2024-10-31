package com.example.marketplacepuj.feature.profile.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class AbstractViewHolder<in T>(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var listener: OnItemClickedListener? = null

    abstract fun bind(viewModel: T)

    fun setOnItemClickListener(listener: OnItemClickedListener) {
        this.listener = listener
    }

    interface OnItemClickedListener {
        fun onItemClicked(position: Int)
        fun onClickCloseSession()
        fun onClickChangePassword()
        fun onClickEditProfile()
        fun onClickOrderHistory()
    }
}
