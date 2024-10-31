package com.example.marketplacepuj.feature.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.components.usecase.catch
import com.example.marketplacepuj.core.components.usecase.onStart
import com.example.marketplacepuj.core.components.usecase.result
import com.example.marketplacepuj.core.exception.ProfileErrorException
import com.example.marketplacepuj.feature.profile.domain.ChangePasswordUseCase
import com.example.marketplacepuj.feature.profile.domain.CloseSessionUseCase
import com.example.marketplacepuj.feature.profile.domain.GetUserProfileUseCase
import com.example.marketplacepuj.feature.profile.domain.UpdateProfileUseCase
import com.example.marketplacepuj.feature.profile.domain.entities.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userProfile: GetUserProfileUseCase,
    private val closeSession: CloseSessionUseCase,
    private val changePassword: ChangePasswordUseCase,
    private val updateProfile: UpdateProfileUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<UserProfileState>(UserProfileState.Init)
    val state = _state.asStateFlow()

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            userProfile()
                .onStart {
                    _state.value = UserProfileState.Loading()
                }.catch {
                    _state.value = UserProfileState.Error(it)
                }.result { profile ->
                    when (profile.isSuccess) {
                        true -> _state.value = UserProfileState.Success(profile.getOrThrow())
                        false -> _state.value = UserProfileState.Error(ProfileErrorException())
                    }
                }
        }
    }

    fun closeUserSession() {
        viewModelScope.launch(Dispatchers.IO) {
            closeSession().onStart {
                _state.value = UserProfileState.Loading(R.string.closing_session)
            }.catch {
                _state.value = UserProfileState.Error(it)
            }.result {
                when (it.getOrThrow()) {
                    true -> _state.value = UserProfileState.SessionClosed
                    false -> _state.value = UserProfileState.Error(Exception())
                }
            }
        }
    }

    fun changeUserPassword(password: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            changePassword(ChangePasswordUseCase.ChangePassword(password, newPassword))
                .onStart {
                    _state.value = UserProfileState.Loading(R.string.changing_password)
                }
                .catch { exception ->
                    _state.value = UserProfileState.Error(exception)
                }
                .result { result ->
                    when (result.isSuccess) {
                        true -> _state.value = UserProfileState.PasswordChanged
                        false -> _state.value = UserProfileState.Error(result.exceptionOrNull()!!)
                    }
                }
        }
    }

    fun setProfileData(name: String, address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateProfile(UpdateProfileUseCase.ProfileData(name, address))
                .onStart {
                    _state.value = UserProfileState.Loading(R.string.updating_profile)
                }
                .catch { exception ->
                    _state.value = UserProfileState.Error(exception)
                }
                .result { result ->
                    when (result.isSuccess) {
                        true -> _state.value = UserProfileState.ProfileUpdated
                        false -> _state.value = UserProfileState.Error(result.exceptionOrNull()!!)
                    }
                }
        }
    }

    fun setInitState() {
        _state.value = UserProfileState.Init
    }

    sealed class UserProfileState {
        data object Init : UserProfileState()
        data class Loading(val messageId: Int? = null) : UserProfileState()
        data class Success(val profile: ProfileEntity) : UserProfileState()
        data object SessionClosed : UserProfileState()
        data object PasswordChanged : UserProfileState()
        data object ProfileUpdated : UserProfileState()
        data class Error(val exception: Throwable) : UserProfileState()
    }
}