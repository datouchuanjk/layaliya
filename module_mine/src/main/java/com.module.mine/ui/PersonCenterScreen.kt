package com.module.mine.ui

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.navigateForResult
import com.helper.im.util.toConversationId
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppBackIcon
import com.module.basic.ui.AppIcon
import com.module.basic.ui.AppTabRow
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.util.aspectRatio
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.mine.R
import com.module.mine.viewmodel.PersonCenterViewModel

fun NavGraphBuilder.personCenterScreen() = composable(route = AppRoutes.PersonCenter.static) {
    PersonCenterScreen()
}

/**
 * 个人中心
 */
@Composable
internal fun PersonCenterScreen(viewModel: PersonCenterViewModel = apiHandlerViewModel()) {
    val personResponse = viewModel.personResponse
    val localNav = LocalNavController.current
    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(124.dp + it.calculateTopPadding())
            ) {
                AppImage(
                    contentScale = ContentScale.Crop,
                    model = personResponse?.cover,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black.copy(0.3f))
                )
                val localBack = LocalOnBackPressedDispatcherOwner.current
                AppBackIcon(
                    tint = Color.White,
                    modifier = Modifier
                        .padding(top = it.calculateTopPadding() + 10.dp)
                        .padding(start = 15.dp)
                ) {
                    localBack?.onBackPressedDispatcher?.onBackPressed()
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = it.calculateTopPadding() + 54.dp)
            ) {
                Content(viewModel)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(alignment = Alignment.TopCenter)
                ) {
                    AppImage(
                        model = personResponse?.avatar,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    if (viewModel.isSelf) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .align(alignment = Alignment.BottomEnd)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xffFF76BD),
                                            Color(0xffFF4070),
                                        )
                                    ), shape = CircleShape
                                )
                                .onClick {
                                    localNav.navigateForResult<Boolean>(AppRoutes.PersonEdit.static) { result ->
                                        if (result == true) {
                                            viewModel.getPersonInfo()
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            AppImage(
                                model = R.drawable.mine_ic_edit_info
                            )
                        }
                    }
                }
            }
            if (!viewModel.isSelf) {
                val localNav = LocalNavController.current
                val localContext = LocalContext.current
                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(bottom = 15.dp)
                        .height(46.dp)
                        .width(166.dp)
                        .background(
                            color = Color(0xffFF4070),
                            shape = RoundedCornerShape(23.dp)
                        )
                        .onClick {
                            Toast.makeText(localContext, "缺少云信id 不能调整 ", Toast.LENGTH_SHORT)
                                .show()
                            return@onClick
                            localNav.navigate(
                                AppRoutes.Chat.dynamic("conversationId" to "".toConversationId())
                            )
                        }
                        .wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AppImage(
                        model = R.drawable.mine_ic_chat
                    )
                    SpacerWidth(14.dp)
                    Text(
                        stringResource(R.string.mine_message),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.FansItem(count: Int, name: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .onClick {
                onClick()
            }, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = count.toString(), fontSize = 24.sp, color = Color(0xff333333))
        Text(text = name, fontSize = 12.sp, color = Color(0xff999999))
    }
}

@Composable
private fun RowScope.TopItem(level: Int, url: String? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1f)
            .wrapContentSize()
    ) {
        if (url == null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(color = Color(0xffe6e6e6))
            ) {
                AppImage(
                    model = R.drawable.mine_ic_person_center_no_top,
                    modifier = Modifier.size(30.dp)
                )
            }
        } else {
            Box {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            color = when (level) {
                                1 -> Color(0xffFDE200)
                                2 -> Color(0xff61A7FF)
                                3 -> Color(0xffFFC371)
                                else -> error("")
                            }
                        )
                        .padding(4.dp)
                ) {
                    AppImage(
                        url, modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                AppImage(
                    modifier = Modifier.align(alignment = Alignment.TopEnd),
                    model = when (level) {
                        1 -> R.drawable.mine_ic_top1
                        2 -> R.drawable.mine_ic_top2
                        3 -> R.drawable.mine_ic_top3
                        else -> error("")
                    }
                )
            }
        }
        Text(
            "Top${level}", fontSize = 16.sp, color = when (level) {
                1 -> Color(0xffFFC935)
                2 -> Color(0xff61A7FF)
                3 -> Color(0xffFFC371)
                else -> error("")
            }
        )
    }
}


