package com.example.marketplacepuj.feature.register.view

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
import com.example.marketplacepuj.core.exception.BadEmailFieldException
import com.example.marketplacepuj.core.exception.BadPasswordFieldException
import com.example.marketplacepuj.core.exception.BadPasswordLengthException
import com.example.marketplacepuj.core.exception.EmptyFieldException
import com.example.marketplacepuj.databinding.FragmentSignUpBinding
import com.example.marketplacepuj.feature.register.viewmodel.SignUpViewModel
import com.example.marketplacepuj.feature.register.viewmodel.SignUpViewModel.SignUpState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SignUpFragment : BaseFragment() {

    private val viewModel: SignUpViewModel by viewModel<SignUpViewModel>()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val dialog: LoadingDialog by inject { parametersOf(getString(R.string.text_sign_up_2)) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSignUpState()
        binding.materialToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeSignUpState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                when (state) {
                    SignUpState.Init -> handleInitState()
                    SignUpState.Loading -> handleLoadingState()
                    SignUpState.Success -> handleSuccessState()
                    is SignUpState.Error -> handleErrorState(state.exception)
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleErrorState(exception: Throwable) {
        dismissCurrentlyShownDialog()
        when (exception) {
            is BadEmailFieldException -> {
                binding.edtUsername.error = getString(R.string.error_invalid_email)
                showSnackbar(getString(R.string.error_invalid_email))
            }

            is BadPasswordFieldException -> {
                binding.edtPassword.error = getString(R.string.error_invalid_password)
                showSnackbar(getString(R.string.error_invalid_password))
            }

            is EmptyFieldException -> {
                showSnackbar(getString(R.string.error_empty_fields))
            }

            is BadPasswordLengthException -> {
                binding.edtPassword.error = getString(R.string.error_password_too_short)
                showSnackbar(getString(R.string.error_password_too_short))
            }
        }
    }

    private fun handleSuccessState() {
        dismissCurrentlyShownDialog()
        findNavController().navigate(R.id.action_signUpFragment2_to_mainActivity)
    }

    private fun handleLoadingState() {
        showDialog(dialog, "sign_up_dialog")
    }

    private fun handleInitState() {
        binding.btnSignUp.setOnClickListener {
            val username = binding.edtUsername.getString()
            val password = binding.edtPassword.getString()
            val password2 = binding.edtPassword2.getString()
            viewModel.signUp(username, password, password2)
        }
    }

}