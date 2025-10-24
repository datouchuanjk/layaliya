package com.module.agent.viewmodel

import androidx.lifecycle.viewModelScope
import com.helper.develop.paging.PagingStart
import com.module.agent.api.data.request.WeekRangeRequest
import com.module.agent.api.service.AgentApiService
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel

internal class AdminViewModel(
    private val api: AgentApiService
) : BaseViewModel() {

    private var startTime = ""
    private var endTime = ""
    fun refreshByTime(
        startTime: String,
        endTime: String
    ) {
        this.startTime = startTime
        this.endTime = endTime
        pagingData.refresh()
    }

    val pagingData = buildOffsetPaging(viewModelScope, pagingStart = PagingStart.LAZY) {
        api.adminIncomingList(
            WeekRangeRequest(
                startDate = startTime,
                endDate = endTime,
                page = it.key!!,
            )
        ).checkAndGet()?.list
    }.pagingData
}