package com.example.marketplacepuj.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplacepuj.core.components.usecase.catch
import com.example.marketplacepuj.core.components.usecase.onStart
import com.example.marketplacepuj.core.components.usecase.result
import com.example.marketplacepuj.feature.login.domain.CheckLoginUseCase
import com.example.marketplacepuj.feature.login.domain.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val checkLoginUseCase: CheckLoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Init)
    val state = _state.asStateFlow()

    private val _currentSession = MutableStateFlow<SessionState>(SessionState.Init)
    val currentSession = _currentSession.asStateFlow()

    fun login(
        username: String,
        password: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(LoginUseCase.UserData(username, password))
                .onStart {
                    _state.value = LoginState.Loading
                }.catch {
                    _state.value = LoginState.Error
                }.result { result ->
                    when (result.isSuccess) {
                        true -> _state.value = LoginState.Success
                        else -> _state.value = LoginState.Error
                    }
                }
        }
    }

    fun checkCurrentLoginSession() {
        viewModelScope.launch(Dispatchers.IO) {
            checkLoginUseCase()
                .result { result ->
                    _currentSession.value = SessionState.SessionValidation(result.getOrThrow())
                }
        }

    }

    sealed class SessionState {
        data object Init : SessionState()
        data class SessionValidation(val isValid: Boolean) : SessionState()
    }

    sealed class LoginState {
        data object Init : LoginState()
        data object Loading : LoginState()
        data object Success : LoginState()
        data object Error : LoginState()
    }
}