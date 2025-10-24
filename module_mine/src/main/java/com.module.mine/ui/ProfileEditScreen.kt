package com.module.mine.ui

import android.view.*
import androidx.activity.compose.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.navigateAndPopAll
import com.helper.develop.nav.setResult
import com.helper.develop.util.*
import com.module.basic.route.*
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.*
import com.module.basic.ui.picker.AppCountryPicker
import com.module.basic.ui.picker.AppDatePicker
import com.module.basic.ui.picker.AppLanguagePicker
import com.module.basic.ui.picker.AppSexPicker
import com.module.basic.util.*
import com.module.basic.viewmodel.*
import com.module.mine.R
import com.module.mine.viewmodel.*


fun NavGraphBuilder.personEditScreen() =
    composable(route = AppRoutes.PersonEdit.static, arguments = AppRoutes.PersonEdit.arguments) {
        PersonEditScreen()
    }

@Composable
internal fun PersonEditScreen(viewModel: ProfileEditViewModel = apiHandlerViewModel()) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            AppTitleBar(
                text = stringResource(R.string.mine_profile),
                showBackIcon = !viewModel.isFomComplete,
            )
        }
    ) { innerPadding ->
        val context = LocalContext.current
        val localNav = LocalNavController.current
        val localBack = LocalOnBackPressedDispatcherOwner.current
        LaunchedEffect(viewModel) {
            viewModel.editSuccessfulFlow.collect {
                context.toast(R.string.mine_edit_successful)
                localNav.setResult(true)
                localBack?.onBackPressedDispatcher?.onBackPressed()
            }
        }
        LaunchedEffect(viewModel) {
            viewModel.checkCompleteFlow.collect {
                localNav.navigateAndPopAll(AppRoutes.Main.static)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Avatar(viewModel)
            Line()
            Nickname(viewModel)
            if (!viewModel.isFomComplete) {
                Line()
                Introduce(viewModel)
            }
            Line()
            Sex(viewModel)
            if (viewModel.isFomComplete) {
                Line()
                Language(viewModel)
            }
            Line()
            Country(viewModel)
            Line()
            Birthday(viewModel)
            Text(
                text = stringResource(R.string.mine_edit),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
                    .appBrushBackground(shape = RoundedCornerShape(40.dp))
                    .onClick {
                        viewModel.edit()
                    }
                    .wrapContentSize()
            )
        }
    }
}

@Composable
private fun Avatar(viewModel: ProfileEditViewModel) {
    var isShow by remember {
        mutableStateOf(false)
    }
    AppBottomPickVisualSelected(
        isShow = isShow,
        onDismissRequest ={
            isShow =false
        },
        onResult = {
            viewModel.avatar(it[0])
        }
    )
    Item(stringResource(R.string.mine_avatar), onClick = {
        isShow = true
    }) {
        AppImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape), model = viewModel.avatar
        )
    }
}

@Composable
private fun Nickname(viewModel: ProfileEditViewModel) {
    Item(stringResource(R.string.mine_nickname)) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End),
            value = viewModel.nickname.orEmpty(),
            onValueChange = viewModel::nickname,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color(0xff666666)
            ),
            decorationBox = {
                if (viewModel.nickname.isNullOrEmpty()) {
                    Text(
                        text = stringResource(R.string.mine_please_input),
                        fontSize = 16.sp,
                        color = Color(0xff999999)
                    )
                }
                it()
            }
        )
    }
}

@Composable
private fun Introduce(viewModel: ProfileEditViewModel) {
    Item(stringResource(R.string.mine_introduce)) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End),
            value = viewModel.introduce.orEmpty(),
            onValueChange = viewModel::introduce,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color(0xff666666)
            ),
            decorationBox = {
                if (viewModel.introduce.isNullOrEmpty()) {
                    Text(
                        text = stringResource(R.string.mine_please_input),
                        fontSize = 16.sp,
                        color = Color(0xff999999)
                    )
                }
                it()
            }
        )
    }
}

