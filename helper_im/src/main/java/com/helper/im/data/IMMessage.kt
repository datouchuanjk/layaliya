package com.helper.im.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.helper.develop.util.*
import com.helper.im.IMHelper
import com.helper.im.R
import com.helper.im.util.format
import com.helper.im.util.getString
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.v2.message.V2NIMMessage
import com.netease.nimlib.sdk.v2.message.V2NIMMessageService
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageCustomAttachment
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageImageAttachment
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import org.json.JSONObject

fun V2NIMMessage.transform() = IMMessage(this)

@Keep
data class IMMessage(
    val v2NIMMessage: V2NIMMessage,
    private val userInfo: IMUser? = IMHelper.userHandler.getLocalUserInfo(v2NIMMessage.senderId),
    private val receiverInfo: IMUser? = IMHelper.userHandler.getLocalUserInfo(v2NIMMessage.receiverId),
    val senderAvatar: String? = userInfo?.avatar,
    val senderName: String? = userInfo?.name,
    val receiverAvatar: String? = receiverInfo?.avatar,
    val receiverName: String? = receiverInfo?.name,
    val time: String = v2NIMMessage.createTime.format(),
    val text: String? = when (v2NIMMessage.messageType) {
        V2NIMMessageType.V2NIM_MESSAGE_TYPE_TEXT -> v2NIMMessage.text
        V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE -> getString(R.string.im_image_message_display)
        V2NIMMessageType.V2NIM_MESSAGE_TYPE_CUSTOM -> {
            try {
                val attachment = v2NIMMessage.attachment as V2NIMMessageCustomAttachment
                val jsonObject = JSONObject(attachment.raw)
                val code = jsonObject.getInt("code")
                when (code) {
                    1000 -> getString(R.string.im_gift_message_display)
                    1001 -> getString(R.string.im_gift_message_invitation)
                    else -> getString(R.string.im_unknown_message_display)
                }
            } catch (e: Exception) {
                getString(R.string.im_unknown_message_display)
            }
        }

        else -> getString(R.string.im_unknown_message_display)
    },
    val senderId: String = v2NIMMessage.senderId,
    val receiverId: String = v2NIMMessage.receiverId,
    val isSelf: Boolean = v2NIMMessage.isSelf,
    val conversationId: String = v2NIMMessage.conversationId,
    val messageId: Long = v2NIMMessage.messageId,
    val sendingState: Int = v2NIMMessage.sendingState.value,
    val isRead: Boolean = NIMClient.getService(V2NIMMessageService::class.java)
        .isPeerRead(v2NIMMessage),
    val createTime: Long = v2NIMMessage.createTime,
    val body: Any = when (v2NIMMessage.messageType) {
        V2NIMMessageType.V2NIM_MESSAGE_TYPE_TEXT -> {
            IMTextBody(v2NIMMessage.text)
        }

        V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE -> {
            val attachment = v2NIMMessage.attachment as V2NIMMessageImageAttachment
            IMImageBody(
                path = attachment.path,
                url = attachment.url,
                width = attachment.width,
                height = attachment.height
            )
        }

        V2NIMMessageType.V2NIM_MESSAGE_TYPE_CUSTOM -> {
            try {
                val attachment = v2NIMMessage.attachment as V2NIMMessageCustomAttachment
                val jsonObject = JSONObject(attachment.raw)
                val code = jsonObject.getInt("code")
                val data = jsonObject.getString("data")
                when (code) {
                    1000 -> {
                        data.fromJson<IMGiftBody>() ?: IMUnknownBody
                    }

                    1001 -> {
                        data.fromJson<IMInvitationBody>() ?: IMUnknownBody
                    }

                    else -> IMUnknownBody
                }
            } catch (e: Exception) {
                getString(R.string.im_unknown_message_display)
            }

        }

        else -> IMUnknownBody
    },
)

data class IMTextBody(val text: String?)
data class IMImageBody(
    val path: String?,
    val url: String?,
    val width: Int,
    val height: Int,
)

data class IMGiftBody(
    val string: String,
)

data class IMInvitationBody(
    @SerializedName("record_id")
    val recordId: String,
    val uid: String,
    val nickname: String,
    val content: String,
    val isSubmit: Boolean = false
) {
    fun copyToSubmit() = JSONObject().apply {
        put("code", 1001)
        put("data", copy(isSubmit = true).toJson())
    }.toString()
}

object IMUnknownBody