package com.example.marketplacepuj.core.components.dialog

import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.UiThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

@UiThread
class DialogsManager(private val fragmentManager: FragmentManager) {

    companion object {
        const val ARGUMENT_DIALOG_ID = "ARGUMENT_DIALOG_ID"
        private const val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG"
    }

    var mCurrentlyShownDialog: DialogFragment? = null

    init {
        val fragmentWithDialogTag: Fragment? =
            fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG)

        if (fragmentWithDialogTag != null &&
            DialogFragment::class.java.isAssignableFrom(fragmentWithDialogTag.javaClass)
        ) {
            mCurrentlyShownDialog = fragmentWithDialogTag as DialogFragment
        }
    }

    fun getCurrentlyShownDialog(): DialogFragment? {
        return mCurrentlyShownDialog
    }

    fun getCurrentlyShownDialogId(): String? {
        return if (mCurrentlyShownDialog == null || mCurrentlyShownDialog!!.arguments == null ||
            !mCurrentlyShownDialog!!.requireArguments().containsKey(ARGUMENT_DIALOG_ID)
        ) {
            ""
        } else {
            mCurrentlyShownDialog!!.requireArguments().getString(ARGUMENT_DIALOG_ID)
        }
    }

    fun isDialogCurrentlyShown(id: String): Boolean {
        val shownDialogId = getCurrentlyShownDialogId()
        return !TextUtils.isEmpty(shownDialogId) && shownDialogId == id
    }

    fun dismissCurrentlyShownDialog() {
        mCurrentlyShownDialog?.dismissAllowingStateLoss()
        mCurrentlyShownDialog = null
    }

    /**
     * Show dialog and assign it a given "id". Replaces any other currently shown dialog.
     * @param dialog dialog to show
     * @param id string that uniquely identifies the dialog; can be null
     */
    fun showDialogWithId(dialog: DialogFragment, id: String?) {
        dismissCurrentlyShownDialog()
        setId(dialog, id)
        showDialog(dialog)
    }

    private fun setId(dialog: DialogFragment, id: String?) {
        val args = if (dialog.arguments != null) dialog.arguments else Bundle(1)
        args!!.putString(ARGUMENT_DIALOG_ID, id)
        dialog.arguments = args
    }

    private fun showDialog(dialog: DialogFragment) {
        fragmentManager.beginTransaction()
            .add(dialog, DIALOG_FRAGMENT_TAG)
            .commitAllowingStateLoss()
        mCurrentlyShownDialog = dialog
    }
}