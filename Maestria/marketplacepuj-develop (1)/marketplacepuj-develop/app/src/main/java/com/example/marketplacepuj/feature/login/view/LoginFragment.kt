package com.example.marketplacepuj.feature.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.components.dialog.LoadingDialog
import com.example.marketplacepuj.core.components.fragment.BaseFragment
import com.example.marketplacepuj.databinding.FragmentLoginBinding
import com.example.marketplacepuj.feature.login.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by viewModel<LoginViewModel>()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val dialog: LoadingDialog by inject { parametersOf(getString(R.string.text_sign_in)) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoginState()
        viewModel.checkCurrentLoginSession()
    }

    private fun observeLoginState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                when (state) {
                    LoginViewModel.LoginState.Init -> handleInitState()
                    LoginViewModel.LoginState.Loading -> handleLoadingState()
                    LoginViewModel.LoginState.Success -> handleSuccessState()
                    LoginViewModel.LoginState.Error -> handleErrorState()
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleInitState() {
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.getString()
            val password = binding.edtPassword.getString()
            viewModel.login(username, password)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_signUpFragment2)
        }
    }

    private fun handleErrorState() {
        dismissCurrentlyShownDialog()
        showSnackbar("Invalid username or password")
    }

    private fun handleSuccessState() {
        dismissCurrentlyShownDialog()
        findNavController().navigate(R.id.action_loginFragment2_to_mainActivity)
    }

    private fun handleLoadingState() {
        showDialog(dialog, "login_dialog")
    }
}