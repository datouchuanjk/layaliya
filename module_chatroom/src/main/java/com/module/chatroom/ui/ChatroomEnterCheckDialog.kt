package com.module.chatroom.ui

import android.Manifest
import android.content.SharedPreferences
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.navigateAndPopCurrent
import com.module.basic.route.AppRoutes
import com.module.basic.sp.getHiddenIdentity
import com.module.basic.ui.UpdateDialogWindow
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.chatroom.viewmodel.ChatroomEnterCheckViewModel
import org.koin.androidx.compose.get


fun NavGraphBuilder.chatroomEnterCheckDialog() =
    dialog(
        route = AppRoutes.ChatroomEnterCheck.static,
        arguments = AppRoutes.ChatroomEnterCheck.arguments
    ) {
        UpdateDialogWindow {
            it.dimAmount = 0f
        }
        ChatroomEnterCheckDialog()
    }

/**
 * 进入房间之前的一个检查，
 */
@Composable
internal fun ChatroomEnterCheckDialog(viewModel: ChatroomEnterCheckViewModel = apiHandlerViewModel()) {
    val localNav = LocalNavController.current
    val localBack = LocalOnBackPressedDispatcherOwner.current
    val sp = get<SharedPreferences>()
    var isMysteriousPerson by remember {
        mutableStateOf(false)
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            localNav.navigateAndPopCurrent(
                AppRoutes.Chatroom.dynamic(
                    "roomId" to viewModel.roomId,
                    "isMysteriousPerson" to isMysteriousPerson,
                )
            )
        } else {
            localBack?.onBackPressedDispatcher?.onBackPressed()
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.checkResultfulFlow
            .collect {
                isMysteriousPerson = if (it) {
                    sp.getHiddenIdentity()
                } else {
                    false
                }
                launcher.launch(Manifest.permission.RECORD_AUDIO)
            }
    }
}
