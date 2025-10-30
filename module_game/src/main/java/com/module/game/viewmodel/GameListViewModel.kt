package com.module.game.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.module.basic.util.buildOffsetPaging
import com.module.basic.viewmodel.BaseViewModel
import com.module.game.api.data.request.GameListRequest
import com.module.game.api.service.GameApiService


internal class GameListViewModel(
    private val api: GameApiService,
    saveHandler: SavedStateHandle,
) : BaseViewModel() {

    val roomId = saveHandler.get<String?>("roomId")


    val pagingData = buildOffsetPaging(viewModelScope) {
      if(roomId!=null){
          api.getRoomGameList().checkAndGet()?.list
      }else{
          api.getGameList().checkAndGet()?.list
      }
    }.pagingData

}