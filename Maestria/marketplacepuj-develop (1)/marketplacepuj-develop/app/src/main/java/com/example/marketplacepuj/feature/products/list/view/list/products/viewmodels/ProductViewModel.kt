package com.example.marketplacepuj.feature.products.list.view.list.products.viewmodels

import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product
import com.example.marketplacepuj.feature.products.list.view.list.products.factory.ProductsTypeFactory

class ProductViewModel(var product: Product) : AbstractViewModel() {

    init {
        id = product.id
    }

    override fun type(typeFactory: ProductsTypeFactory): Int {
        return typeFactory.type(product)
    }
}
