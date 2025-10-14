package com.module.charm.api.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import androidx.compose.ui.graphics.Color

@Keep
internal data class CharmLevelResponse(
    @SerializedName("reward_info")
    val rewardInfo: List<RewardInfo?>?,
    @SerializedName("user_info")
    val userInfo: UserInfo?
) {

    @Keep
    data class RewardInfo(
        val bg: String,
        val icon: String?,
        val level: String?
    ) {
        val displayLevel get() = if((level?.indexOf("~")?:-1)>=0 ) level?.split("~")?.getOrNull(1)  else   level
    }

    @Keep
    data class UserInfo(
        val avatar: String?,
        val exp: Int?,
        val bg: String?,
        val level: Int?,
        @SerializedName("next_level_exp")
        val nextLevelExp: Int?,
        val nickname: String?
    ) {

        val progress
            get() = try {
                1f * (exp ?: 0) / (nextLevelExp ?: 0)
            } catch (e: Exception) {
                0f
            }

        val textColor
            get() = when (level) {
                1 -> Color(0xff3753A7)
                2 -> Color(0xff3753A7)
                3 -> Color(0xff3753A7)
                4 -> Color(0xff3753A7)
                5 -> Color(0xff3753A7)
                6 -> Color(0xff3753A7)
                7 -> Color(0xff3753A7)
                8 -> Color(0xff3753A7)
                9 -> Color(0xff3753A7)
                10 -> Color(0xff3753A7)
                else -> Color(0xff3753A7)
            }
        val progressColors
            get() = when (level) {
                1 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                2 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                3 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                4 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                5 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                6 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                7 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                8 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                9 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                10 -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
                else -> Color(0xffe6e6e6).copy(0.6f) to Color(0xff90A8E1)
            }
    }
}