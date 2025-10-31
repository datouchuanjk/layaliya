package com.module.app.ui

import androidx.activity.compose.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import com.helper.develop.nav.*
import com.helper.develop.util.getStringOrNull
import com.module.basic.route.*
import com.module.basic.ui.*
import com.module.basic.util.onClick
import com.module.noble.R
import kotlinx.coroutines.delay
import org.json.JSONObject


@Composable
internal fun TopPlay(json: String, block: () -> Unit) {
    Top(JSONObject(json))
    LaunchedEffect(Unit) {
        delay(3000)
        block()
    }
}

@Composable
private fun Top(jsonObject: JSONObject) {
    val localNav = LocalNavController.current
    val content = jsonObject.getStringOrNull("content").orEmpty()
    val icon = jsonObject.getStringOrNull("icon").orEmpty()
    val nickname = jsonObject.getStringOrNull("nickname").orEmpty()
    val levelName = jsonObject.getStringOrNull("level_name").orEmpty()
    val bg = when (levelName) {
        "baron" -> R.drawable.noble_bg_play_1
        "Viscount" -> R.drawable.noble_bg_play_2
        "count" -> R.drawable.noble_bg_play_3
        "marquis" -> R.drawable.noble_bg_play_4
        "duke" -> R.drawable.noble_bg_play_5
        "king" -> R.drawable.noble_bg_play_6
        else -> R.drawable.noble_bg_play_1
    }
    val textColor = when (levelName) {
        "baron" -> Color(0xff903D19)
        "Viscount" -> Color(0xff0051FF)
        "count" -> Color(0xff5E00FF)
        "marquis" -> Color(0xff7300FF)
        "duke" -> Color(0xff7B00FF)
        "king" -> Color(0xffFFFF00)
        else -> Color(0xff903D19)
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
            .onClick {
                localNav.navigate(AppRoutes.Noble.static)
            }
    ) {
        Box(modifier = Modifier.height(42.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 61.dp)
                .wrapContentSize()
        ) {
            Text(
                content.replaceFirst("*", nickname).replaceFirst("*", levelName),
                color = textColor,
                fontSize = 10.sp
            )
            SpacerWidth(4.dp)
            AppImage(
                model = icon,
                modifier = Modifier
                    .size(31.dp)
                    .clip(CircleShape)
            )
        }
    }
}