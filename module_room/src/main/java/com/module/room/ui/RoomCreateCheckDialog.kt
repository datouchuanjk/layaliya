package com.module.room.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.navigateAndPopCurrent
import com.module.basic.route.AppRoutes
import com.module.basic.ui.UpdateDialogWindow
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.room.viewmodel.RoomCheckViewModel

fun NavGraphBuilder.roomCreateCheckDialog() = dialog(route = AppRoutes.RoomCreateCheck.static) {
    UpdateDialogWindow {
        it.dimAmount = 0f
    }
    RoomCreateCheckDialog()
}

@Composable
internal fun RoomCreateCheckDialog(viewModel: RoomCheckViewModel = apiHandlerViewModel()) {
    val localNav = LocalNavController.current
    LaunchedEffect(Unit) {
        viewModel.checkResultfulFlow
            .collect {
                if (it == null) {
                    localNav.popBackStack()
                } else {
                    localNav.navigateAndPopCurrent(
                        AppRoutes.CreateOrEditRoom.dynamic(
                            "type" to it.type,
                            "roomInfo" to it.roomInfo
                        )
                    )
                }
            }
    }
}