package com.example.marketplacepuj.feature.orders.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplacepuj.core.components.usecase.catch
import com.example.marketplacepuj.core.components.usecase.onStart
import com.example.marketplacepuj.core.components.usecase.result
import com.example.marketplacepuj.feature.cart.data.entity.OrderEntity
import com.example.marketplacepuj.feature.orders.domain.RetrieveOrdersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(private val retrieveOrders: RetrieveOrdersUseCase) : ViewModel() {
    private val _state = MutableStateFlow<OrdersState>(OrdersState.Init)
    val state = _state.asStateFlow()

    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            retrieveOrders().onStart {
                _state.value = OrdersState.Loading
            }.catch {
                _state.value = OrdersState.Error(it)
            }.result { result ->
                when (result.isSuccess) {
                    true -> _state.value = OrdersState.Success(result.getOrThrow())
                    false -> _state.value = OrdersState.Error(result.exceptionOrNull()!!)
                }
            }
        }
    }

    sealed class OrdersState {
        data object Init : OrdersState()
        data object Loading : OrdersState()
        data class Error(val exception: Throwable) : OrdersState()
        data class Success(val data: List<OrderEntity>) : OrdersState()
    }
}