package com.module.basic.ui.paging

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.develop.paging.LoadState
import com.module.basic.R
import com.module.basic.util.onClick

@Composable
internal fun LazyItemScope.DefaultLoadingCompose() {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .wrapContentSize()
    ) {
        CircularProgressIndicator(modifier = Modifier.size(15.dp), strokeWidth = 3.dp, color = Color(0xff333333))
    }
}

@Composable
internal fun LazyItemScope.DefaultNotLoadingCompose(loadState: LoadState) {
    if (loadState.endOfPaginationReached) {
        Row(
            modifier = Modifier
                .fillParentMaxWidth()
                .wrapContentSize()
        ) {
            Text(text = "no more", fontSize = 12.sp, color = Color(0xff333333))
        }
    }
}

@Composable
internal fun LazyItemScope.DefaultLoadErrorCompose(retry: () -> Unit) {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .wrapContentSize()
    ) {
        Icon(
            painter = painterResource(R.drawable.paging_ic_refresh),
            contentDescription = null,
            modifier = Modifier.onClick {
                retry()
            })
    }
}

@Composable
internal fun LazyItemScope.DefaultEmptyCompose() {
    Column(
        modifier = Modifier.fillParentMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.paging_ic_empty), contentDescription = null)
    }
}

@Composable
internal fun DefaultSkeletonCompose() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(strokeWidth = 3.dp, color = Color(0xff333333))
    }
}

@Composable
internal fun DefaultErrorCompose(refresh: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.paging_ic_error), contentDescription = null,
            modifier = Modifier.onClick {
                refresh()
            })
    }
}