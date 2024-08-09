package com.devx.tictacpro.domain.mapper

import androidx.annotation.DrawableRes
import com.devx.tictacpro.R

class DrawableResourceMapper {
    private val resourceToStringMap = mutableMapOf<Int, String>()
    private val stringToResourceMap = mutableMapOf<String, Int>()

    init {
        resourceToStringMap[R.drawable.boy_avatar_1] = "boy_avatar_1"
        resourceToStringMap[R.drawable.boy_avatar_2] = "boy_avatar_2"
        resourceToStringMap[R.drawable.boy_avatar_3] = "boy_avatar_3"
        resourceToStringMap[R.drawable.boy_avatar_4] = "boy_avatar_4"
        resourceToStringMap[R.drawable.girl_avatar_1] = "girl_avatar_1"
        resourceToStringMap[R.drawable.girl_avatar_2] = "girl_avatar_2"
        resourceToStringMap[R.drawable.girl_avatar_3] = "girl_avatar_3"
        resourceToStringMap[R.drawable.girl_avatar_4] = "girl_avatar_4"

        stringToResourceMap["boy_avatar_1"] = R.drawable.boy_avatar_1
        stringToResourceMap["boy_avatar_2"] = R.drawable.boy_avatar_2
        stringToResourceMap["boy_avatar_3"] = R.drawable.boy_avatar_3
        stringToResourceMap["boy_avatar_4"] = R.drawable.boy_avatar_4
        stringToResourceMap["girl_avatar_1"] = R.drawable.girl_avatar_1
        stringToResourceMap["girl_avatar_2"] = R.drawable.girl_avatar_2
        stringToResourceMap["girl_avatar_3"] = R.drawable.girl_avatar_3
        stringToResourceMap["girl_avatar_4"] = R.drawable.girl_avatar_4
    }

    fun getResourceString(@DrawableRes resId: Int): String? {
        return resourceToStringMap[resId]
    }

    fun getResourceId(resString: String): Int? {
        return stringToResourceMap[resString]
    }
}