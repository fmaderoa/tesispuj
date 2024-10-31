package com.example.marketplacepuj.feature.login.data

class LoginRepository(private val dataSource: FirebaseDataSource) : ILoginRepository {

    override suspend fun signIn(
        username: String,
        password: String
    ): Result<Boolean>  {
        return dataSource.signIn(username, password)
    }

    override fun isSessionActive(): Result<Boolean> {
        return Result.success(dataSource.isSessionActive())
    }
}