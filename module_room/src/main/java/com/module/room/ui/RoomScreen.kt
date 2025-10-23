package com.module.room.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.helper.develop.nav.LocalNavController
import com.module.basic.provider.GameScreenProvider
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppTabRow
import com.module.basic.ui.paging.AppPagingRefresh
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.util.onClick
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.basic.util.todoImageUrl
import com.module.room.R
import com.module.room.viewmodel.RoomViewModel
import org.koin.androidx.compose.get
import org.koin.core.parameter.*

@Composable
fun Home1() {
    RoomScreen()
}

@Composable
internal fun RoomScreen() {
    var index by rememberSaveable {
        mutableIntStateOf(1)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xffF5F5F5))
            .statusBarsPadding()
    ) {
        AppTabRow(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 15.dp),
            tabs = stringArrayResource(R.array.room_tabs),
            selectedIndex = index
        ) {
            index = it
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val stateHolder = rememberSaveableStateHolder()
            when (index) {
                0 -> stateHolder.SaveableStateProvider("follow") {
                    Item(
                        viewModel = apiHandlerViewModel(key = "follow", parameters = {
                            parametersOf(0)
                        }),
                    )
                }

                1 -> stateHolder.SaveableStateProvider("party") {
                    Item(
                        viewModel = apiHandlerViewModel(key = "party", parameters = {
                            parametersOf(1)
                        }),
                    )
                }

                2 -> stateHolder.SaveableStateProvider("game") {
                    get<GameScreenProvider>().GameScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Item(
    viewModel: RoomViewModel,
) {
    val localNav = LocalNavController.current
    AppPagingRefresh(
        modifier = Modifier
            .fillMaxSize(),
        pagingData = viewModel.followPagingDate
    ) {
        val userList by viewModel.userListFlow.collectAsState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (viewModel.isShowUserList) {
                item {
                    if (userList.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(15.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color(0xffEBEBEB))
                        ) {
                            items(items = userList) {
                                AppImage(
                                    model = it.avatar,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape),
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(84.dp)
                                .background(color = Color(0xffEBEBEB)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Empty", fontSize = 16.sp, color = Color(0xff999999))
                        }
                    }
                }
            }
            itemsIndexed(
                pagingData = viewModel.followPagingDate,
                key = { it.id }) { _, item ->
                ConstraintLayout(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 15.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                        .onClick {
                            localNav.navigate(
                                AppRoutes.ChatroomEnterCheck.dynamic(
                                    "roomId" to item.id.toString()
                                )
                            )
                        }
                        .padding(12.dp)
                ) {
                    val (image, name, uid, icons, hot) = createRefs()
                    AppImage(
                        model = item.cover,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .constrainAs(image) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                    )
                    Text(
                        text = item.name.orEmpty(),
                        color = Color.Black,
                        fontSize = 20.sp,
                        modifier = Modifier.constrainAs(name) {
                            start.linkTo(image.end, margin = 12.dp)
                            top.linkTo(image.top)
                        })
                    Text(
                        text = "UID:${item.id}",
                        color = Color(0xff999999),
                        fontSize = 12.sp,
                        modifier = Modifier.constrainAs(uid) {
                            start.linkTo(name.start)
                            top.linkTo(name.bottom, margin = 5.dp)
                        })
                    Box(
                        modifier = Modifier.constrainAs(icons) {
                            start.linkTo(uid.start)
                            end.linkTo(hot.start)
                            width = Dimension.fillToConstraints
                            top.linkTo(uid.bottom, margin = 5.dp)
                        }) {
                        item.avatars?.take(5)?.forEachIndexed { index, icon ->
                            AppImage(
                                modifier = Modifier
                                    .offset {
                                        IntOffset(28.dp.roundToPx() / 2 * index, 0)
                                    }
                                    .size(28.dp)
                                    .clip(CircleShape),
                                model = icon
                            )
                        }
                    }
                    Row(modifier = Modifier.constrainAs(hot) {
                        start.linkTo(icons.end)
                        top.linkTo(icons.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(icons.bottom)
                    }, verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.room_ic_hot),
                            contentDescription = null
                        )
                        SpacerWidth(5.dp)
                        Text(
                            text = item.hotVal.toString(),
                            fontSize = 16.sp,
                            color = Color(0xff333333)
                        )
                    }
                }
            }
        }
    }
}

