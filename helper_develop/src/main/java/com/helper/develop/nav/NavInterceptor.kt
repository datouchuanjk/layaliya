package com.helper.develop.nav

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch


@Deprecated("")
fun <T> NavHostController.navigateForResult(route: String, block: (T?) -> Unit) {
    val lastBackStackEntry = currentBackStackEntry
    navigate(route)
    lastBackStackEntry?.lifecycleScope?.launch {
        currentBackStackEntryFlow.filter {
            it == lastBackStackEntry
        }.take(1)
            .map {
                it.savedStateHandle
            }.collect {
                try {
                    val result = it.get<T>("POP_RESULT")
                    it.remove<T>("POP_RESULT")
                    block(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                    block(null)
                }
            }
    }
}

@Deprecated("")
fun <T> NavHostController.setResult(value: T) {
    previousBackStackEntry?.savedStateHandle?.set("POP_RESULT", value)
}
