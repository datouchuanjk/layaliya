package com.module.setting.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.helper.develop.util.toast
import com.module.basic.viewmodel.BaseViewModel
import com.module.setting.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class SettingViewModel(private val application: Application) : BaseViewModel() {

    fun clearCache() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                application.cacheDir.deleteRecursively()
            }
            application.toast(R.string.setting_cache_has_been_cleared)
        }
    }
}