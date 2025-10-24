package com.helper.im.data

import androidx.annotation.Keep
import com.helper.develop.util.getIntOrNull
import com.helper.develop.util.getStringOrNull
import com.helper.im.IMHelper
import com.helper.im.R
import com.helper.im.util.format
import com.helper.im.util.getString
import com.netease.nimlib.sdk.v2.conversation.model.V2NIMLocalConversation
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageCustomAttachment
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageType
import com.netease.nimlib.sdk.v2.utils.V2NIMConversationIdUtil
import org.json.JSONObject

fun V2NIMLocalConversation.transform() = IMConversation(this)
@Keep
 data class IMConversation(
    private val v2NIMLocalConversation: V2NIMLocalConversation,
    val conversationId: String = v2NIMLocalConversation.conversationId,
    val targetId: String = V2NIMConversationIdUtil.conversationTargetId(conversationId),
    val name: String? = v2NIMLocalConversation.name,
    val avatar: String? = v2NIMLocalConversation.avatar,
    val unreadCount: String = if (v2NIMLocalConversation.unreadCount > 99) "99+" else v2NIMLocalConversation.unreadCount.toString(),
    val isShowUnreadCount: Boolean = v2NIMLocalConversation.unreadCount > 0,
    val time: String = v2NIMLocalConversation.updateTime.format(),
    val text: String? = when (val type =v2NIMLocalConversation.lastMessage?.messageType) {
        null -> ""
        V2NIMMessageType.V2NIM_MESSAGE_TYPE_TEXT -> v2NIMLocalConversation.lastMessage?.text
        V2NIMMessageType.V2NIM_MESSAGE_TYPE_IMAGE -> getString(R.string.im_image_message_display)
        V2NIMMessageType.V2NIM_MESSAGE_TYPE_CUSTOM -> {
            try {
                val attachment = v2NIMLocalConversation.lastMessage.attachment as V2NIMMessageCustomAttachment
                val jsonObject = JSONObject(attachment.raw)
                val code = jsonObject.getIntOrNull("code")
                when (code) {
                    1000 -> getString(R.string.im_gift_message_display)
                    1001 -> getString(R.string.im_gift_message_invitation)
                    else ->   if(IMHelper.isDebug){
                        "不支持的自定义消息code code: ${code} data =${ jsonObject.getStringOrNull("data")} 会话id ${v2NIMLocalConversation.conversationId}"
                    }else{
                        getString(R.string.im_unknown_message_display)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if(IMHelper.isDebug){
                    "自定义消息解析失败 raw =${(v2NIMLocalConversation.lastMessage.attachment as V2NIMMessageCustomAttachment).raw} 会话id ${v2NIMLocalConversation.conversationId}"
                }else{
                    getString(R.string.im_unknown_message_display)
                }
            }
        }else->{
            if(IMHelper.isDebug){
                "不支持的消息类型: ${type} 会话id ${v2NIMLocalConversation.conversationId}"
            }else{
                getString(R.string.im_unknown_message_display)
            }
        }
    }
)