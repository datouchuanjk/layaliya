package com.module.emoji.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.*
import com.helper.develop.nav.*
import com.module.basic.sp.AppGlobal
import com.module.basic.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

class EmojiViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val list = AppGlobal.emojiIds
    private var _index by mutableIntStateOf(0)
    val index get() = _index
    fun index(index: Int) {
        _index = index
    }

    val x = savedStateHandle.get<Float>("x").apply {
        Log.e("1234", "x=$this")
    } ?: 0f
    val y = savedStateHandle.get<Float>("y").apply {
        Log.e("1234", "y=$this")
    } ?: 0f
    val w = savedStateHandle.get<Float>("w").apply {
        Log.e("1234", "w=$this")
    } ?: 0f
    val h = savedStateHandle.get<Float>("h").apply {
        Log.e("1234", "h=$this")
    } ?: 0f

    val animationRectList = mutableStateListOf<Pair<String, Rect>>()
    fun addAnimationRectList(id: String, rect: Rect) {
        animationRectList.add(id to rect)
    }

    /**
     * 这个时候需要发射 通知了哦
     */
    fun removeAnimationRectList(item: Pair<String, Rect>, localNav: NavHostController) {
        viewModelScope.launch {
            if (animationRectList.contains(item)) {
                val jsonObject = JSONObject()
                jsonObject.put("emojiId", item.first)
                jsonObject.put("uid", AppGlobal.userResponse?.id.toString())
                localNav.emitResult("send_emoji_result", jsonObject.toString())
                delay(500)
                animationRectList.remove(item)
            }
        }
    }
}