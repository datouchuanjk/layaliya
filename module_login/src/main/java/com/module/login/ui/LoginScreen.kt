package com.module.login.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.navigateTo
import com.helper.develop.util.toJson
import com.helper.develop.util.toast
import com.module.basic.route.AppRoutes
import com.module.basic.ui.*
import com.module.login.R
import com.module.login.viewmodel.LoginViewModel
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.basic.util.onClick
import com.module.login.util.getGoogleLoginIntent
import kotlinx.coroutines.delay

fun NavGraphBuilder.loginScreen() = composable(route = AppRoutes.Login.static) {
    LoginScreen()
}

@Composable
internal fun LoginScreen(viewModel: LoginViewModel = apiHandlerViewModel()) {
    val localActivity = LocalActivity.current
    BackHandler {
        localActivity?.finish()
    }
    Scaffold {
        val localContext = LocalContext.current
        val googleLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val idToken = task.result.idToken
                    if (idToken.isNullOrEmpty()) {
                        localContext.toast(R.string.login_error_google)
                        return@rememberLauncherForActivityResult
                    }
                    viewModel.googleLogin(idToken)
                }else{
                    localContext.toast(R.string.login_error_google)
                }
            }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(R.drawable.login_bg),
                    contentScale = ContentScale.Crop
                )
                .padding(it)
        ) {
            SpacerHeight(140.dp)
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(R.drawable.login_ic_logo),
                contentDescription = null
            )
            SpacerHeight(16.dp)
            Image(
                painter = painterResource(R.drawable.login_ic_app_name),
                contentDescription = null
            )
            SpacerWeight()
            val hostController = LocalNavController.current
            LaunchedEffect(Unit) {
                viewModel.loginResultFlow.collect {
                    if(it)
                    hostController.navigateTo(AppRoutes.Main.static)
                    else{
                        hostController.navigateTo(   AppRoutes.PersonEdit.dynamic(
                            "fromComplete" to true
                        ))
                    }
                }
            }
            Text(
                text = stringResource(R.string.login_google),
                color = Color(0xff333333),
                fontSize = 20.sp,
                modifier = Modifier.buttonModifier {
                    getGoogleLoginIntent(localContext)?.let {
                        googleLauncher.launch(it)
                    }
                }
            )
            SpacerHeight(16.dp)
            Text(
                text = stringResource(R.string.login_facebook),
                color = Color(0xff333333),
                fontSize = 20.sp,
                modifier = Modifier.buttonModifier {
                    viewModel.facebookLogin()
                }
            )
            SpacerHeight(50.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 41.dp)
            ) {
                CheckBox(
                    block = { viewModel.isCheck },
                    clickable = viewModel::check,
                )
                SpacerWidth(8.dp)
                AgreeText(viewModel) {

                }
            }
            SpacerHeight(50.dp)
        }
    }
}

@Composable
private fun CheckBox(block: () -> Boolean, clickable: () -> Unit) {
    val isChecked = block()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(12.dp)
            .border(width = 1.dp, color = Color(0xffD9D9D9), shape = CircleShape)
            .clip(CircleShape)
            .onClick {
                clickable()
            }
    ) {
        if (isChecked) {
            AppImage(
                modifier = Modifier.size(8.dp),
                model = R.drawable.login_ic_gou
            )
        }
    }
}

/**
 * 按钮通用 样式 封装一下
 */
private fun Modifier.buttonModifier(block: () -> Unit) = fillMaxWidth()
    .padding(horizontal = 15.dp)
    .height(56.dp)
    .background(
        color = Color.White, shape = RoundedCornerShape(16.dp)
    )
    .clip(RoundedCornerShape(16.dp))
    .border(
        width = 1.dp,
        color = Color(0xff333333),
        shape = RoundedCornerShape(16.dp)
    )
    .clickable {
        block()
    }
    .wrapContentSize()

/**
 * 政策文本
 */
@Composable
private fun AgreeText(viewModel: LoginViewModel, block: (Int) -> Unit) {
    var shouldShake by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition()
    val shakeTranslation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    LaunchedEffect(Unit) {
        viewModel.loginWithNoCheck.collect {
            shouldShake = true
            delay(500)
            shouldShake = false
        }
    }
    val strings = stringArrayResource(R.array.login_agree)
    Text(
        modifier = Modifier.then(if (shouldShake) Modifier.graphicsLayer {
            translationX = shakeTranslation
        } else Modifier),
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        color = Color(0xff999999),
        text = buildAnnotatedString {
            strings.forEachIndexed { index, it ->
                when (index) {
                    2 -> appendLine(it)
                    1, 3 -> withLink(
                        LinkAnnotation.Clickable(
                            index.toString(), TextLinkStyles(
                                style = SpanStyle(
                                    color = Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        ) {
                            block(index)
                        }) {
                        append(" $it ")
                    }

                    else -> append(it)
                }
            }
        }
    )
}