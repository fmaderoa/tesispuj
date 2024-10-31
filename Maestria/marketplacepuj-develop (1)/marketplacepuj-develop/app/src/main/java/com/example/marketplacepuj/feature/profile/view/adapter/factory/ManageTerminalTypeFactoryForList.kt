package com.example.marketplacepuj.feature.profile.view.adapter.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.databinding.MenuLabeledItemLayoutBinding
import com.example.marketplacepuj.feature.profile.view.adapter.entities.MenuOptionLabeled
import com.example.marketplacepuj.feature.profile.view.adapter.exception.TypeNotSupportedException
import com.example.marketplacepuj.feature.profile.view.adapter.viewholder.AbstractViewHolder
import com.example.marketplacepuj.feature.profile.view.adapter.viewholder.TerminalLabeledViewHolder

class ManageTerminalTypeFactoryForList : MenuOptionTypeFactory {

    override fun type(optionLabeled: MenuOptionLabeled): Int = TerminalLabeledViewHolder.LAYOUT

    override fun createViewHolder(binding: ViewBinding, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TerminalLabeledViewHolder.LAYOUT -> TerminalLabeledViewHolder(binding as MenuLabeledItemLayoutBinding)
            else -> throw TypeNotSupportedException.create(String.format("LayoutType: %d", type))
        }
    }

    override fun createViewBinding(parent: ViewGroup, type: Int): ViewBinding {
        return when (type) {

            TerminalLabeledViewHolder.LAYOUT -> {
                val binding = MenuLabeledItemLayoutBinding.inflate(
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
