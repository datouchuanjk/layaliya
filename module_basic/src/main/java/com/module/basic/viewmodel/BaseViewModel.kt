package com.module.basic.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helper.im.IMHelper
import com.module.basic.api.data.request.TokenRequest
import com.module.basic.api.service.BasicApiService
import com.module.basic.sp.AppGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _errorFlow = MutableSharedFlow<Throwable>()
    internal val errorFlow = _errorFlow.asSharedFlow()

    private var _loading by mutableStateOf(false)
    internal val loading get() = _loading

    protected fun handleLoading(isLoading: Boolean) {
        _loading = isLoading
    }

    fun <T> apiRequest(
        request: suspend () -> T,
    ) = flow {
        emit(request())
    }

    suspend fun <T> Flow<T>.apiResponse(
        loading: (suspend FlowCollector<T>.(Boolean, suspend () -> Unit) -> Unit)? = { _, block ->
            block()
        },
        catch: (suspend FlowCollector<T>.(Throwable, suspend () -> Unit) -> Unit)? = { _, block ->
            block()
        },
        collector: suspend (T) -> Unit = {},
    ) = flowOn(Dispatchers.IO)
        .onStart {
            loading?.invoke(this, true) {
                _loading = true
            }
        }.onCompletion {
            loading?.invoke(this, false) {
                _loading = false
            }
        }.catch {
            catch?.invoke(this, it) {
                Log.e("1234","错误日志;${it.message}")
                _errorFlow.emit(it)
            }
        }
        .collect(collector)


    suspend fun check(basicApi: BasicApiService, mustIsComplete: Boolean = false): Boolean {
        Log.e("1234", "check1")
        val userResponse = basicApi.user().checkAndGet()?.apply {
            AppGlobal.userResponse(this)
        } ?: throw NullPointerException("userinfo is null")
        Log.e("1234", "check2")
        basicApi.config().checkAndGet()?.apply {
            AppGlobal.configResponse(this)
        } ?: throw NullPointerException("config is null")
        Log.e("1234", "check3")
        AppGlobal.hearBeat(basicApi)
        AppGlobal.preloadGift()
        AppGlobal.preloadEmoji()
        val isComplete = userResponse.isComplete == "1"
        Log.e(
            "1234",
            "isComplete=${isComplete} imAccount=${userResponse.imAccount} imToken=${userResponse.imToken}"
        )
        if (isComplete) {
            val imAccount = userResponse.imAccount ?: throw NullPointerException("account is null")
            val imToken = userResponse.imToken ?: throw NullPointerException("token is null")
            IMHelper.loginHandler.login(imAccount, imToken)
            return true
        } else {
            if (mustIsComplete) {
                throw NullPointerException("userInfo must be complete")
            }
            return false
        }
    }

}

