package com.module.emoji.ui


import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.helper.develop.nav.LocalNavController
import com.module.basic.route.AppRoutes
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.AppFileImage
import com.module.basic.ui.AppImage
import com.module.basic.ui.AppTabRow
import com.module.basic.ui.SpacerHeight
import com.module.basic.util.*
import com.module.basic.viewmodel.*
import com.module.emoji.R
import com.module.emoji.viewmodel.EmojiViewModel


fun NavGraphBuilder.emojiDialog() = dialog(
    route = AppRoutes.Emoji.static,
    arguments = AppRoutes.Emoji.arguments,
    dialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false
    )
) {
    EmojiDialog()
}

@Composable
internal fun EmojiDialog(viewModel: EmojiViewModel = apiHandlerViewModel()) {
    val localBack = LocalOnBackPressedDispatcherOwner.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
    ) {
        val rectList = remember {
            mutableStateListOf<Rect>()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .onClick {
                        localBack?.onBackPressedDispatcher?.onBackPressed()
                    })
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xff000000).copy(0.6f),
                        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
                    )
            ) {

                val state = rememberLazyGridState()
                LazyVerticalGrid(
                    contentPadding = PaddingValues(vertical = 15.dp),
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .height(217.dp + 48.dp),
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    itemsIndexed(items = viewModel.list) { index, item ->
                        AppFileImage(
                            model = AppGlobal.getEmojiFileById(item),
                            modifier = Modifier
                                .width(72.dp)
                                .height(72.dp)
                                .clip(CircleShape)
                                .onGloballyPositioned { coordinates ->
                                    rectList.add(coordinates.boundsInRoot())
                                }
                                .onClick {
                                    viewModel.addAnimationRectList(item, rectList[index])
                                }
                        )
                    }
                }
                SpacerHeight(24.dp)
            }
        }
        viewModel.animationRectList.forEach {
            FlyBox(it, viewModel)
        }
    }
}

@Composable
private fun FlyBox(item: Pair<String, Rect>, viewModel: EmojiViewModel) {
    val localNav = LocalNavController.current
    DisposableEffect(Unit) {
        onDispose {
            viewModel.removeAnimationRectList(item,localNav)
        }
    }
    val localDensity = LocalDensity.current
    var isShow by remember {
        mutableStateOf(false)
    }
    val rect = item.second
    val id = item.first
    val scaleX by animateFloatAsState(
        targetValue = if (isShow) viewModel.x else rect.left,
        animationSpec = tween(500)
    )
    val scaleY by animateFloatAsState(
        targetValue = if (isShow) viewModel.y else rect.top,
        animationSpec = tween(500)
    )
    val scaleW by animateFloatAsState(
        targetValue = if (isShow) viewModel.w else rect.width,
        animationSpec = tween(500)
    )
    val scaleH by animateFloatAsState(
        targetValue = if (isShow) viewModel.h else rect.height,
        animationSpec = tween(500),
        finishedListener = {
            viewModel.removeAnimationRectList(item,localNav)
        }
    )

    val width = with(localDensity) {
        scaleW.toDp()
    }
    val height = with(localDensity) {
        scaleH.toDp()
    }
    LaunchedEffect(Unit) {
        isShow = true
    }
    AppFileImage(
        model = AppGlobal.getEmojiFileById(id),
        modifier = Modifier
            .offset {
                IntOffset(x = scaleX.toInt(), y = scaleY.toInt())
            }
            .width(width)
            .height(height)
            .scale(1.1f)
            .clip(CircleShape)
    )
}


