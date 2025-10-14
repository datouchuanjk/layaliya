package com.module.chat.viewmodel

import com.helper.im.IMHelper
import com.module.basic.viewmodel.BaseViewModel

internal class ConversationViewModel : BaseViewModel() {
    val pagingData = IMHelper.conversationHandler.pagingData
}