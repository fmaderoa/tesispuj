package com.example.marketplacepuj.feature.profile.view.adapter.viewmodels

import com.example.marketplacepuj.feature.profile.view.adapter.factory.MenuOptionTypeFactory

abstract class AbstractViewModel(var id: String? = null) {
    abstract fun type(typeFactory: MenuOptionTypeFactory): Int

    override fun equals(other: Any?): Boolean {
        if (id == null || other == null) {
            return super.equals(other)
        }

        return (other as AbstractViewModel).id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}