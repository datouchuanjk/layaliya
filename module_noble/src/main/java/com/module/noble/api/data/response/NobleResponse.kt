package com.module.noble.api.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.helper.develop.util.YMD
import com.module.basic.sp.AppGlobal
import java.util.Calendar

@Keep
data class NobleResponse(
    val level: Int?,
    val name: String?,
    val bg: String?,
    val icon: String?,
    @SerializedName("icon_bg")
    val iconBg: String?,
    @SerializedName("power_bg")
    val powerBg: String?,
    val price: Int?,
    val description: String?,
    val avatar: String?,
    @SerializedName("avatar_svg")
    val avatarSvg: String?,
    val medal: String?,
    val car: String?,
    @SerializedName("car_svg")
    val carSvg: String?,
    @SerializedName("text_box")
    val textBox: String?,
    @SerializedName("power_data")
    val powerData: List<PowerData?>?,

    ) {


    val textColor
        get() = when (level) {
            1 -> Brush.verticalGradient(colors = listOf(Color(0xffFFAC55), Color(0xff92350E)))
            2 -> Brush.verticalGradient(colors = listOf(Color(0xff32B2F8), Color(0xff3768CA)))
            3 -> Brush.verticalGradient(colors = listOf(Color(0xff32B2F8), Color(0xff3768CA)))
            4 -> Brush.verticalGradient(colors = listOf(Color(0xffFF79E7), Color(0xff771AA8)))
            5 -> Brush.verticalGradient(colors = listOf(Color(0xffFF8789), Color(0xffE0029E)))
            6 -> Brush.verticalGradient(colors = listOf(Color(0xffFF0D0D), Color(0xffA52E49)))
            else -> Brush.verticalGradient(colors = listOf(Color(0xffFFAC55), Color(0xff92350E)))
        }
    val secondTextColor
        get() = when (level) {
            1 -> Color(0xffB55B25)
            2 -> Color(0xff3464C6)
            3 -> Color(0xff3464C6)
            4 -> Color(0xff7B1DAE)
            5 -> Color(0xffE0029E)
            6 -> Color(0xffA52E49)
            else -> Color(0xffB55B25)
        }

    @Keep
    data class PowerData(
        val icon: String?,
        val name: String?
    )
}