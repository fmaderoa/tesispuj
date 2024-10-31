package com.example.marketplacepuj.feature.products.list.view.list.categories.viewmodels

import com.example.marketplacepuj.feature.products.list.view.list.categories.entities.Category
import com.example.marketplacepuj.feature.products.list.view.list.categories.factory.CategoriesTypeFactory

class CategoryViewModel(var category: Category) : AbstractViewModel() {

    init {
        id = category.id
    }

    override fun type(typeFactory: CategoriesTypeFactory): Int {
        return typeFactory.type(category)
    }
}