@Composable
private fun Content(viewModel: PersonCenterViewModel) {
    val pagingData = viewModel.pagingData
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
            )
    ) {
        item { UserInfo(viewModel) }
        itemsIndexed(pagingData = pagingData, key = {
            it.id
        }) { index, item ->
            Column(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = item.createTime.orEmpty(),
                    fontSize = 14.sp,
                    color = Color(0xff999999)
                )
                SpacerHeight(4.dp)
                Text(text = item.content.orEmpty(), fontSize = 16.sp, color = Color(0xff333333))
                SpacerHeight(8.dp)
                Row(modifier = Modifier.fillMaxWidth()) {
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
                SpacerHeight(16.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    val localNav = LocalNavController.current
                    AppIcon(
                        res = R.drawable.mine_ic_person_center_like,
                        tint = if (item.imPraise == 1) Color.Red else null
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
                    AppIcon(res = R.drawable.mine_ic_person_center_message) {
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
            }
        }
    }
}


@Composable
private fun LazyItemScope.UserInfo(viewModel: PersonCenterViewModel) {
    val personResponse = viewModel.personResponse
    ConstraintLayout(
        modifier = Modifier.fillParentMaxWidth()
    ) {
        val (add, name, sex, uid, fans, topLevel, tab) = createRefs()
        if (personResponse != null && !viewModel.isSelf) {
            Text(
                text = if (personResponse.isFollow == 1) "-" else "+",
                modifier = Modifier
                    .constrainAs(add) {
                        top.linkTo(parent.top, 15.dp)
                        end.linkTo(parent.end, 15.dp)
                    }
                    .width(40.dp)
                    .height(30.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xffFF4070),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .onClick {
                        viewModel.updateFollowStatus()
                    }
                    .wrapContentSize(),
                color = Color(0xffFF4071),
                fontSize = 14.sp
            )
        }
        Text(
            text = personResponse?.nickname.orEmpty(),
            fontSize = 24.sp,
            color = Color(0xff333333),
            modifier = Modifier.constrainAs(name) {
                top.linkTo(parent.top, 54.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.wrapContent
            })
        AppImage(model = R.drawable.mine_ic_woman, modifier = Modifier.constrainAs(sex) {
            top.linkTo(name.top)
            bottom.linkTo(name.bottom)
            start.linkTo(name.end, 4.dp)
        })
        Text(
            text = "UID:${personResponse?.uuid.orEmpty()}",
            fontSize = 13.sp,
            color = Color(0xff999999),
            modifier = Modifier.constrainAs(uid) {
                top.linkTo(name.bottom, 4.dp)
                start.linkTo(name.start)
                end.linkTo(name.end)
                width = Dimension.wrapContent
            })
        Row(modifier = Modifier.constrainAs(fans) {
            top.linkTo(uid.bottom, 12.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }, verticalAlignment = Alignment.CenterVertically) {
            val localNav = LocalNavController.current
            FansItem(personResponse?.followNum ?: 0, stringResource(R.string.mine_followers)) {
                localNav.navigate(
                    AppRoutes.FollowersOrFans.dynamic(
                        "type" to 0,
                        "uid" to viewModel.uid
                    )
                )
            }
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(color = Color(0xffe6e6e6))
            )
            FansItem(personResponse?.fansNum ?: 0, stringResource(R.string.mine_fans)) {
                localNav.navigate(
                    AppRoutes.FollowersOrFans.dynamic(
                        "type" to 1,
                        "uid" to viewModel.uid
                    )
                )
            }
        }
        Row(
            modifier = Modifier
                .constrainAs(topLevel) {
                    top.linkTo(fans.bottom, 12.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    height = Dimension.value(117.dp)
                    width = Dimension.fillToConstraints
                }
                .background(
                    color = Color(0xfff5f5f5),
                    shape = RoundedCornerShape(16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically) {
            repeat(3) {
                val item = personResponse?.top3?.getOrNull(it)
                TopItem(it + 1, item)
            }
        }
        AppTabRow(
            selectedTextStyle = TextStyle(
                color = Color(0xff333333),
                fontSize = 20.sp,
            ),
            modifier = Modifier
                .constrainAs(tab) {
                    top.linkTo(topLevel.bottom, 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .wrapContentWidth(),
            selectedIndex = 0,
            onIndexChanged = {},
            tabs = stringArrayResource(R.array.mine_person_center_tabs)
        )
    }
}
