package com.module.game.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import com.module.basic.sp.getToken
import com.module.basic.viewmodel.BaseViewModel


internal class GameViewModel(
    private val saveHandler: SavedStateHandle,
    private val share: SharedPreferences,
) : BaseViewModel() {

    val roomId = saveHandler.get<String?>("roomId")

    //是否作为子界面嵌套 如果是 那么不能退出界面
    val withChildScreen = saveHandler.get<Boolean>("withChildScreen")
    val token = share.getToken()
}