package com.module.game.ui

import android.view.Gravity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.*
import androidx.navigation.compose.*
import com.helper.develop.nav.LocalNavController
import com.module.basic.route.*
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.UpdateDialogWindow
import com.module.basic.ui.paging.AppPagingRefresh
import com.module.basic.ui.paging.items
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.game.viewmodel.GameListViewModel


fun NavGraphBuilder.gameListScreen() = dialog(
    route = AppRoutes.GameList.static,
    arguments = AppRoutes.GameList.arguments,
    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
) {
    val height = LocalWindowInfo.current.containerSize.height * 0.7f
    UpdateDialogWindow {
        it.gravity = Gravity.BOTTOM
        it.height = height.toInt()
    }
    GameListScreen()
}

@Composable
internal fun GameListScreen() {
    Scaffold{ innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            GameListScreenOnly(true)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GameListScreenOnly(
    isDialog: Boolean = false,
    viewModel: GameListViewModel = apiHandlerViewModel()
) {
    val localNav = LocalNavController.current
    AppPagingRefresh(
        pagingData = viewModel.pagingData
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            columns = GridCells.Fixed(2),
        ) {
            items(pagingData = viewModel.pagingData, key = { it.id.toString() }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onClick {
                            localNav.navigate(
                                if (isDialog) {
                                    AppRoutes.GameDialog
                                } else {
                                    AppRoutes.Game
                                }.dynamic(
                                    "roomId" to viewModel.roomId,
                                    "url" to it.path.toString(),
                                    "agentId" to it.agentId.toString(),
                                    "type" to it.type.toString(),
                                )
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppImage(
                        model = it.pic,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    SpacerHeight(5.dp)
                    Text(text = it.name.orEmpty(), fontSize = 14.sp, color = Color.Black)
                }
            }
        }
    }
}