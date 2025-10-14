package com.module.chatroom.api.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.module.basic.api.data.request.BaseRequest

@Keep
internal data class ChatroomReportRequest(
    @SerializedName("report_type_id")
    val reportTypeId: String,
    val content: String,
    @SerializedName("report_obj_id")
    val reportObjId: String,
): BaseRequest()