@Composable
private fun Sex(viewModel: ProfileEditViewModel) {
    var isShow by remember {
        mutableStateOf(false)
    }
    Box {
        Item(stringResource(R.string.mine_sex), onClick = {
            isShow = true
        }) {
            Text(
                text = when (viewModel.sex) {
                    1 -> stringResource(R.string.mine_sex_man)
                    0 -> stringResource(R.string.mine_sex_woman)
                    else -> stringResource(R.string.mine_sex_unknow)
                },
                fontSize = 16.sp,
                color = Color(0xff666666)
            )
        }
        if (isShow) {
            AppDialog(
                usePlatformDefaultWidth = false,
                layoutParamsSetting = {
                    it.dimAmount = 0.1f
                    it.gravity = Gravity.BOTTOM
                }, onDismissRequest = { isShow = false }
            ) {
                AppSexPicker(
                    modifier = Modifier.background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
                ) {
                    isShow = false
                    viewModel.sex(it)
                }
            }
        }
    }
}


@Composable
private fun Language(viewModel: ProfileEditViewModel) {
    var isShow by remember {
        mutableStateOf(false)
    }
    Box {
        Item(stringResource(R.string.mine_language), onClick = {
            isShow = true
        }) {
            Text(
                text = AppGlobal.getLanguageByCode(viewModel.language)?.name.orEmpty(),
                fontSize = 16.sp,
                color = Color(0xff666666)
            )
        }
        if (isShow) {
            AppDialog(
                usePlatformDefaultWidth = false,
                layoutParamsSetting = {
                    it.dimAmount = 0.1f
                    it.gravity = Gravity.BOTTOM
                }, onDismissRequest = { isShow = false }
            ) {
                AppLanguagePicker(
                    modifier = Modifier.background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
                ) {
                    isShow = false
                    viewModel.language(it.code)
                }
            }
        }
    }
}

@Composable
private fun Country(viewModel: ProfileEditViewModel) {
    var isShow by remember {
        mutableStateOf(false)
    }
    Box {
        Item(stringResource(R.string.mine_country), onClick = {
            isShow = true
        }) {
            Text(
                text = AppGlobal.getCountryByCode(viewModel.countryCode)?.name.orEmpty(),
                fontSize = 16.sp,
                color = Color(0xff666666)
            )
        }
        if (isShow) {
            AppDialog(
                usePlatformDefaultWidth = false,
                layoutParamsSetting = {
                    it.dimAmount = 0.1f
                    it.gravity = Gravity.BOTTOM
                }, onDismissRequest = { isShow = false }
            ) {
                AppCountryPicker(
                    modifier = Modifier.background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
                ) {
                    isShow = false
                    viewModel.countryCode(it.code)
                }
            }
        }
    }
}

@Composable
private fun Birthday(viewModel: ProfileEditViewModel) {
    var isShow by remember {
        mutableStateOf(false)
    }
    Item(stringResource(R.string.mine_birthday), onClick = {
        isShow = true
    }) {
        Text(
            text = viewModel.birthDay.orEmpty(),
            fontSize = 16.sp,
            color = Color(0xff666666)
        )
    }
    if (isShow) {
        AppDialog(
            usePlatformDefaultWidth = false,
            layoutParamsSetting = {
                it.dimAmount = 0.1f
                it.gravity = Gravity.BOTTOM
            }, onDismissRequest = { isShow = false }
        ) {
            AppDatePicker(
                modifier = Modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                )
            ) {
                isShow = false
                viewModel.birthDay("${it.year}-${it.month + 1}-${it.dayOfMonth}")
            }
        }
    }
}

@Composable
private fun Item(
    text: String,
    withMoreIcon: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .onClick {
                onClick?.invoke()
            }
            .padding(15.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color(0xff333333)
        )
        SpacerWeight(1f)
        SpacerWidth(15.dp)
        content()
        if (withMoreIcon) {
            SpacerWidth(8.dp)
            AppMoreIcon()
        }
    }
}

@Composable
private fun Line() {
    HorizontalDivider(
        thickness = 1.dp,
        color = Color(0xfff5f5f5),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    )
}