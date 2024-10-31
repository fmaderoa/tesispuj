package com.example.marketplacepuj.feature.cart.data

import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity
import com.example.marketplacepuj.feature.login.data.FirebaseDataSource

class CreateOrderRepository(private val dataSource: FirebaseDataSource) : ICreateOrder {


    override suspend fun createOrder(items: OrderEntity): Result<Boolean> {
       return dataSource.createOrder(items)
    }
}