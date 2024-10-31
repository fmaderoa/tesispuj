package com.example.marketplacepuj.feature.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.marketplacepuj.MainActivity
import com.example.marketplacepuj.R
import com.example.marketplacepuj.core.components.dialog.ChangePasswordDialog
import com.example.marketplacepuj.core.components.dialog.ConfirmationDialog
import com.example.marketplacepuj.core.components.dialog.EditProfileDialog
import com.example.marketplacepuj.core.components.dialog.LoadingDialog
import com.example.marketplacepuj.core.components.fragment.BaseFragment
import com.example.marketplacepuj.core.exception.BadEmailFieldException
import com.example.marketplacepuj.core.exception.BadPasswordFieldException
import com.example.marketplacepuj.core.exception.BadPasswordLengthException
import com.example.marketplacepuj.core.exception.ChangePasswordException
import com.example.marketplacepuj.core.exception.CloseSessionException
import com.example.marketplacepuj.core.exception.EmptyFieldException
import com.example.marketplacepuj.core.exception.NoUserFoundException
import com.example.marketplacepuj.core.exception.ProfileErrorException
import com.example.marketplacepuj.databinding.FragmentUserProfileBinding
import com.example.marketplacepuj.feature.profile.domain.entities.ProfileEntity
import com.example.marketplacepuj.feature.profile.view.adapter.ProfileOptionsAdapter
import com.example.marketplacepuj.feature.profile.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserProfileFragment : BaseFragment(), ProfileOptionsAdapter.ManageTerminalAdapterListener {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserProfileViewModel by viewModel<UserProfileViewModel>()
    private val loadingDialog: LoadingDialog by inject { parametersOf(getString(R.string.text_loading_profile)) }
    private val changePasswordDialog: ChangePasswordDialog by inject {
        parametersOf(object : ChangePasswordDialog.OnConfirmDialogListener {
            override fun onConfirmDialog(password: String, newPassword: String) {
                viewModel.changeUserPassword(password, newPassword)
            }
        })
    }

    private val editProfileDialog: EditProfileDialog by inject {
        parametersOf(object : EditProfileDialog.OnConfirmDialogListener {
            override fun onConfirmDialog(name: String, address: String) {
                viewModel.setProfileData(name, address)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setToolbarColor(0xffffffff.toInt(), 0xff4D4D4D.toInt())
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { result ->
                when (result) {
                    UserProfileViewModel.UserProfileState.Init -> handleInitState()
                    is UserProfileViewModel.UserProfileState.Loading -> handleLoadingState(result.messageId)
                    is UserProfileViewModel.UserProfileState.Success -> handleSuccessState(result.profile)
                    is UserProfileViewModel.UserProfileState.Error -> handleErrorState(result.exception)
                    UserProfileViewModel.UserProfileState.SessionClosed -> handleSessionClosedState()
                    UserProfileViewModel.UserProfileState.PasswordChanged -> handlePasswordChangedState()
                    UserProfileViewModel.UserProfileState.ProfileUpdated -> handleProfileUpdatedState()
                }
            }.launchIn(lifecycleScope)
    }

    private fun handleProfileUpdatedState() {
        dismissCurrentlyShownDialog()
        showToast(getString(R.string.profile_updated))
        viewModel.setInitState()
    }

    private fun handlePasswordChangedState() {
        dismissCurrentlyShownDialog()
        showToast(getString(R.string.password_changed))
        (activity as MainActivity).goToLogin()
    }

    private fun handleSessionClosedState() {
        showToast(getString(R.string.session_closed))
        (activity as MainActivity).goToLogin()
    }

    private fun handleErrorState(exception: Throwable) {
        dismissCurrentlyShownDialog()
        when (exception) {
            is NoUserFoundException -> goToLogin(getString(R.string.error_no_user_found))
            is ProfileErrorException -> showSnackbar(getString(R.string.error_getting_user_profile))
            is CloseSessionException -> showSnackbar(getString(R.string.error_closing_session))
            is BadEmailFieldException -> setChangePasswordError(getString(R.string.error_invalid_email))
            is BadPasswordFieldException -> setChangePasswordError(getString(R.string.error_invalid_password))
            is EmptyFieldException -> setChangePasswordError(getString(R.string.error_empty_fields))
            is BadPasswordLengthException -> setChangePasswordError(getString(R.string.error_password_too_short))
            is ChangePasswordException -> setChangePasswordError(getString(R.string.error_passwords_not_equal))
            is FirebaseAuthRecentLoginRequiredException -> goToLogin(getString(R.string.error_recent_login_required))
        }
    }

    private fun goToLogin(message: String) {
        dismissCurrentlyShownDialog()
        showToast(message)
        (activity as MainActivity).goToLogin()
    }

    private fun setChangePasswordError(message: String) {
        changePasswordDialog.arguments = Bundle().apply {
            putString(ChangePasswordDialog.DIALOG_ERROR_MESSAGE, message)
        }
        showDialog(changePasswordDialog, "change_password_dialog")
    }

    private fun handleSuccessState(profile: ProfileEntity) {
        dismissCurrentlyShownDialog()
        binding.tvEmail.text = profile.email
        binding.tvName.text = profile.name

        binding.rvOptions.adapter = ProfileOptionsAdapter().apply {
            items = profile.menuOptions
            setListener(this@UserProfileFragment)
        }
    }

    private fun handleLoadingState(messageId: Int?) {
        messageId?.let {
            val message = getString(it)
            loadingDialog.message = message
        }
        showDialog(loadingDialog, "profile_dialog")
    }

    private fun handleInitState() {
        viewModel.getUserProfile()
    }

    override fun onClickChangePassword() {
        showDialog(changePasswordDialog, "change_password_dialog")
    }

    override fun onClickEditProfile() {
        showDialog(editProfileDialog, "edit_profile_dialog")
    }

    override fun onClickOrderHistory() {
        findNavController().navigate(R.id.action_userProfileFragment_to_ordersFragment)
    }

    override fun onClickCloseSession() {
        val confirmationDialog =
            ConfirmationDialog(object : ConfirmationDialog.OnConfirmDialogListener {
                override fun onConfirmDialog() {
                    viewModel.closeUserSession()
                }
            })
        val arg = Bundle()
        arg.putString(
            ConfirmationDialog.DIALOG_TITLE,
            getString(R.string.text_label_close_session)
        )
        arg.putString(
            ConfirmationDialog.DIALOG_MESSAGE,
            getString(R.string.text_close_session_message)
        )
        confirmationDialog.arguments = arg
        showDialog(confirmationDialog, "confirmation_dialog")
    }
}