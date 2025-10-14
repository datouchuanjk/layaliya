package com.module.agent.api.data.request

import kotlinx.serialization.SerialName

data class WeekRangeRequest(
    val page: Int,
    @SerialName("start_date")
    val startDate: String = System.currentTimeMillis().toString(),
    @SerialName("end_date")
    val endDate: String =System.currentTimeMillis().toString(),
)
