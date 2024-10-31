package com.example.marketplacepuj.feature.profile.view.adapter.viewmodels

import com.example.marketplacepuj.feature.profile.view.adapter.entities.MenuOptionLabeled
import com.example.marketplacepuj.feature.profile.view.adapter.factory.MenuOptionTypeFactory

class OptionLabeledViewModel(var optionLabeled: MenuOptionLabeled) : AbstractViewModel() {

    init {
        id = optionLabeled.id
    }

    override fun type(typeFactory: MenuOptionTypeFactory): Int {
        return typeFactory.type(optionLabeled)
    }
}
