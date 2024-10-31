package com.example.marketplacepuj.feature.products.list.view.list.categories.viewmodels

import com.example.marketplacepuj.feature.products.list.view.list.categories.factory.CategoriesTypeFactory

abstract class AbstractViewModel(var id: String? = null) {
    var isSelected: Boolean = false

    abstract fun type(typeFactory: CategoriesTypeFactory): Int

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