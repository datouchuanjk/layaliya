package com.module.community.ui

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.module.basic.ui.AppIcon
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.paging.AppPagingBox
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.util.appBrushBackground
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.community.R
import com.module.community.viewmodel.CommentViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun Comment(
    id: String,
    focusRequester: FocusRequester,
    viewModel: CommentViewModel = apiHandlerViewModel(parameters = {
        parametersOf(id)
    }),
    onPostComment: () -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel.postCommentSuccessfulFlow.collect {
            onPostComment()
        }
    }
    val pagingData = viewModel.pagingData
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 12.dp)
            .padding(vertical = 0.dp, horizontal = 15.dp)
    ) {

        AppPagingBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f),
            pagingData = pagingData
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
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
                                res = R.drawable.community_ic_like,
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
    }
}
@Composable
internal fun BoxScope.Send(
    focusRequester: FocusRequester,
    viewModel: CommentViewModel = apiHandlerViewModel()){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .align(alignment = Alignment.BottomStart)
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .imePadding()
            .height(60.dp)
            .background(color = Color.White)
            .padding(top = 5.dp, bottom = 15.dp)
            .focusRequester(focusRequester)
            .background(color = Color(0xfff5f5f5), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 15.dp)
    ) {
        if (viewModel.commentResponse != null) {
            Text(
                text = "${stringResource(R.string.community_reply)}:${viewModel.commentResponse?.nickname.orEmpty()}",
                fontSize = 12.sp,
                color = Color(0xff333333)
            )
            SpacerWidth(4.dp)
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .then(if (viewModel.input.isEmpty()) Modifier.onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL) {
                        viewModel.clearComment()
                        true
                    } else
                        false
                } else Modifier)
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
                            color = Color(0xff808080)
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
        val focusManager = LocalFocusManager.current
        if (viewModel.isCommenting) {
            CircularProgressIndicator(
                strokeWidth = 1.dp,
                color = Color(0xff333333),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(10.dp)
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(30.dp)
                    .appBrushBackground(
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 11.dp, vertical = 3.dp)
            ) {
                AppImage(
                    model = R.drawable.communityic_send,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                ){
                    viewModel.postComment()
                    focusManager.clearFocus()
                }
            }
        }
    }
}

