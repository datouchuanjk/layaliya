package com.module.chatroom.ui

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.helper.develop.Background
import com.helper.develop.util.toast
import com.module.chatroom.R
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppBottomPickVisualSelected
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.util.appBrushBackground
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.chatroom.viewmodel.ChatroomReportViewModel


fun NavGraphBuilder.chatroomReportDialog() = dialog(
    route = AppRoutes.ChatroomReport.static,
    arguments = AppRoutes.ChatroomReport.arguments,
) {

    ChatroomReportDialog()
}

@Composable
internal fun ChatroomReportDialog(viewModel: ChatroomReportViewModel = apiHandlerViewModel()) {
    val back = LocalOnBackPressedDispatcherOwner.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .background(color = Color.Black.copy(0.75f), shape = RoundedCornerShape(20.dp))
                .padding(15.dp),
        ) {
            Text(
                text = stringResource(R.string.room_report_type),
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            SpacerHeight(4.dp)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                viewModel.reportList.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.id == viewModel.selectedReport?.id) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .border(1.dp, color = Color.Red, shape = CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .border(1.dp, color = Color.White, shape = CircleShape)
                                    .onClick {
                                        viewModel.selectedReport(item)
                                    })
                        }
                        SpacerWidth(4.dp)
                        Text(
                            text = item.content.orEmpty(),
                            fontSize = 10.sp,
                            color = Color(0xffe6e6e6)
                        )
                    }
                }
            }
            SpacerHeight(12.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(color = Color.White.copy(0.1f), shape = RoundedCornerShape(8.dp))
                    .padding(6.dp)
            ) {
                Text(
                    stringResource(R.string.room_please_enter_the_reason_for_reporting),
                    fontSize = 8.sp,
                    color = Color(0xffE6E6E6)
                )
                SpacerHeight(12.dp)
                BasicTextField(
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = viewModel.input,
                    onValueChange = viewModel::input,
                    textStyle = TextStyle(
                        fontSize = 8.sp,
                        color = Color.White
                    )
                )
            }
            SpacerHeight(8.dp)
            var isShow by remember {
                mutableStateOf(false)
            }

            AppBottomPickVisualSelected(
                isShow = isShow,
                onDismissRequest ={
                    isShow =false
                },
                onResult = {
                    viewModel.addImage(it)
                }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                viewModel.images.forEach { item ->
                    AppImage(
                        item,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
                if (viewModel.isShowAddImage) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color=Color.White.copy(0.1f))
                    ) {
                        AppImage(
                            R.drawable.room_ic_report_add_image,
                        ) {
                          isShow = true
                        }
                    }
                }
            }
            SpacerHeight(4.dp)
            Text(
                text = stringResource(R.string.room_upload_photos),
                fontSize = 8.sp,
                color = Color(0xffE6E6E6)
            )
            SpacerHeight(12.dp)
            Row(modifier = Modifier) {
                Text(
                    text = stringResource(R.string.room_cancel),
                    fontSize = 10.sp,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .height(26.dp)
                        .background(color =Color.White.copy(0.2f), shape = RoundedCornerShape(13.dp))
                        .onClick {
                            back?.onBackPressedDispatcher?.onBackPressed()
                        }
                        .wrapContentSize()
                )
                SpacerWidth(8.dp)
                val context = LocalContext.current
                LaunchedEffect(viewModel) {
                    viewModel.postSuccessfulFlow
                        .collect {
                            context.toast(R.string.room_report_successful)
                            back?.onBackPressedDispatcher?.onBackPressed()
                        }
                }
                Text(
                    text = stringResource(R.string.room_submit),
                    fontSize = 10.sp,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .height(26.dp)
                        .appBrushBackground(
                            shape = RoundedCornerShape(13.dp)
                        )
                        .onClick {
                            viewModel.post()
                        }
                        .wrapContentSize()
                )
        }
    }
}