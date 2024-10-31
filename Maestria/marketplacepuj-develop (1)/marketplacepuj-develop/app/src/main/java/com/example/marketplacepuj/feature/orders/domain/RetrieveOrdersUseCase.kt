package com.example.marketplacepuj.feature.orders.domain

import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity
import com.example.marketplacepuj.feature.orders.data.IRetrieveOrders

class RetrieveOrdersUseCase(private val repository: IRetrieveOrders) :
    AbstractUseCase<Nothing, Result<List<OrderEntity>>>() {

    override suspend fun internalExecute(): Result<List<OrderEntity>> {
        return repository.getOrders()
    }
}