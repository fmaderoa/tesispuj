package com.example.marketplacepuj.feature.orders.data

import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity

interface IRetrieveOrders {

    suspend fun getOrders(): Result<List<OrderEntity>>
}