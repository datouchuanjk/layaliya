package com.module.chatroom.ui.popup

import androidx.compose.ui.graphics.painter.Painter

data class PopupAction(
    val image: Painter,
    val text: String,
    val enable: Boolean = true,
    val onClick: () -> Unit
){
}


