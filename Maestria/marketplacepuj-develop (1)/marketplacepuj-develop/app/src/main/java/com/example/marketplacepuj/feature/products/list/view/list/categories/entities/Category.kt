package com.example.marketplacepuj.feature.products.list.view.list.categories.entities

import com.example.marketplacepuj.feature.products.list.view.list.subcategories.entities.SubCategory

data class Category(var id: String, var name: String, var subCategories: List<SubCategory>)