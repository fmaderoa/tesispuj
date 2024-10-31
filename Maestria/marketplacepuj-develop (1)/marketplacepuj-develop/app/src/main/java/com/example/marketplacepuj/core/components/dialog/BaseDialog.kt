package com.example.marketplacepuj.core.components.dialog

import android.view.WindowManager
import androidx.fragment.app.DialogFragment

open class BaseDialog : DialogFragment() {

    override fun onResume() {
        val params = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params
        super.onResume()
    }
}