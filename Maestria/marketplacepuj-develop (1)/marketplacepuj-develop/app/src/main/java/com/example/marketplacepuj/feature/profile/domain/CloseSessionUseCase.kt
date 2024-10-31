package com.example.marketplacepuj.feature.profile.domain

import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.feature.profile.data.IUserProfileRepository

class CloseSessionUseCase(private val repository: IUserProfileRepository) :
    AbstractUseCase<Nothing, Result<Boolean>>() {
    override suspend fun internalExecute(): Result<Boolean> {
        return repository.closeSession()
    }
}