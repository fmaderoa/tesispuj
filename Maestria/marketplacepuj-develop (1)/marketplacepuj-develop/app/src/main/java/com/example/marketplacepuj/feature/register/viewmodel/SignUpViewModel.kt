package com.example.marketplacepuj.feature.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplacepuj.core.components.usecase.catch
import com.example.marketplacepuj.core.components.usecase.onStart
import com.example.marketplacepuj.core.components.usecase.result
import com.example.marketplacepuj.feature.register.domain.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(private val signUpUseCase: SignUpUseCase) : ViewModel() {

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Init)
    val state = _state.asStateFlow()

    fun signUp(username: String, password: String, password2: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signUpUseCase(SignUpUseCase.UserData(username, password, password2))
                .onStart {
                    _state.value = SignUpState.Loading
                }
                .catch { exception ->
                    _state.value = SignUpState.Error(exception)
                }
                .result { result ->
                    when (result.isSuccess) {
                        true -> _state.value = SignUpState.Success
                        false -> _state.value = SignUpState.Error(result.exceptionOrNull()!!)
                    }
                }
        }
    }

    sealed class SignUpState {
        data object Init : SignUpState()
        data object Loading : SignUpState()
        data object Success : SignUpState()
        data class Error(val exception: Throwable) : SignUpState()
    }
}