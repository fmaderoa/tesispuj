package com.example.marketplacepuj.feature.profile.domain

import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.core.exception.EmptyFieldException
import com.example.marketplacepuj.feature.profile.data.IUserProfileRepository
import kotlinx.coroutines.delay

class UpdateProfileUseCase(private val repository: IUserProfileRepository) :
    AbstractUseCase<UpdateProfileUseCase.ProfileData, Result<Boolean>>() {
    override suspend fun internalExecute(): Result<Boolean> {
        val result = dataIsValid(request!!.name, request!!.address)
        return if (result.isSuccess) {
            repository.updateProfile(request!!.name, request!!.address)
        } else {
            delay(500)
            result
        }
    }

    private fun dataIsValid(
        name: String?,
        address: String?,
    ): Result<Boolean> {
        return if (name.isNullOrEmpty() || address.isNullOrEmpty()) {
            Result.failure(EmptyFieldException())
        } else {
            Result.success(true)
        }
    }

    data class ProfileData(val name: String, val address: String)
}