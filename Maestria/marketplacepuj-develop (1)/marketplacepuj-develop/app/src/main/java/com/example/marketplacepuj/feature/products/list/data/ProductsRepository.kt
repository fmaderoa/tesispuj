package com.example.marketplacepuj.feature.products.list.data

import com.example.marketplacepuj.feature.login.data.FirebaseDataSource
import com.example.marketplacepuj.feature.products.list.view.list.categories.entities.Category
import com.example.marketplacepuj.feature.products.list.view.list.products.entities.Product

class ProductsRepository(private val dataSource: FirebaseDataSource) :
    IProductsRepository {

    override suspend fun getProducts(): List<Product>? {
        return dataSource.getProducts()
    }

    override suspend fun getCategories(): List<Category>? {
        return dataSource.getCategories()
    }
}