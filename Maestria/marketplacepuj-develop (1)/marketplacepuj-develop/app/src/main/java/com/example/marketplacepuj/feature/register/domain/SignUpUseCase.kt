package com.example.marketplacepuj.feature.register.domain

import android.util.Patterns
import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.core.exception.BadEmailFieldException
import com.example.marketplacepuj.core.exception.BadPasswordFieldException
import com.example.marketplacepuj.core.exception.BadPasswordLengthException
import com.example.marketplacepuj.core.exception.EmptyFieldException
import com.example.marketplacepuj.feature.register.data.ISignUpRepository
import kotlinx.coroutines.delay

class SignUpUseCase(private val repository: ISignUpRepository) :
    AbstractUseCase<SignUpUseCase.UserData, Result<Boolean>>() {

    private fun dataIsValid(
        username: String?,
        password: String?,
        password2: String?,
    ): Result<Boolean> {
        return if (username.isNullOrEmpty() || password.isNullOrEmpty() || password2.isNullOrEmpty()) {
            Result.failure(EmptyFieldException())
        } else {
            val isEmail = Patterns.EMAIL_ADDRESS.matcher(username).matches()
            val passwordsMatch = password == password2
            if (!isEmail) {
                Result.failure(BadEmailFieldException())
            } else if (!passwordsMatch) {
                Result.failure(BadPasswordFieldException())
            } else if (password.length < 6) {
                Result.failure(BadPasswordLengthException())
            } else {
                Result.success(true)
            }
        }
    }

    override suspend fun internalExecute(): Result<Boolean> {
        val username = request!!.username
        val password = request!!.password
        val password2 = request!!.password2
        val result = dataIsValid(username, password, password2)
        return if (result.isSuccess) {
            repository.signUp(username, password)
        } else {
            delay(500)
            result
        }
    }

    class UserData(val username: String, val password: String, val password2: String)
}