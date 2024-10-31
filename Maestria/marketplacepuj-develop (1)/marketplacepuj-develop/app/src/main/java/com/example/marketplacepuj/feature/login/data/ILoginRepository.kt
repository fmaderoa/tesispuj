package com.example.marketplacepuj.feature.login.data

interface ILoginRepository {
    suspend fun signIn(
        username: String,
        password: String,
    ): Result<Boolean>

    fun isSessionActive(): Result<Boolean>
}