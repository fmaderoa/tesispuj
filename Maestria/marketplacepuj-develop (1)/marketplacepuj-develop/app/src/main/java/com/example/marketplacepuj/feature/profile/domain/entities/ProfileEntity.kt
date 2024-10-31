package com.example.marketplacepuj.feature.profile.domain.entities

import com.example.marketplacepuj.feature.profile.view.adapter.viewmodels.AbstractViewModel

data class ProfileEntity(
    val name: String,
    val email: String,
    var menuOptions: List<AbstractViewModel> = emptyList(),
)
