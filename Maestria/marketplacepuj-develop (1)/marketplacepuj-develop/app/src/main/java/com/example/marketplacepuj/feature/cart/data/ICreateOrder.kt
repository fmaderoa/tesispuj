package com.example.marketplacepuj.feature.cart.data

import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity

interface ICreateOrder {
    suspend fun createOrder(items: OrderEntity): Result<Boolean>
}