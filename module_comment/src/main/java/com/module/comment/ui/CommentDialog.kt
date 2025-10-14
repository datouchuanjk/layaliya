package com.module.comment.ui

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.setResult
import com.helper.develop.util.toast
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppTitleBar
import com.module.basic.ui.paging.AppPagingBox
import com.module.basic.ui.AppIcon
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.UpdateDialogWindow
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.comment.R
import com.module.comment.viewmodel.CommentViewModel

fun NavGraphBuilder.commentDialog() = dialog(
    route = AppRoutes.Comment.static, dialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    )
) {
    UpdateDialogWindow {
        it.gravity = Gravity.BOTTOM
    }
    CommentDialog()
}

@Composable
internal  fun CommentDialog(viewModel: CommentViewModel = apiHandlerViewModel()) {
    val localNav = LocalNavController.current
    val pagingData = viewModel.pagingData
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.postCommentSuccessfulFlow.collect {
            context.toast(R.string.comment_post_successful)
            localNav.setResult(it)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
            )
            .padding(vertical = 12.dp, horizontal = 15.dp)
    ) {
        val focusRequester = remember {
            FocusRequester()
        }
        AppTitleBar(
            showLine = false,
            text = stringResource(R.string.comment_comments)
        ) {
            AppImage(R.drawable.comment_ic_comment) {
                viewModel.commentId(null)
                focusRequester.requestFocus()
            }
        }
        SpacerHeight(13.dp)
        val focusManager = LocalFocusManager.current
        AppPagingBox(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            pagingData = pagingData
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onPress = {
                            focusManager.clearFocus()
                        })
                    },
                contentPadding = PaddingValues(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                itemsIndexed(pagingData = pagingData, key = {
                    it.id
                }) { index, item ->
                    ConstraintLayout(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .onClick {
                                viewModel.commentId(item)
                                focusRequester.requestFocus()
                            }
                    ) {
                        val (icon, name, content, time, action) = createRefs()
                        AppImage(
                            item.avatar, modifier = Modifier
                                .constrainAs(icon) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }
                                .size(48.dp)
                                .clip(CircleShape))
                        Text(
                            item.nickname.orEmpty(),
                            fontSize = 16.sp,
                            color = Color(0xff999999),
                            modifier = Modifier.constrainAs(name) {
                                top.linkTo(icon.top)
                                start.linkTo(icon.end, 12.dp)
                            })
                        Text(
                            text = item.displayContent,
                            fontSize = 14.sp,
                            color = Color(0xff333333),
                            modifier = Modifier.constrainAs(content) {
                                top.linkTo(name.bottom, 4.dp)
                                start.linkTo(name.start)
                            })
                        Text(
                            item.createTime.orEmpty(),
                            fontSize = 12.sp,
                            color = Color(0xffcccccc),
                            modifier = Modifier.constrainAs(time) {
                                top.linkTo(content.bottom, 6.dp)
                                start.linkTo(content.start)
                            })
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .constrainAs(action) {
                                    start.linkTo(time.end)
                                    top.linkTo(time.top)
                                    bottom.linkTo(time.bottom)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                }
                                .wrapContentWidth(align = Alignment.End)
                        ) {
                            AppIcon(
                                tint = if (item.imPraise == 1) Color.Red else null,
                                res = R.drawable.comment_ic_like,
                            ) {
                                viewModel.like(index, item)
                            }
                            SpacerWidth(4.dp)
                            Text(
                                text = item.praiseNum.toString(),
                                fontSize = 12.sp,
                                color = Color(0xff333333)
                            )
                        }
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .focusRequester(focusRequester)
                .border(1.dp, Color(0xff333333), RoundedCornerShape(4.dp))
                .padding(horizontal = 15.dp)
        ) {
            if (viewModel.commentResponse != null) {
                Text(
                    text = "${stringResource(R.string.comment_reply)}:${viewModel.commentResponse?.nickname.orEmpty()}",
                    fontSize = 12.sp,
                    color = Color(0xff333333)
                )
                SpacerWidth(4.dp)
            }
            BasicTextField(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .wrapContentHeight(),
                textStyle = TextStyle(
                    fontSize = 12.sp,
                    color = Color(0xff333333)
                ),
                value = viewModel.input,
                onValueChange = viewModel::input,
                decorationBox = {
                    if (viewModel.input.isEmpty()) {
                        Text(
                            text = stringResource(R.string.comment_please_input_comment),
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xff333333)
                            ),
                        )
                    }
                    it()
                }, maxLines = 1, keyboardActions = KeyboardActions(
                    onSend = {
                        viewModel.postComment()
                    },
                )
            )
            if (viewModel.isCommenting) {
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    color = Color(0xff333333),
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(10.dp)
                )
            } else {
                Text(stringResource(R.string.comment_send), modifier = Modifier.onClick {
                    viewModel.postComment()
                })
            }
        }
    }
}

