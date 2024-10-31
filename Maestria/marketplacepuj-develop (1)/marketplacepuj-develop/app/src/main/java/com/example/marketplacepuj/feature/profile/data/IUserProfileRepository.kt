package com.example.marketplacepuj.feature.profile.data

import com.example.marketplacepuj.feature.profile.domain.entities.ProfileEntity

interface IUserProfileRepository {

    fun getUserProfile(): ProfileEntity?
    suspend fun closeSession(): Result<Boolean>
    suspend fun changePassword(password: String): Result<Boolean>
    suspend fun updateProfile(name: String, address: String): Result<Boolean>
}