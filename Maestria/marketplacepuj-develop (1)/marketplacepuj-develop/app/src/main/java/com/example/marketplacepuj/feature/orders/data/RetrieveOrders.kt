package com.example.marketplacepuj.feature.orders.data

import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity
import com.example.marketplacepuj.feature.login.data.FirebaseDataSource

class RetrieveOrders(private val dataSource: FirebaseDataSource) : IRetrieveOrders {

    override suspend fun getOrders(): Result<List<OrderEntity>> {
        return dataSource.getOrderHistory()
    }
}