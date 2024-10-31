package com.example.marketplacepuj.feature.products.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplacepuj.core.components.usecase.catch
import com.example.marketplacepuj.core.components.usecase.onStart
import com.example.marketplacepuj.core.components.usecase.result
import com.example.marketplacepuj.feature.products.list.domain.GetCategoriesUseCase
import com.example.marketplacepuj.feature.products.list.domain.GetProductsUseCase
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewmodels.AbstractViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.marketplacepuj.feature.products.list.view.list.products.viewmodels.AbstractViewModel as ProductsAbstractViewModel

class ProductsViewModel(
    private val getProducts: GetProductsUseCase,
    private val categories: GetCategoriesUseCase,
) : ViewModel() {
    private val _productsState = MutableSharedFlow<ProductsState>(replay = 1)
    val productsState: SharedFlow<ProductsState> get() = _productsState.asSharedFlow()

    private val _categoriesState = MutableStateFlow<CategoriesState>(CategoriesState.Init)
    val categoriesState: StateFlow<CategoriesState> get() = _categoriesState.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getProducts()
                .catch {
                    _productsState.tryEmit(ProductsState.Error(it))
                }.result { result ->
                    when (result.isSuccess) {
                        true -> _productsState.tryEmit(ProductsState.SuccessProducts(result.getOrThrow()))
                        false -> _productsState.tryEmit(ProductsState.Error(result.exceptionOrNull()!!))
                    }
                }
        }
    }

    fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            categories()
                .onStart {
                    _categoriesState.value = CategoriesState.Loading
                }.catch {
                    _categoriesState.value = CategoriesState.Error(it)
                }.result { result ->
                    when (result.isSuccess) {
                        true -> _categoriesState.value =
                            CategoriesState.SuccessCategories(result.getOrThrow())

                        false -> _categoriesState.value =
                            CategoriesState.Error(result.exceptionOrNull()!!)
                    }
                }
        }
    }

    sealed class ProductsState {
        data class SuccessProducts(val products: List<ProductsAbstractViewModel>) : ProductsState()
        data class Error(val exception: Throwable) : ProductsState()
    }

    sealed class CategoriesState {
        data object Init : CategoriesState()
        data object Loading : CategoriesState()
        data class SuccessCategories(val categories: List<AbstractViewModel>) : CategoriesState()
        data class Error(val exception: Throwable) : CategoriesState()
    }
}