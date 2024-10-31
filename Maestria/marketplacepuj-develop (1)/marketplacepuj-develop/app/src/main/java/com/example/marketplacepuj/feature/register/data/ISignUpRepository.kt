package com.example.marketplacepuj.feature.register.data

interface ISignUpRepository {
    suspend fun signUp(
        username: String,
        password: String,
    ): Result<Boolean>
}