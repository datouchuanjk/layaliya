package com.module.noble.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.helper.develop.Background
import com.module.basic.ui.AppDialog
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.util.onClick
import com.module.basic.util.todoImageUrl
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.noble.R
import com.module.noble.viewmodel.GiveToWhoViewModel

@Composable
internal fun GiveToWhoDialog(
    isShow: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val viewModel: GiveToWhoViewModel = apiHandlerViewModel()
    if(isShow){
    AppDialog(
        onDismissRequest = onDismissRequest,
        usePlatformDefaultWidth = false,
        layoutParamsSetting = {
            it.dimAmount = 0f
        }
    ) {
        Background(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            painter = painterResource(R.drawable.noble_bg_dialog_find_user)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.noble_uid),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    SpacerWidth(5.dp)
                    BasicTextField(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(0.2f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp),
                        value = viewModel.searchUid,
                        onValueChange = viewModel::searchUid,
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            color = Color.White
                        ), decorationBox = {
                            if (viewModel.searchUid.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.noble_please_enter),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        color = Color(0xffcccccc)
                                    )
                                )
                            }
                            it()
                        }
                    )
                    SpacerWidth(27.dp)
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(70.dp)
                            .background(
                                color = Color(0xff212020),
                                shape = RoundedCornerShape(15.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.isSearching) {
                            CircularProgressIndicator(
                                strokeWidth = 1.dp,
                                color = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.noble_search),
                                fontSize = 14.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .onClick {
                                        viewModel.findUserByUid()
                                    }
                            )
                        }
                    }
                }
                SpacerHeight(12.dp)
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(
                            if (viewModel.showUser) 1f else 0f
                        )
                ) {
                    val (icon, name, cancel, confirm) = createRefs()
                    AppImage(
                        model = todoImageUrl(),
                        modifier = Modifier
                            .constrainAs(icon) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = stringResource(R.string.noble_name),
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.constrainAs(name) {
                            top.linkTo(icon.top)
                            start.linkTo(icon.end, 8.dp)
                            end.linkTo(parent.end, 8.dp)
                            width = Dimension.fillToConstraints
                        })
                    Text(
                        text = stringResource(R.string.noble_cancel),
                        fontSize = 16.sp,
                        color = Color(0xffcccccc),
                        modifier = Modifier
                            .constrainAs(cancel) {
                                top.linkTo(name.bottom, 12.dp)
                                start.linkTo(name.start)
                                end.linkTo(confirm.start)
                                height = Dimension.value(32.dp)
                                width = Dimension.fillToConstraints
                            }
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .onClick(enabled = viewModel.showUser) {
                                onDismissRequest()
                            }
                            .wrapContentSize()
                    )
                    Text(
                        text = stringResource(R.string.noble_confirm),
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .constrainAs(confirm) {
                                top.linkTo(cancel.top)
                                bottom.linkTo(cancel.bottom)
                                start.linkTo(cancel.end, 9.dp)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            }
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xffFF6CAF),
                                        Color(0xffFF477A),
                                    )
                                ), shape = RoundedCornerShape(16.dp)
                            )
                            .onClick(enabled = viewModel.showUser) {
                                onConfirm(
                                    viewModel.findUserResponse?.id.orEmpty()
                                )
                            }
                            .wrapContentSize()
                    )
                }
            }
        }
    }
    }
}