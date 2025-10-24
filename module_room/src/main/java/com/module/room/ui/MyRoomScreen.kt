package com.module.room.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppSwitch
import com.module.basic.ui.AppTabRow
import com.module.basic.ui.paging.AppPagingRefresh
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerWidth
import com.module.basic.ui.paging.itemsIndexed
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.room.R
import com.module.room.viewmodel.MyRoomViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.myRoomScreen() = composable(route = AppRoutes.MyRoom.static) {
    MyRoomScreen()
}

@Composable
internal fun MyRoomScreen() {
    var index by rememberSaveable {
        mutableIntStateOf(0)
    }
    Scaffold(
        topBar = {
            AppTabRow(
                contentPadding = PaddingValues(horizontal = 15.dp),
                withBackIcon = true,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 12.dp),
                        tabs = stringArrayResource (R.array.room_my_tabs),
                selectedIndex = index,
                onIndexChanged = {
                    index = it
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val stateHolder = rememberSaveableStateHolder()
            when (index) {
                0 -> stateHolder.SaveableStateProvider("my") {
                    Item(
                        viewModel = apiHandlerViewModel(
                            key = "my",
                            parameters = {
                                parametersOf(0)
                            }
                        )
                    )
                }

                1 -> stateHolder.SaveableStateProvider("manage") {
                    Item(
                        viewModel = apiHandlerViewModel(
                            key = "manage",
                            parameters = {
                                parametersOf(1)
                            }
                        )
                    )
                }

                2 -> stateHolder.SaveableStateProvider("follow") {
                    Item(
                        viewModel = apiHandlerViewModel(
                            key = "follow",
                            parameters = {
                                parametersOf(2)
                            }
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Item(
    viewModel: MyRoomViewModel = apiHandlerViewModel()
) {
    AppPagingRefresh(
        modifier = Modifier
            .fillMaxSize(),
        pagingData = viewModel.pagingData
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(pagingData = viewModel.pagingData, key = { it.uid }) { _, item ->
                ConstraintLayout(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    val (image, name, city, uid, home, action) = createRefs()
                    AppImage(
                        model = item.cover,
                        modifier = Modifier
                            .size(60.dp)
                            .constrainAs(image) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                    )
                    Text(
                        text = item.name.orEmpty(),
                        color = Color(0xff333333),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.constrainAs(name) {
                            start.linkTo(image.end, margin = 12.dp)
                            top.linkTo(image.top)
                        })
//                    Text(
//                        text = "这个是么逼？",
//                        color = Color(0xffcccccc),
//                        fontSize = 13.sp,
//                        modifier = Modifier.constrainAs(city) {
//                            start.linkTo(name.start)
//                            top.linkTo(name.bottom, margin = 4.dp)
//                        })
                    Text(
                        text = "UID:${item.uid}",
                        color = Color(0xff999999),
                        fontSize = 13.sp,
                        modifier = Modifier.constrainAs(uid) {
                            start.linkTo(name.start)
                            top.linkTo(name.bottom, margin = 4.dp)
                        })
                    Image(
                        painter = painterResource(R.drawable.room_ic_home),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.constrainAs(home) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                    )
                    if (viewModel.isShowSwitch) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.constrainAs(action) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(home.start, margin = 13.dp)
                            }) {
                            Text(
                                text = if (item.isOpen == 1)
                                    stringResource(R.string.room_hide) else
                                    stringResource(R.string.room_show),
                                fontSize = 16.sp,
                                color = Color(0xff333333),
                                fontWeight = FontWeight.Bold
                            )
                            SpacerWidth(6.dp)
                            AppSwitch(
                                checked = item.isOpen == 1,
                            ) {
                                viewModel.showOrHideRoom(item)
                            }
                        }
                    }
                }
            }
        }
    }
}