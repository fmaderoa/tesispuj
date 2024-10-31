package com.example.marketplacepuj.feature.profile.data

import com.example.marketplacepuj.feature.login.data.FirebaseDataSource
import com.example.marketplacepuj.feature.profile.domain.entities.ProfileEntity
import com.example.marketplacepuj.feature.profile.view.adapter.entities.MenuOptionLabeled
import com.example.marketplacepuj.feature.profile.view.adapter.viewmodels.AbstractViewModel
import com.example.marketplacepuj.feature.profile.view.adapter.viewmodels.OptionLabeledViewModel

class UserProfileRepository(private val dataSource: FirebaseDataSource) : IUserProfileRepository {

    override fun getUserProfile(): ProfileEntity? {
        return dataSource.getCurrentProfile()?.apply {
            menuOptions = getMenuOptions()
        }
    }

    private fun getMenuOptions(): List<AbstractViewModel> = listOf(
        OptionLabeledViewModel(MenuOptionLabeled.EditProfile),
        OptionLabeledViewModel(MenuOptionLabeled.ChangePassword),
        OptionLabeledViewModel(MenuOptionLabeled.OrderHistory),
        OptionLabeledViewModel(MenuOptionLabeled.CloseSession),
    )

    override suspend fun closeSession(): Result<Boolean> {
        return dataSource.closeUserSession()
    }

    override suspend fun changePassword(password: String): Result<Boolean> {
        return dataSource.changeUserPassword(password)
    }

    override suspend fun updateProfile(name: String, address: String): Result<Boolean> {
        return dataSource.updateProfile(name, address)
    }
}