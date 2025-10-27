package com.module.agent.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.filterDigit
import com.helper.develop.util.toast
import com.module.agent.R
import com.module.agent.api.data.request.AgentInviteRequest
import com.module.agent.api.data.response.AgentInfoResponse
import com.module.agent.api.service.AgentApiService
import com.module.basic.api.data.request.PagingRequest
import com.module.basic.api.data.request.UidRequest
import com.module.basic.api.data.response.SearchUserResponse
import com.module.basic.api.data.response.UserResponse
import com.module.basic.api.service.BasicApiService
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class BasicInformationViewModel(
    private val api: AgentApiService,
    private val basicApi: BasicApiService,
    private val application: Application,
) : BaseViewModel() {


    /**
     * 获取代理信息
     */
    private var _agentInfo by mutableStateOf<AgentInfoResponse?>(null)
    val agentInfo get() = _agentInfo
    fun getAgentInfo() {
        viewModelScope.launch {
            apiRequest {
                api.getAgentInfo().checkAndGet()
            }.apiResponse {
                _agentInfo = it
            }
        }
    }

    init {
        getAgentInfo()
    }

    /**
     * 邀请人的uid
     */
    private var _inviteUid by mutableStateOf("")
    val inviteUid get() = _inviteUid
    fun inviteUid(value: String) {
        _inviteUid = value.filterDigit()
    }

    private var _inviteUserResponse by mutableStateOf<SearchUserResponse?>(null)
    val inviteUserResponse get() = _inviteUserResponse
    fun clearInviteUserResponse() {
        _inviteUserResponse = null
    }

    fun findUserByUid() {
        if (inviteUid.isEmpty()) {
            return
        }
        viewModelScope.launch {
            apiRequest {
                basicApi.searchUser(UidRequest(inviteUid)).checkAndGet()
            }.apiResponse {
                if (it == null||it.id==null) {
                    application.toast(R.string.agent_not_find_user)
                    return@apiResponse
                }
                _inviteUserResponse = it
            }
        }
    }

    /**
     * 代理邀请
     */

    fun agentInvite() {
        val id = _inviteUserResponse?.id.toString()
        if (id.isEmpty()) return
        viewModelScope.launch {
            apiRequest {
                api.agentInvite(AgentInviteRequest(id)).checkAndGet()
            }.apiResponse {
                //邀请成功的话 邀请列表需要刷新一下
                pagingData.refresh()
                clearInviteUserResponse()
            }
        }
    }

    val pagingData = buildOffsetPaging(viewModelScope) {
        api.agentInviteRecord(PagingRequest(page = it.key!!)).checkAndGet()?.list
    }.pagingData
}