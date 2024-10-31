package com.example.marketplacepuj.feature.products.list.data

import com.example.marketplacepuj.feature.products.list.view.list.categories.entities.Category
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product

interface IProductsRepository {

    suspend fun getProducts(): List<Product>?

    suspend fun getCategories(): List<Category>?
}