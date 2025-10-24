package com.module.agent.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.module.agent.R
import com.module.basic.api.data.response.SearchUserResponse
import com.module.basic.api.data.response.UserResponse
import com.module.basic.ui.AppDialog
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerWidth
import com.module.basic.util.onClick

@Composable
internal fun InformationDialog(
    isShow: Boolean,
    userResponse: SearchUserResponse?,
    onDismissRequest: () -> Unit,
    onSubmit: () -> Unit,
) {
    if (isShow && userResponse != null) {
        AppDialog(
            usePlatformDefaultWidth = false,
            onDismissRequest = onDismissRequest
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                val (icon, name, info, button) = createRefs()
                AppImage(
                    model = userResponse.avatar, modifier = Modifier
                        .constrainAs(icon) {
                            top.linkTo(parent.top, 12.dp)
                            start.linkTo(parent.start)
                            width = Dimension.value(48.dp)
                            height = Dimension.value(48.dp)
                        }
                        .clip(CircleShape)
                )
                Text(
                    fontWeight = FontWeight.Black,
                    text = userResponse.nickname.toString(),
                    fontSize = 20.sp,
                    color = Color(0xff333333),
                    modifier = Modifier.constrainAs(name) {
                        top.linkTo(icon.top)
                        start.linkTo(icon.end, 12.dp)
                    })
                Row(modifier = Modifier.constrainAs(info) {
                    top.linkTo(name.bottom, 2.dp)
                    start.linkTo(name.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }) {
                    Text("UID:${userResponse.uuid}", fontSize = 14.sp, color = Color(0xff999999))
                }
                Row(
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(icon.bottom, 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.agent_cancel),
                        fontSize = 20.sp,
                        color = Color(0xffcccccc),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .border(
                                1.dp,
                                color = Color(0xffcccccc),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .onClick {
                                onDismissRequest()
                            }
                            .wrapContentSize()
                    )
                    SpacerWidth(13.dp)
                    Text(
                        text = stringResource(R.string.agent_confirm),
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color = Color(0xffFF87AB),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .onClick {
                                onSubmit()
                            }
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}