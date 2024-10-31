package com.example.marketplacepuj.feature.profile.domain

import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.feature.profile.data.IUserProfileRepository
import com.example.marketplacepuj.feature.profile.domain.entities.ProfileEntity

class GetUserProfileUseCase(private val repository: IUserProfileRepository) :
    AbstractUseCase<Nothing, Result<ProfileEntity>>() {

    override suspend fun internalExecute(): Result<ProfileEntity> {
        return repository.getUserProfile()?.let { Result.success(it) }
            ?: Result.failure(Exception())
    }
}