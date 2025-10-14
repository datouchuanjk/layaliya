package com.module.basic.ui

import android.view.Window
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

@Composable
fun UpdateDialogWindow(block: (Window.(WindowManager.LayoutParams) -> Unit)){
    val window = (LocalView.current.parent as DialogWindowProvider).window
    val attributes = window.attributes
    block(window,attributes)
    window.attributes = attributes
}


@Composable
fun AppDialog(
    onDismissRequest: () -> Unit,
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    usePlatformDefaultWidth: Boolean = true,
    layoutParamsSetting: (Window.(WindowManager.LayoutParams) -> Unit)? = null,
    content: @Composable () -> Unit
) {

        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnClickOutside = dismissOnClickOutside,
                dismissOnBackPress = dismissOnBackPress,
                usePlatformDefaultWidth = usePlatformDefaultWidth,
            )
        ) {
            layoutParamsSetting?.let {
                UpdateDialogWindow(layoutParamsSetting)
            }
            content()
        }
}