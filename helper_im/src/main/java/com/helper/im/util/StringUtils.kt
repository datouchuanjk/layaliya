package com.helper.im.util

import androidx.annotation.StringRes
import com.helper.im.IMHelper
import com.netease.nimlib.sdk.v2.conversation.enums.V2NIMConversationType
import com.netease.nimlib.sdk.v2.utils.V2NIMConversationIdUtil

internal fun getString(@StringRes id: Int) = IMHelper.context.getString(id)

fun String.toTargetId(): String = V2NIMConversationIdUtil.conversationTargetId(this)

fun String.toConversationId(): String = V2NIMConversationIdUtil.conversationId(this, V2NIMConversationType.V2NIM_CONVERSATION_TYPE_P2P)