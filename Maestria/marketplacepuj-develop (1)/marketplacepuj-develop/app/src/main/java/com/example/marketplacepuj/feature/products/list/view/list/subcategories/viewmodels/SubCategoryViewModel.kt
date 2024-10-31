package com.example.marketplacepuj.feature.products.list.view.list.subcategories.viewmodels

import com.example.marketplacepuj.feature.products.list.view.list.subcategories.entities.SubCategory
import com.example.marketplacepuj.feature.products.list.view.list.subcategories.factory.SubCategoryTypeFactory

class SubCategoryViewModel(var subCategory: SubCategory) : AbstractViewModel() {

    init {
        id = subCategory.id
    }

    override fun type(typeFactory: SubCategoryTypeFactory): Int {
        return typeFactory.type(subCategory)
    }
}
