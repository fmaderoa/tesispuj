package com.example.marketplacepuj.core.components.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.marketplacepuj.core.components.dialog.DialogsManager
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    private lateinit var dialogsManager: DialogsManager

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogsManager = DialogsManager(parentFragmentManager)
    }

    fun showDialog(dialog: DialogFragment, id: String) {
        dialogsManager.showDialogWithId(dialog, id)
    }

    fun dismissCurrentlyShownDialog() {
        dialogsManager.dismissCurrentlyShownDialog()
    }

    fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}