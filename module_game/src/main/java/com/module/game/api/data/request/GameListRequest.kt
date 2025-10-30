package com.module.game.api.data.request

import com.google.gson.annotations.SerializedName

data class GameListRequest(
    @SerializedName("'type")
    val  type: Int //1 all  2 竖屏
)
