package com.devx.tictacpro.presentation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Parcelize
@Serializable
data class Player(
    val id: String,
    val name: String,
    @DrawableRes val avatar: Int,
    val turn: String,
    var score: Int = 0
): Parcelable

val PlayerNavType = object: NavType<Player?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): Player? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Player::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Player {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Player?) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: Player?): String {
        return Json.encodeToString(value)
    }
}
