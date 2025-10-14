package com.module.agent.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.agent.R
import com.module.agent.ui.dialog.InformationDialog
import com.module.agent.viewmodel.BasicInformationViewModel
import com.module.basic.ui.paging.AppPagingBox
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel


@Composable
internal fun BasicInformation(viewModel: BasicInformationViewModel = apiHandlerViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
    ) {
        Top(viewModel)
        Invitation(viewModel)
        Bottom(viewModel)
    }
}


@Composable
private fun Top(viewModel: BasicInformationViewModel) {
    val agentIngo = viewModel.agentInfo
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        TopInput(
            text = stringResource(R.string.agent_proxy_name),
            value = agentIngo?.nickname.orEmpty()
        )
        SpacerHeight(20.dp)
        TopInput(
            text = stringResource(R.string.agent_code),
            value = agentIngo?.id?.toString().orEmpty()
        )
        SpacerHeight(20.dp)
        TopInput(
            text = stringResource(R.string.agent_payment_method),
            value = agentIngo?.displayReceiveMethod.orEmpty()
        )
        SpacerHeight(20.dp)
        TopInput(
            text = stringResource(R.string.agent_payment_information),
            value = agentIngo?.receiveName.orEmpty()
        )
    }
}


@Composable
private fun TopInput(
    text: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(text = text, fontSize = 16.sp, color = Color(0xff999999))
        SpacerWidth(12.dp)
        Text(text = value, fontSize = 20.sp, color = Color(0xff333333))
    }
}

@Composable
private fun Invitation(viewModel: BasicInformationViewModel) {
    InformationDialog(
        isShow = viewModel.inviteUserResponse !=null,
        userResponse = viewModel.inviteUserResponse,
        onDismissRequest = {viewModel.clearInviteUserResponse()},
        onSubmit = {
            viewModel.agentInvite()
        }
    )
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .height(30.dp)
            .padding(horizontal = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(1.dp, color = Color(0xffe6e6e6), shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp)
        ) {
            BasicTextField(
                textStyle = TextStyle(
                    fontSize = 10.sp,
                    color = Color(0xff333333)
                ),
                value = viewModel.inviteUid,
                onValueChange = viewModel::inviteUid,
                decorationBox = {
                    if (viewModel.inviteUid.isEmpty()) {
                        Text(
                            text = stringResource(R.string.agent_please_input), style = TextStyle(
                                fontSize = 10.sp,
                                color = Color(0xff999999)
                            )
                        )
                    }
                    it()
                }
            )
        }
        SpacerWidth(12.dp)
        Text(
            text = stringResource(R.string.agent_invitations),
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxHeight()
                .background(color = Color(0xff333333), shape = RoundedCornerShape(15.dp))
                .onClick {
                   viewModel.findUserByUid()
                }
                .padding(horizontal = 12.dp)
                .wrapContentSize()
        )
    }
}

@Composable
private fun Bottom(viewModel: BasicInformationViewModel) {
    val pagingData = viewModel.pagingData
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .padding(horizontal = 15.dp)
    ) {
        Title()
        AppPagingBox(
            pagingData = pagingData, modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(pagingData = pagingData, key = {
                    it.id
                }) { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .background(
                                color = Color.White,
                                shape = if (index == pagingData.lastIndex) RoundedCornerShape(
                                    bottomEnd = 8.dp,
                                    bottomStart = 8.dp
                                ) else RectangleShape
                            )
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = item.uid?.toString().orEmpty(),
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .wrapContentSize()
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = item.nickname.orEmpty(),
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .wrapContentSize()
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = item.createTime.orEmpty(),
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .wrapContentSize()
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = item.statusText.orEmpty(),
                            fontSize = 12.sp,
                            color = when (item.status) {
                                1 -> Color(0xffFF8669) // 待同意
                                2 -> Color(0xff0BF936) //同意
                                3 -> Color(0xffF90B0F) //拒绝
                                else -> Color.Black
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Title() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(
                color = Color(0xfff0f0f0),
                shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
            )
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.agent_uid),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.agent_name),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.agent_time),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.agent_status),
            fontSize = 14.sp,
            color = Color(0xff666666),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .wrapContentSize()
        )
    }
}