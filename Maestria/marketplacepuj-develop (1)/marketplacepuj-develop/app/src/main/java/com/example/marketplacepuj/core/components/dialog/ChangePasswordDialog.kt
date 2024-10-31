package com.example.marketplacepuj.core.components.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import com.example.marketplacepuj.databinding.ChangePasswordLayoutBinding

class ChangePasswordDialog(val listener: OnConfirmDialogListener) : BaseDialog() {

    private var _binding: ChangePasswordLayoutBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val DIALOG_ERROR_MESSAGE: String = "DIALOG_ERROR_MESSAGE"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        _binding = ChangePasswordLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        binding.btnAccept.setOnClickListener {
            binding.tvError.visibility = View.INVISIBLE
            listener.onConfirmDialog(
                binding.edtPassword.getString(),
                binding.edtConfirmPwd.getString()
            )
        }

        binding.btnCancel.setOnClickListener {
            binding.edtPassword.setText("")
            binding.edtConfirmPwd.setText("")
            dismissAllowingStateLoss()
            listener.onCancelDialog()
        }

        val message = arguments?.getString(
            DIALOG_ERROR_MESSAGE, ""
        ) ?: ""

        setErrorMessage(message)
    }

    override fun onResume() {
        dialog?.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        super.onResume()
    }

    private fun setErrorMessage(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }

    interface OnConfirmDialogListener {
        fun onConfirmDialog(password: String, newPassword: String)
        fun onCancelDialog() {}
    }
}