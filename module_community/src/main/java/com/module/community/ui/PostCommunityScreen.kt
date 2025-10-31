package com.module.community.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.setResult
import com.helper.develop.util.toast
import com.module.basic.route.AppRoutes
import com.module.basic.ui.*
import com.module.basic.util.*
import com.module.basic.viewmodel.apiHandlerViewModel
import com.module.community.R
import com.module.community.viewmodel.PostCommunityViewModel

fun NavGraphBuilder.postCommunityScreen() = composable(route = AppRoutes.PostCommunity.static) {
    PostCommunityScreen()
}

@Composable
internal fun PostCommunityScreen(viewModel: PostCommunityViewModel = apiHandlerViewModel()) {
    val context = LocalContext.current
    val localNav = LocalNavController.current
    LaunchedEffect(Unit) {
        viewModel.postCommunitySuccessfulFlow
            .collect {
                context.toast(R.string.community_post_successful)
                localNav.setResult(true)
                localNav.popBackStack()
            }
    }
    Scaffold(
        containerColor = Color(0xfff5f5f5),
        topBar = {
            AppTitleBar(
                showLine = false,
                text = stringResource(R.string.community_post_update),
            ) {
                Text(
                    text = stringResource(R.string.community_post),
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .appBrushBackground(
                            shape = RoundedCornerShape(20.dp)
                        )
                        .onClick {
                            viewModel.post()
                        }
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                )
            }
        }
    ) {
        Column {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(15.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp, horizontal = 12.dp)
            ) {
                BasicTextField(
                    value = viewModel.input,
                    onValueChange = viewModel::input,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        color = Color(0xff333333)

                    ), decorationBox = {
                        if (viewModel.input.isEmpty()) {
                            Text(
                                text = stringResource(R.string.community_share_your_good_mood),
                                fontSize = 20.sp,
                                color = Color(0xff999999)
                            )
                        }
                        it()
                    }
                )
                SpacerHeight(24.dp)
                var isShow by remember {
                    mutableStateOf(false)
                }
                AppBottomPickVisualSelected(
                    isShow = isShow,
                    onDismissRequest = {
                        isShow = false
                    },
                    onResult = {
                        viewModel.addImage(it)
                    }
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = viewModel.images) { item ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(120f / 140)
                        ) {
                            AppImage(
                                item,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            AppImage(
                                model = R.drawable.community_ic_delete,
                                modifier = Modifier
                                    .align(alignment = Alignment.TopEnd)
                                    .padding(top = 5.dp, end = 5.dp)
                                    .size(20.dp)
                            ) {
                                viewModel.deleteImage(item)
                            }
                        }
                    }
                    if (viewModel.isShowAddImage) {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(120f / 140)
                                    .background(
                                        color = Color(0xfff0f0f0),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                AppIcon(
                                    tint = Color(0xffcccccc),
                                    res = R.drawable.community_ic_add_pic,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    isShow = true
                                }

                            }
                        }
                    }
                }
                SpacerHeight(8.dp)
            }
            SpacerHeight(24.dp)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                text = buildAnnotatedString {
                    append("1.It is forbidden to publish fraudulent advertisements and vulgar content.")
                    appendLine()
                    append("2.Personal attacks, insults, and hate speech are prohibited.")
                    appendLine()
                    append("3.Once any illegal content is found, it will be dealt with immediately, and serious cases will be blocked.")
                },
                fontSize = 14.sp,
                color = Color(0xff666666)
            )
        }
    }
}