package com.module.agent.viewmodel

import androidx.lifecycle.viewModelScope
import com.module.agent.api.data.request.WeekRangeRequest
import com.module.agent.api.service.AgentApiService
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel

internal class AdminViewModel(
    private val api: AgentApiService
) : BaseViewModel() {

    val pagingData = buildOffsetPaging(viewModelScope) {
        api.adminIncomingList(
            WeekRangeRequest(
                page = it.key!!,
            )
        ).checkAndGet()?.list
    }.pagingData
}