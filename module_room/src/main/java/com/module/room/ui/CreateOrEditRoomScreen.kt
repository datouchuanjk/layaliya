package com.module.room.ui

import android.util.Log
import android.view.Gravity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helper.develop.nav.*
import com.helper.develop.util.CustomActivityResultContracts
import com.helper.develop.util.launchImageOnly
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppBottomPickVisualSelected
import com.module.basic.ui.AppCountryPicker
import com.module.basic.ui.AppDialog
import com.module.basic.ui.AppImage
import com.module.basic.ui.AppLanguagePicker
import com.module.basic.ui.AppMoreIcon
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.AppTitleBar
import com.module.basic.util.*
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.room.R
import com.module.room.viewmodel.CreateOrEditRoomViewModel

fun NavGraphBuilder.createOrEditRoomScreen() =
    composable(
        route = AppRoutes.CreateOrEditRoom.static,
        arguments = AppRoutes.CreateOrEditRoom.arguments
    ) {
        CreateOrEditRoomScreen()
    }

/**
 * 创建/编辑房间
 */
@Composable
internal fun CreateOrEditRoomScreen(viewModel: CreateOrEditRoomViewModel = apiHandlerViewModel()) {
    val localBack = LocalOnBackPressedDispatcherOwner.current
    val localNav = LocalNavController.current
    LaunchedEffect(Unit) {
        viewModel.roomCreateSuccessful
            .collect {
                localNav.navigateTo(AppRoutes.ChatroomEnterCheck.dynamic(
                    "roomId" to it
                ))
            }
    }
    LaunchedEffect(Unit) {
        viewModel.roomEditSuccessful
            .collect {
                localNav.setResult(true)
                localBack?.onBackPressedDispatcher?.onBackPressed()
            }
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xfff5f5f5))
                .padding(it.calculateBottomPadding())
        ) {
            AppTitleBar(
                text = if (viewModel.isEdit) stringResource(R.string.room_chatroom_settings) else stringResource(
                    R.string.room_chatroom_create
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                SpacerHeight(4.dp)
                Cover(viewModel)
                SpacerHeight(12.dp)
                NameAndAnnouncement(viewModel)
                SpacerHeight(12.dp)
                Country(viewModel)
                SpacerHeight(12.dp)
                Language(viewModel)
                SpacerHeight(12.dp)
                RoomType(viewModel)
                SpacerHeight(12.dp)
                NumberOfMic(viewModel)
            }
            Save(viewModel)
        }
    }
}

/**
 * 选取
 */
@Composable
private fun Cover(viewModel: CreateOrEditRoomViewModel) {

    var isShow by remember {
        mutableStateOf(false)
    }
    AppBottomPickVisualSelected(
        isShow = isShow,
        onDismissRequest ={
            isShow =false
        },
        onResult = {
            viewModel.cover(it[0])
        }
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .onClick {
                isShow = true
            }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.room_cover),
            fontSize = 20.sp,
            color = Color(0xff333333)
        )
        SpacerWeight(1f)
        Image(
            painter = painterResource(R.drawable.room_ic_take_pic),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        SpacerWidth(14.dp)
        AppMoreIcon()
    }
}

@Composable
private fun NameAndAnnouncement(
    viewModel: CreateOrEditRoomViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(R.string.room_name),
            fontSize = 20.sp,
            color = Color(0xff333333)
        )
        SpacerHeight(6.dp)
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xfff5f5f5), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            value = viewModel.name,
            onValueChange = viewModel::name,
            textStyle = TextStyle(fontSize = 16.sp, color = Color(0xff333333)),
            decorationBox = {
                if (viewModel.name.isEmpty()) {
                    Text(
                        text = stringResource(R.string.room_please_enter_the_name),
                        style = TextStyle(fontSize = 16.sp, color = Color(0xffcccccc))
                    )
                }
                it()
            }
        )
        SpacerHeight(12.dp)
        Text(
            text = stringResource(R.string.room_announcement),
            fontSize = 20.sp,
            color = Color(0xff333333)
        )
        SpacerHeight(6.dp)
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xfff5f5f5), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            value = viewModel.announcement,
            onValueChange = viewModel::announcement,
            textStyle = TextStyle(fontSize = 16.sp, color = Color(0xff333333)),
            decorationBox = {
                if (viewModel.announcement.isEmpty()) {
                    Text(
                        text = stringResource(R.string.room_enter_the_announcement),
                        style = TextStyle(fontSize = 16.sp, color = Color(0xffcccccc))
                    )
                }
                it()
            }
        )
    }
}

