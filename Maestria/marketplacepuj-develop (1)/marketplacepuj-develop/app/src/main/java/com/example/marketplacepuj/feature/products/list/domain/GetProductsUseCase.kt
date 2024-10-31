package com.example.marketplacepuj.feature.products.list.domain

import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.core.exception.EmptyProductsException
import com.example.marketplacepuj.core.exception.ProductsException
import com.example.marketplacepuj.feature.products.list.data.IProductsRepository
import com.example.marketplacepuj.feature.products.list.view.list.products.viewmodels.AbstractViewModel
import com.example.marketplacepuj.feature.products.list.view.list.products.viewmodels.ProductViewModel

class GetProductsUseCase(private val repository: IProductsRepository) :
    AbstractUseCase<Unit, Result<List<AbstractViewModel>>>() {

    override suspend fun internalExecute(): Result<List<AbstractViewModel>> {
        return when (val products = repository.getProducts()) {
            null -> Result.failure(ProductsException())
            else -> {
                if (products.isEmpty()) Result.failure(EmptyProductsException())
                else {
                    val models: List<AbstractViewModel> = products.map { ProductViewModel(it) }
                    Result.success(models)
                }
            }
        }
    }
}