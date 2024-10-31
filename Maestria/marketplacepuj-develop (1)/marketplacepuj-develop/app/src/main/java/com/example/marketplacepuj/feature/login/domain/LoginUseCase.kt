package com.example.marketplacepuj.feature.login.domain

import android.util.Patterns
import com.example.marketplacepuj.core.components.usecase.AbstractUseCase
import com.example.marketplacepuj.core.exception.InvalidUserPasswordException
import com.example.marketplacepuj.feature.login.data.ILoginRepository
import kotlinx.coroutines.delay

class LoginUseCase(private val repository: ILoginRepository):
    AbstractUseCase<LoginUseCase.UserData, Result<Boolean>>() {

    private fun dataIsValid(username: String?, password: String?): Boolean {
        return if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            false
        } else {
            val isEmail = Patterns.EMAIL_ADDRESS.matcher(username).matches()
            password.isNotEmpty() && isEmail
        }
    }

    override suspend fun internalExecute(): Result<Boolean> {
        val username = request!!.username
        val password = request!!.password
        return if (dataIsValid(username, password)) {
            repository.signIn(username, password)
        } else {
            delay(500)
            Result.failure(InvalidUserPasswordException())
        }
    }

    class UserData(val username: String, val password: String)
}