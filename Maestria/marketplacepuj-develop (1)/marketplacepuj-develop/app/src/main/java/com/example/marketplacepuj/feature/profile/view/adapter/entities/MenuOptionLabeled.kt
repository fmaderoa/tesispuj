package com.example.marketplacepuj.feature.profile.view.adapter.entities

import androidx.annotation.DrawableRes
import com.example.marketplacepuj.R

sealed class MenuOptionLabeled(val id: String, val label: Int, @DrawableRes val icon: Int) {
    data object CloseSession :
        MenuOptionLabeled("CloseSession", R.string.text_label_close_session, R.drawable.ic_exit)

    data object ChangePassword :
        MenuOptionLabeled(
            "ChangePassword",
            R.string.text_label_change_password,
            R.drawable.ic_password
        )

    data object EditProfile : MenuOptionLabeled(
        "EditProfile",
        R.string.text_label_edit_profile,
        R.drawable.outline_account_circle_24
    )

    data object OrderHistory :
        MenuOptionLabeled("OrderHistory", R.string.text_label_order_history, R.drawable.ic_history)
}