package com.example.marketplacepuj.feature.register.data

import com.example.marketplacepuj.feature.login.data.FirebaseDataSource

class SignUpRepository(private val dataSource: FirebaseDataSource) : ISignUpRepository {

    override suspend fun signUp(
        username: String,
        password: String,
    ): Result<Boolean> {
        return dataSource.createAccount(username, password)
    }
}