@Composable
private fun Country(viewModel: CreateOrEditRoomViewModel) {
    var isShow by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .onClick {
                isShow = true
            }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                    viewModel.country(it)
                }
            }
        }
        Text(
            text = stringResource(R.string.room_country_or_region),
            fontSize = 20.sp,
            color = Color(0xff333333)
        )
        SpacerWeight(1f)
        Text(
            text = viewModel.country?.name.orEmpty(),
            fontSize = 16.sp,
            color = Color(0xff999999)
        )
        SpacerWidth(14.dp)
        AppMoreIcon()
    }
}

@Composable
private fun Language(viewModel: CreateOrEditRoomViewModel) {
    var isShow by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .onClick {
                isShow = true
            }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                    viewModel.language(it)
                }
            }
        }
        Text(
            text = stringResource(R.string.room_language),
            fontSize = 20.sp,
            color = Color(0xff333333)
        )
        SpacerWeight(1f)
        Text(
            text = viewModel.language?.name.orEmpty(),
            fontSize = 16.sp,
            color = Color(0xff999999)
        )
        SpacerWidth(14.dp)
        AppMoreIcon()
    }
}

@Composable
private fun RoomType(viewModel: CreateOrEditRoomViewModel) {
    val roomTypeList = viewModel.roomTypeList
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.room_room_type),
                fontSize = 20.sp,
                color = Color(0xff333333)
            )
        }
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            maxItemsInEachRow = 3
        ) {
            roomTypeList.forEachIndexed { index, item ->
                Text(
                    item.name,
                    color = if (viewModel.roomType?.id == item.id) Color(0xffFF4070) else Color(
                        0xff999999
                    ),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .then(
                            if (viewModel.roomType?.id == item.id) Modifier
                                .background(
                                    color = Color(0xffFFEBF0),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    1.dp,
                                    color = Color(0xffFF4070),
                                    shape = RoundedCornerShape(16.dp)
                                ) else Modifier.background(
                                color = Color(0xfff5f5f5), shape = RoundedCornerShape(16.dp)
                            )
                        )
                        .onClick {
                            viewModel.roomType(item)
                        }
                        .wrapContentSize()
                )
            }
        }
    }
}

@Composable
private fun NumberOfMic(viewModel: CreateOrEditRoomViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.room_number_of_mic),
                fontSize = 20.sp,
                color = Color(0xff333333)
            )
        }
        BoxWithConstraints {
            val width = ((maxWidth - 12.dp) / 2).value.toInt().dp
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                maxItemsInEachRow = 2,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                viewModel.numList.forEachIndexed { _, item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(width)
                            .onClick {
                                viewModel.micNum(item)
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(154f / 124)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    color = if (item.code == viewModel.micNum?.code) Color(
                                        0xffFF4070
                                    ) else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(1.dp)
                        ) {
                            AppImage(
                                model = item.image,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .fillMaxSize()
                            )
                        }
                        SpacerHeight(4.dp)
                        Text(item.name, fontSize = 14.sp, color = item.textColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun Save(viewModel: CreateOrEditRoomViewModel) {
    Text(
        text = stringResource(R.string.room_save),
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .padding(top = 10.dp)
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .appBrushBackground(
                shape = RoundedCornerShape(40.dp)
            )
            .onClick {
                viewModel.roomCreate()
            }
            .wrapContentSize()
    )
}