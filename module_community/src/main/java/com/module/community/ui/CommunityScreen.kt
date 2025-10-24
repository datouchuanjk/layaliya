package com.module.community.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.navigateForResult
import com.module.basic.route.AppRoutes
import com.module.basic.ui.*
import com.module.basic.ui.paging.AppPagingRefresh
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.util.*
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.community.viewmodel.CommunityViewModel
import  com.module.community.R
import kotlinx.coroutines.launch

@Composable
fun Home3() {
    CommunityScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommunityScreen(viewModel: CommunityViewModel = apiHandlerViewModel()) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val localNav = LocalNavController.current
        val pagingData = viewModel.pagingData
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.community), style = TextStyle(
                        fontSize = 20.sp,
                        color = Color(0xffFF4070)
                    )
                )
            }, actions = {
                AppImage(
                    R.drawable.community_ic_take_pic,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .size(25.dp)
                ) {
                    localNav.navigateForResult<Boolean>(AppRoutes.PostCommunity.static) {
                        if (it == true) {
                            pagingData.refresh()
                            scope.launch {
                                state.animateScrollToItem(0)
                            }
                        }
                    }
                }
            }
        )
//        AppTitleBar(
//            text = stringResource(R.string.community), textStyle = TextStyle(
//                fontSize = 20.sp,
//                color = Color(0xffFF4070)
//            ), showLine = false, showBackIcon = false
//        ) {
//            AppImage(
//                R.drawable.community_ic_take_pic,
//                modifier = Modifier
//                    .size(25.dp)
//            ) {
//                localNav.navigate(AppRoutes.PostCommunity.static)
//            }
//        }
        AppPagingRefresh(
            pagingData = pagingData,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            LazyColumn(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(pagingData = pagingData, key = {
                    it.id
                }) { index, item ->
                    ConstraintLayout(
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        val (icon, name, userinfo, add, more, content, images, action, line) = createRefs()
                        val localNav = LocalNavController.current
                        AppImage(
                            model = item.avatar, modifier = Modifier
                                .constrainAs(icon) {
                                    top.linkTo(parent.top, 12.dp)
                                    start.linkTo(parent.start, 15.dp)
                                    width = Dimension.value(48.dp)
                                    height = Dimension.value(48.dp)
                                }
                                .clip(CircleShape)) {
                            localNav.navigate(
                                AppRoutes.PersonCenter.dynamic("uid" to item.id.toString())
                            )
                        }
                        Text(
                            text = item.nickname.orEmpty(),
                            fontSize = 20.sp,
                            color = Color(0xff333333),
                            modifier = Modifier.constrainAs(name) {
                                top.linkTo(icon.top)
                                start.linkTo(icon.end, 12.dp)
                            })
//                        Text(
//                            text = "...",
//                            modifier = Modifier
//                                .constrainAs(more) {
//                                    top.linkTo(icon.top)
//                                    end.linkTo(parent.end, 15.dp)
//                                },
//                            color = Color(0xff333333),
//                            fontSize = 14.sp
//                        )

                        Box(
                            modifier = Modifier
                                .constrainAs(add) {
                                    top.linkTo(icon.top)
                                    end.linkTo(parent.end, 12.dp)
                                }
                                .width(32.dp)
                                .height(24.dp)
                                .appBrushBackground(
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .onClick {
                                    viewModel.follow(index, item)
                                }
                                .wrapContentSize()
                        ) {
                            AppImage(
                                if (item.isFollow == 1) R.drawable.community_ic_unfollow else R.drawable.community_ic_follow
                            )
                        }

//                        Text(
//                            text = if (item.isFollow == 1) "+" else "-",
//                            modifier = Modifier
//                                .constrainAs(add) {
//                                    top.linkTo(icon.top)
//                                    end.linkTo(more.start, 12.dp)
//                                }
//                                .border(
//                                    width = 1.dp,
//                                    color = Color(0xffFF4070),
//                                    shape = RoundedCornerShape(20.dp)
//                                )
//                                .onClick {
//                                    viewModel.follow(index, item)
//                                }
//                                .padding(vertical = 5.dp, horizontal = 12.dp),
//                            color = Color(0xffFF4071),
//                            fontSize = 18.sp
//                        )
                        Row(modifier = Modifier.constrainAs(userinfo) {
                            top.linkTo(name.bottom, 0.dp)
                            start.linkTo(name.start)
                        }) {
//                            Text(text = "China", fontSize = 16.sp, color = Color(0xff999999))
                        }
                        Text(
                            text = item.content.orEmpty(),
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier.constrainAs(content) {
                                top.linkTo(userinfo.bottom, 3.dp)
                                start.linkTo(userinfo.start)
                            })
                        Row(modifier = Modifier.constrainAs(images) {
                            top.linkTo(content.bottom, 12.dp)
                            start.linkTo(content.start)
                            end.linkTo(parent.end, 15.dp)
                            width = Dimension.fillToConstraints
                        }, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            item.images?.take(3)?.forEach { image ->
                                AppImage(
                                    model = image, modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(image?.aspectRatio ?: 1f)
                                        .clip(
                                            RoundedCornerShape(20.dp)
                                        )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .constrainAs(action) {
                                    top.linkTo(images.bottom, 24.dp)
                                    end.linkTo(images.end)
                                    width = Dimension.wrapContent
                                },
                            verticalAlignment = Alignment.CenterVertically,
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
                                fontSize = 16.sp,
                                color = Color(0xff333333)
                            )
                            SpacerWidth(24.dp)
                            AppIcon(res = R.drawable.community_ic_comment) {
                                localNav.navigateForResult<Int>(AppRoutes.Comment.dynamic("id" to item.id.toString())) {
                                    if (it != null) {
                                        viewModel.uploadCommentNum(item, it)
                                    }
                                }
                            }
                            SpacerWidth(4.dp)
                            Text(
                                text = item.commentNum.toString(),
                                fontSize = 16.sp,
                                color = Color(0xff333333)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .constrainAs(line) {
                                    top.linkTo(action.bottom, 12.dp)
                                    start.linkTo(images.start)
                                    end.linkTo(images.end)
                                    height = Dimension.value(1.dp)
                                    width = Dimension.fillToConstraints
                                }
                                .background(color = Color(0xffe6e6e6)))
                    }
                }
            }
        }
    }
}