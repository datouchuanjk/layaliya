package com.module.agent.api.data.request

import kotlinx.serialization.SerialName

data class WeekRangeRequest(
    val page: Int,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String
)
