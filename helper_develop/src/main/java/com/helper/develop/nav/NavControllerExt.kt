package com.helper.develop.nav

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import androidx.savedstate.*
import com.helper.develop.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import kotlin.coroutines.*

fun NavHostController.navigateTo(route: String) {
    val currentRoute: String? = currentDestination?.route
    navigate(route) {
        currentRoute?.let {
            popUpTo(it) {
                inclusive = true
            }
        }
    }
}


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

fun <T> NavHostController.setResult(value: T) {
    previousBackStackEntry?.savedStateHandle?.set("POP_RESULT", value)
}

suspend inline fun <reified T : Any> NavHostController.collectResult(
    key: String,
    crossinline block: (T) -> Unit
) {
    currentBackStackEntry?.savedStateHandle?.getMutableStateFlow(key, "${key}_DEFAULT")
        ?.collect {
            if (it == "${key}_DEFAULT") {
                return@collect
            }
            val jsonObject = JSONObject(it)
            if (jsonObject.has(key)) {
                jsonObject.getString(key).fromTypeJson<T>()?.let(block)
            }
        }
}

suspend fun <T : Any> NavHostController.emitResult(key: String, value: T) {
    val jsonObject = JSONObject()
    jsonObject.put("currentTimeMillis", System.currentTimeMillis())
    jsonObject.put(key, value.toJson())
    previousBackStackEntry?.savedStateHandle?.getMutableStateFlow(key, "${key}_DEFAULT")
        ?.emit(jsonObject.toString())
}

suspend fun NavHostController.waitPopBackStack(route: String) {
    suspendCancellableCoroutine { cancellable ->
        addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: SavedState?
            ) {
                if (destination.route != route) {
                    removeOnDestinationChangedListener(this)
                    cancellable.resume(Unit)
                }
            }
        })
    }
}

fun NavHostController.popToOrNavigate(route: String) {
    if (!popBackStack(route = route, inclusive = false)) {
        navigate(route) {
            launchSingleTop = true
            popUpTo(0)
        }
    }
}

fun NavHostController.navigateAndPopAll(route: String) {
    navigate(route) {
        launchSingleTop = true
        popUpTo(0)
    }
}

val NavBackStackEntry.isDialog: Boolean get() = destination.navigatorName == "dialog"

val NavHostController.currentRoute get() = currentBackStackEntry?.destination?.route


val NavHostController.currentComposableRoute: String?
    @SuppressLint("RestrictedApi")
    get() {
        // 遍历导航栈（通过迭代器，避免直接访问 value）
        try {
            for (entry in currentBackStack.value.reversed()) {
                if (!entry.isDialog) {
                    return entry.destination.route
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return if (currentBackStackEntry?.isDialog == true) {
                previousBackStackEntry?.destination?.route
            } else {
                currentBackStackEntry?.destination?.route
            }
        }
        return currentRoute
    }