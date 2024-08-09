package com.devx.tictacpro.presentation.home.state

import androidx.annotation.DrawableRes
import com.devx.tictacpro.R

data class ProfileState(
    @DrawableRes val avatar: Int,
    val name: String,
    val email: String? = null,
    val availableAvatars: List<Int> = avatars
) {
    companion object {
        private val avatars = listOf(
            R.drawable.boy_avatar_1,
            R.drawable.boy_avatar_2,
            R.drawable.boy_avatar_3,
            R.drawable.boy_avatar_4,
            R.drawable.girl_avatar_1,
            R.drawable.girl_avatar_2,
            R.drawable.girl_avatar_3,
            R.drawable.girl_avatar_4
        )
    }
}