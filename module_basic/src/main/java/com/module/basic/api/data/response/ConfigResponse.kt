package com.module.basic.api.data.response


import android.util.Log
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * 配置信息
 */
@Keep
data class ConfigResponse(
    val language: List<Language>,
    @SerializedName("country_code")
    val country: List<Country>,
    @SerializedName("report_type")
    val reportType: ReportTypes?,
    @SerializedName("room_type")
    val roomType: List<RoomType>?,
    @SerializedName("mute_type")
    val muteType: List<MuteType>?,
    @SerializedName("gift_config")
    val giftConfig: List<GiftConfig>,
    val meme: String?,
) {

    data class GiftConfig(
        val id: String?,
        val pic: String?,
        val svg: String?,
    )

    @Keep
    data class ReportTypes(
        @SerializedName("room_report_types")
        val roomReportTypes: List<ReportType?>?,
        @SerializedName("user_report_types")
        val userReportTypes: List<ReportType?>?
    )


    @Keep
    data class ReportType(
        val content: String?,
        val id: Int?
    )


    @Keep
    data class RoomType(
        val name: String,
        val id: Int
    )

    data class Language(
        val code: String,
        val name: String
    )

    data class Country(
        val code: String,
        val name: String
    )

    data class MuteType(
        val type: Int,
        val content: String
    )
}