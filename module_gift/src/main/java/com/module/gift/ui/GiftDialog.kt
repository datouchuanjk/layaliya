package com.module.gift.ui

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.helper.develop.nav.*
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppListPopup
import com.module.basic.ui.AppTabRow
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.UpdateDialogWindow
import com.module.basic.util.*
import com.module.basic.viewmodel.*
import com.module.gift.R
import com.module.gift.viewmodel.*


fun NavGraphBuilder.giftDialog() = dialog(
    route = AppRoutes.Gift.static,
    arguments = AppRoutes.Gift.arguments, dialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    )
) {
    val height = LocalWindowInfo.current.containerSize.height * 0.5f
    UpdateDialogWindow {
        it.gravity = Gravity.BOTTOM
        it.height = height.toInt()
    }
    GiftDialog()
}

@Composable
internal fun GiftDialog(viewModel: GiftViewModel = apiHandlerViewModel()) {
    val localNav = LocalNavController.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color(0xff000000).copy(0.6f),
                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
            )
    ) {
        if (viewModel.getGiftInfoLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(strokeWidth = 3.dp, color = Color.White)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                AppTabRow(
                    modifier = Modifier
                        .padding(horizontal = 11.dp)
                        .padding(top = 12.dp),
                    tabs = viewModel.tabs.map { it.name.orEmpty() }.toTypedArray(),
                    unselectedTextStyle = TextStyle(
                        color = Color(0xff999999),
                        fontSize = 20.sp
                    ),
                    selectedTextStyle = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp
                    ),
                    selectedIndex = viewModel.index,
                    onIndexChanged = viewModel::index
                )
                SpacerHeight(10.dp)
                GiftDialogItem(viewModel)
                SpacerHeight(24.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    Text(stringResource(R.string.gift_to), fontSize = 14.sp, color = Color.White)
                    SpacerWidth(8.dp)
                    var isShowPopup by remember {
                        mutableStateOf(false)
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(
                                color = Color(0xff3C3A43),
                                shape = RoundedCornerShape(20.dp)
                            ).onClick(viewModel.userInfos.size>1){
                                isShowPopup = true
                            }
                            .padding(vertical = 7.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (isShowPopup) {
                            AppListPopup(
                                list = viewModel.userInfos,
                                map = { it.nickname },
                                onDismissRequest = {
                                    isShowPopup = false
                                },
                                popupPosition = { anchorBounds: IntRect, windowSize: IntSize, popupContentSize: IntSize ->
                                    IntOffset(
                                        anchorBounds.left,
                                        anchorBounds.top - popupContentSize.height - 5.dp.roundToPx()
                                    )
                                }, onSelected = {
                                    viewModel.setCurrentUserInfo(it)
                                }
                            )
                        }
                        Text(viewModel.currentUserInfo?.nickname.orEmpty(), fontSize = 10.sp, color = Color.White)
                        if(viewModel.userInfos.size>1){
                            SpacerWidth(8.dp)
                            Image(
                                painter = painterResource(R.drawable.gift_ic_more),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                            )
                        }
                    }
                    SpacerWeight(1f)
                    if(viewModel.selectedItem!=null){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            var isShowPopup by remember {
                                mutableStateOf(false)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .height(28.dp)
                                    .width(60.dp)
                                    .background(
                                        color = Color(0xff3C3A43),
                                        shape = RoundedCornerShape(
                                            topStart = 20.dp,
                                            bottomStart = 20.dp
                                        )
                                    )
                                    .onClick(!viewModel.selectedItemHasSvg) {
                                        isShowPopup = true
                                    }
                            ) {
                                Text(
                                    "X ${viewModel.selectedNum}",
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                                SpacerWidth(8.dp)
                                Image(
                                    painter = painterResource(R.drawable.gift_ic_more),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                )
                                if (isShowPopup) {
                                    AppListPopup(
                                        list = viewModel.numList,
                                        map = { it.toString() },
                                        onDismissRequest = {
                                            isShowPopup = false
                                        },
                                        popupPosition = { anchorBounds: IntRect, windowSize: IntSize, popupContentSize: IntSize ->
                                            IntOffset(
                                                anchorBounds.left,
                                                anchorBounds.top - popupContentSize.height - 5.dp.roundToPx()
                                            )
                                        }, onSelected = {
                                            viewModel.numIndex(it)
                                        }
                                    )
                                }
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .height(28.dp)
                                    .width(60.dp)
                                    .appBrushBackground(
                                        shape = RoundedCornerShape(
                                            topEnd = 20.dp,
                                            bottomEnd = 20.dp
                                        )
                                    )
                                    .onClick {
                                        viewModel.sendGift(localNav)
                                    }) {
                                if (viewModel.sendGiftLoading) {
                                    CircularProgressIndicator(
                                        strokeWidth = 1.dp,
                                        color = Color(0xff333333),
                                        modifier = Modifier.size(15.dp)
                                    )
                                } else {
                                    Text(
                                        stringResource(R.string.gift_send),
                                        fontSize = 10.sp,
                                        color = Color.White,
                                    )
                                }
                            }
                        }
                    }
                }
                SpacerHeight(32.dp)
            }
        }
    }
}


