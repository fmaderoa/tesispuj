package com.example.marketplacepuj.feature.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplacepuj.core.components.usecase.catch
import com.example.marketplacepuj.core.components.usecase.onStart
import com.example.marketplacepuj.core.components.usecase.result
import com.example.marketplacepuj.core.kart.entities.ShoppingCartItem
import com.example.marketplacepuj.feature.cart.domain.CreateOrder
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingCartViewModel(private val createOrder: CreateOrder) : ViewModel() {

    private val  _state = MutableStateFlow<CreateOrderState>(CreateOrderState.Idle)

    val state = _state.asStateFlow()

    fun sendOrder(products: MutableList<ShoppingCartItem>) {
        val myTrace = Firebase.performance.newTrace("custom-trace-save-order-MANUAL")
        myTrace.start()
        viewModelScope.launch(Dispatchers.IO) {
            createOrder(products)
                .onStart { _state.value = CreateOrderState.Loading }
                .catch { throwable ->
                    _state.value = CreateOrderState.Error(throwable)
                }
                .result { result ->
                    myTrace.stop()
                    when (result.isSuccess) {
                        true -> _state.value = CreateOrderState.Success
                        false -> _state.value = CreateOrderState.Error(result.exceptionOrNull())
                    }
                }
        }
    }

    sealed class CreateOrderState {
        data object Idle : CreateOrderState()
        data object Loading : CreateOrderState()
        data object Success : CreateOrderState()
        data class Error(val throwable: Throwable?) : CreateOrderState()
    }
}