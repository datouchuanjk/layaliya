package com.module.game.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.helper.develop.paging.LoadResult
import com.helper.develop.paging.buildPaging
import com.module.basic.sp.getToken
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.game.api.service.GameApiService
import kotlinx.coroutines.launch


internal class GameListViewModel(
    private val api: GameApiService,
    saveHandler: SavedStateHandle,
) : BaseViewModel() {

    val roomId = saveHandler.get<String?>("roomId")

    //是否作为子界面嵌套 如果是 那么不能退出界面
    val withChildScreen = saveHandler.get<Boolean>("withChildScreen") ?: false

    val pagingData = buildOffsetPaging(viewModelScope) {
        api.getGameList().checkAndGet()?.list
    }.pagingData

}