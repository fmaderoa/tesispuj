package com.example.marketplacepuj.feature.products.list.view.list.products.viewmodels

import com.example.marketplacepuj.feature.products.list.view.list.products.factory.ProductsTypeFactory

abstract class AbstractViewModel(var id: String? = null) {
    abstract fun type(typeFactory: ProductsTypeFactory): Int

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