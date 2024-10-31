package com.example.marketplacepuj.feature.profile.view.adapter.factory

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.marketplacepuj.feature.profile.view.adapter.entities.MenuOptionLabeled
import com.example.marketplacepuj.feature.profile.view.adapter.viewholder.AbstractViewHolder

interface MenuOptionTypeFactory {
    fun type(optionLabeled: MenuOptionLabeled): Int
    fun createViewHolder(binding: ViewBinding, type: Int): AbstractViewHolder<*>
    fun createViewBinding(parent: ViewGroup, type: Int): ViewBinding
}
