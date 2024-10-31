package com.example.marketplacepuj.feature.products.list.domain

import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.core.exception.CategoriesException
import com.example.marketplacepuj.core.exception.EmptyCategoriesException
import com.example.marketplacepuj.feature.products.list.data.IProductsRepository
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewmodels.AbstractViewModel
import com.example.marketplacepuj.feature.products.list.view.list.categories.viewmodels.CategoryViewModel

class GetCategoriesUseCase(private val repository: IProductsRepository) :
    AbstractUseCase<Unit, Result<List<AbstractViewModel>>>() {
    override suspend fun internalExecute(): Result<List<AbstractViewModel>> {
        return when (val categories = repository.getCategories()) {
            null -> Result.failure(CategoriesException())
            else -> {
                if (categories.isEmpty()) Result.failure(EmptyCategoriesException())
                else {
                    val models: List<AbstractViewModel> = categories.map { CategoryViewModel(it) }
                    Result.success(models)
                }
            }
        }
    }
}