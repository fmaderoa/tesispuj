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
import com.example.marketplacepuj.R
import com.example.marketplacepuj.databinding.ConfirmationDialogBinding

class ConfirmationDialog(val listener: OnConfirmDialogListener) : BaseDialog() {

    companion object {
        const val DIALOG_TITLE: String = "DIALOG_TITLE"
        const val DIALOG_MESSAGE: String = "DIALOG_MESSAGE"
        const val HIDE_CANCEL_BTN: String = "HIDE_CANCEL_BTN"
    }

    private var _binding: ConfirmationDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        _binding = ConfirmationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        val title = arguments?.getString(
            DIALOG_TITLE,
            context?.getString(R.string.confirmation_dialog_default_title)
        )
        val message = arguments?.getString(
            DIALOG_MESSAGE, ""
        )

        binding.tvTitle.text = title
        binding.tvMessage.text = message

        binding.btnAccept.setOnClickListener {
            listener.onConfirmDialog()
        }

        binding.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
            listener.onCancelDialog()
        }

        val hideCancelBtn = arguments?.getBoolean(
            HIDE_CANCEL_BTN, false
        ) ?: false

        when (hideCancelBtn) {
            true -> binding.btnCancel.visibility = View.GONE
            false -> binding.btnCancel.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        dialog?.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        super.onResume()
    }

    interface OnConfirmDialogListener {
        fun onConfirmDialog()
        fun onCancelDialog() {}
    }
}