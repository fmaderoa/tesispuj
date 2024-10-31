package com.example.marketplacepuj.core.components.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import com.example.marketplacepuj.databinding.LoadingDialogBinding

class LoadingDialog(var message: String) : BaseDialog() {

    private var _binding: LoadingDialogBinding? = null
    private val binding get() = _binding!!

    val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        var count = 0
        override fun run() {
            count++
            when (count) {
                1 -> binding.tvSync.text = String.format(message, ".")
                2 -> binding.tvSync.text = String.format(message, "..")
                3 -> {
                    binding.tvSync.text = String.format(message, "...")
                    count = 0
                }
            }

            handler.postDelayed(this, (2 * 500L))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        _binding = LoadingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSync.text = String.format(message, "")
        handler.postDelayed(runnable, (1 * 500L))
    }

    override fun onResume() {
        dialog?.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        super.onResume()
    }
}