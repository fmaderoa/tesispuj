package com.example.marketplacepuj.feature.login.domain

import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.feature.login.data.ILoginRepository

class CheckLoginUseCase(private val repository: ILoginRepository): AbstractUseCase<Any, Result<Boolean>>() {

    override suspend fun internalExecute(): Result<Boolean> {
        return repository.isSessionActive()
    }

}