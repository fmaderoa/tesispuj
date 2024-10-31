package com.example.marketplacepuj.feature.profile.view.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.example.marketplacepuj.R
import com.example.marketplacepuj.databinding.MenuLabeledItemLayoutBinding
import com.example.marketplacepuj.feature.profile.view.adapter.entities.MenuOptionLabeled
import com.example.marketplacepuj.feature.profile.view.adapter.viewmodels.OptionLabeledViewModel


class TerminalLabeledViewHolder(private val binding: MenuLabeledItemLayoutBinding) :
    AbstractViewHolder<OptionLabeledViewModel>(binding),
    View.OnClickListener {

    private lateinit var model: MenuOptionLabeled

    companion object {
        val LAYOUT: Int = R.layout.menu_labeled_item_layout
    }

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (model) {
            MenuOptionLabeled.CloseSession -> listener?.onClickCloseSession()
            MenuOptionLabeled.ChangePassword -> listener?.onClickChangePassword()
            MenuOptionLabeled.EditProfile -> listener?.onClickEditProfile()
            MenuOptionLabeled.OrderHistory -> listener?.onClickOrderHistory()
        }
        listener?.onItemClicked(adapterPosition)
    }

    override fun bind(viewModel: OptionLabeledViewModel) {
        model = viewModel.optionLabeled
        binding.tvLabel.text = itemView.context.getString(viewModel.optionLabeled.label)
        ContextCompat.getDrawable(itemView.context, viewModel.optionLabeled.icon)?.let { drawable ->
            binding.tvLabel.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawable,
                null,
                null,
                null
            )
        }

    }
}
