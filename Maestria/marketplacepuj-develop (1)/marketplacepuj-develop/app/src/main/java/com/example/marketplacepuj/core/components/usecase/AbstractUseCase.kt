package com.example.marketplacepuj.core.components.usecase

import androidx.annotation.CallSuper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class AbstractUseCase<REQUEST, RESPONSE> {
    private val useCase
        get() = UseCase(this)

    var request: REQUEST? = null

    abstract suspend fun internalExecute(): RESPONSE

    @CallSuper
    open suspend operator fun invoke(request: REQUEST? = null): UseCase<RESPONSE> =
        withContext(Dispatchers.IO) {
            this@AbstractUseCase.request = request
            useCase
        }
}