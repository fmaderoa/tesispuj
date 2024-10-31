package com.example.marketplacepuj.feature.profile.domain

import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.core.exception.BadPasswordFieldException
import com.example.marketplacepuj.core.exception.BadPasswordLengthException
import com.example.marketplacepuj.core.exception.EmptyFieldException
import com.example.marketplacepuj.feature.profile.data.IUserProfileRepository
import kotlinx.coroutines.delay

class ChangePasswordUseCase(private val repository: IUserProfileRepository) :
    AbstractUseCase<ChangePasswordUseCase.ChangePassword, Result<Boolean>>() {
    override suspend fun internalExecute(): Result<Boolean> {
        val result = dataIsValid(request!!.password, request!!.newPassword)
        return if (result.isSuccess) {
            repository.changePassword(request!!.password)
        } else {
            delay(500)
            result
        }
    }

    private fun dataIsValid(
        password: String?,
        password2: String?,
    ): Result<Boolean> {
        return if (password.isNullOrEmpty() || password2.isNullOrEmpty()) {
            Result.failure(EmptyFieldException())
        } else {
            val passwordsMatch = password == password2
            if (!passwordsMatch) {
                Result.failure(BadPasswordFieldException())
            } else if (password.length < 6) {
                Result.failure(BadPasswordLengthException())
            } else {
                Result.success(true)
            }
        }
    }

    data class ChangePassword(val password: String, val newPassword: String)
}