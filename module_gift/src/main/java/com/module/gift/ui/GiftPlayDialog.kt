package com.module.gift.ui

import androidx.activity.compose.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.helper.develop.nav.*
import com.helper.develop.util.getIntOrNull
import com.helper.develop.util.getStringOrNull
import com.module.basic.route.*
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.*
import com.module.gift.R
import kotlinx.coroutines.*
import org.json.JSONObject


fun NavGraphBuilder.giftPlayDialog() = dialog(
    route = AppRoutes.GiftPlay.static,
    arguments = AppRoutes.GiftPlay.arguments, dialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    )
) {
    GiftPlayDialog()
}

@Composable
internal fun GiftPlayDialog() {
    val localBack = LocalOnBackPressedDispatcherOwner.current
    val localNav = LocalNavController.current
    val json = localNav.currentBackStackEntry!!.savedStateHandle.get<String>("json").orEmpty()
    val jsonObject = JSONObject(json)
    val giftId = jsonObject.getString("giftId").orEmpty()
    val svgFile = AppGlobal.getGiftSvgFileById(giftId)
    Box(modifier = Modifier.fillMaxSize()) {
        if (!svgFile.exists() || svgFile.length() <= 0L) {
            UpdateDialogWindow {
                it.dimAmount =0f
            }
            LaunchedEffect(Unit) {
                delay(3000)
                localBack?.onBackPressedDispatcher?.onBackPressed()
            }
        } else {
            AppSVGA(
                loops = 1,
                modifier = Modifier.fillMaxSize(),
                file = svgFile,
            ) {
                localBack?.onBackPressedDispatcher?.onBackPressed()
            }
        }
        Top(jsonObject)
    }
}

@Composable
private fun Top(jsonObject: JSONObject) {
    val floatingScreenId = jsonObject.getIntOrNull("floatingScreenId")
    if (floatingScreenId == 0) {
        return
    }
    val sendUid = jsonObject.getStringOrNull("sendUid").orEmpty()
    val sendName = jsonObject.getStringOrNull("sendName").orEmpty()
    val sendAvatar = jsonObject.getStringOrNull("sendAvatar").orEmpty()
    val receiveName = jsonObject.getStringOrNull("receiveName").orEmpty()
    val giftName = jsonObject.getStringOrNull("giftName").orEmpty()
    val giftCount = jsonObject.getIntOrNull("giftCount")
    val bg = when (floatingScreenId) {
        1 -> R.drawable.gift_bg_play_1
        2 -> R.drawable.gift_bg_play_2
        3 -> R.drawable.gift_bg_play_3
        4 -> R.drawable.gift_bg_play_4
        else -> R.drawable.gift_bg_play_1
    }
    val textColor = when (floatingScreenId) {
        1 -> Color(0xff83FF87)
        2 -> Color(0xff41F9FF)
        3 -> Color(0xff00763B)
        4 -> Color(0xff0033FF)
        else -> Color(0xff83FF87)
    }
    var isShow by remember {
        mutableStateOf(false)
    }
    val alpha by animateFloatAsState(if (isShow) 1f else 0f, animationSpec = tween(1500))
    val height by animateDpAsState(if (isShow) 81.dp else 0.dp, animationSpec = tween(1500))
    LaunchedEffect(Unit) {
        isShow = true
    }
    Column(
        modifier = Modifier
            .alpha(alpha)
            .paint(
                painter = painterResource(bg),
                contentScale = ContentScale.Crop
            )
            .height(height)
    ) {
        Box(modifier = Modifier.height(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 61.dp)
                .wrapContentSize()
        ) {
            AppImage(
                model = sendAvatar,
                modifier = Modifier
                    .size(31.dp)
                    .clip(CircleShape)
            )
            SpacerWidth(4.dp)
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(sendName, color = textColor, fontSize = 10.sp)
                    SpacerWidth(8.dp)
                    Text(
                        "UID:${sendUid}",
                        fontSize = 8.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                color = Color.White.copy(0.2f),
                                shape = RoundedCornerShape(7.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                SpacerHeight(4.dp)
                Row {
                    Text(
                        "send", fontSize = 10.sp,
                        color = Color.White,
                    )
                    SpacerWidth(4.dp)
                    Text(
                        receiveName, fontSize = 10.sp,
                        color = textColor,
                    )
                    SpacerWidth(4.dp)
                    Text(
                        "${giftName}x${giftCount}", fontSize = 10.sp,
                        color = Color.White,
                    )
                }
            }
        }
    }
}