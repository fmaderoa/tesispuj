package com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewmodels

import com.example.marketplacepuj.feature.products.list.view.list.subcategories.factory.SubCategoryTypeFactory

abstract class AbstractViewModel(var id: String? = null) {
    var isSelected: Boolean = false

    abstract fun type(typeFactory: SubCategoryTypeFactory): Int

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