package com.helper.develop.nav

import androidx.navigation.*
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.savedstate.SavedState
import com.helper.develop.util.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.random.Random

fun NavHostController.navigateAndPopCurrent(route: String) {
    navigate(route) {
        popUpTo(currentDestination?.id ?: 0) {
            inclusive = true
        }
    }
}

fun NavHostController.navigateAndPopAll(route: String) {
    navigate(route) {
        popUpTo(0)
    }
}

suspend fun NavHostController.navigateAndWaitPop(route: String) {
    val currentBackStackEntry = currentBackStackEntry
    suspendCancellableCoroutine { b ->
        addOnDestinationChangedListener(object : OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: SavedState?
            ) {
                if (destination.route == currentBackStackEntry?.destination?.route) {
                    removeOnDestinationChangedListener(this)
                    b.resume(Unit)
                }
            }
        })
    }
    navigate(route)
}

@PublishedApi
internal fun NavHostController.getBackStackEntryOrNull(route: String): NavBackStackEntry? {
    return try {
        getBackStackEntry(route)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


suspend inline fun <reified T : Any> NavHostController.collectResultFrom(
    route: String = currentBackStackEntry?.destination?.route.orEmpty(),
    key: String,
    crossinline block: (T?) -> Unit
) {
    val initialValue = "${key}_INITIAL_VALUE"
    getBackStackEntryOrNull(route)
        ?.savedStateHandle
        ?.getMutableStateFlow(key, initialValue)
        ?.filter {
            it != initialValue
        }?.map {
            JSONObject(it)
        }?.filter {
            it.has(key)
        }?.map {
            it.getStringOrNull(key)
        }?.map {
            it?.fromTypeJson<T>()
        }?.collect {
            block(it)
        }
}

suspend fun <T : Any> NavHostController.emitResultTo(
    route: String = previousBackStackEntry?.destination?.route.orEmpty(),
    key: String,
    value: T
) {
    val initialValue = "${key}_INITIAL_VALUE"
    val jsonObject = JSONObject()
    jsonObject.put("currentTimeMillis", System.currentTimeMillis())
    jsonObject.put("random", Random.nextInt(100))
    jsonObject.put(key, value.toJson())
    getBackStackEntryOrNull(route)
        ?.savedStateHandle
        ?.getMutableStateFlow(key, initialValue)
        ?.emit(jsonObject.toString())
}

