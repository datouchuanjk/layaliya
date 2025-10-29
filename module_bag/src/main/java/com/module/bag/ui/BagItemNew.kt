package com.module.bag.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.bag.R
import com.module.bag.api.data.response.BagResponse
import com.module.bag.viewmodel.BagViewModel
import com.module.basic.ui.AppImage
import com.module.basic.ui.SpacerHeight
import com.module.basic.util.appBrushBackground
import com.module.basic.util.onClick

@Composable
internal fun BagItemNew(viewModel: BagViewModel) {

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xffF5F5F5)),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(15.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = viewModel.selectedList, key = {
            it.id
        }) {
            Item(it,viewModel)
        }
    }
}


@Composable
private fun Item(item: BagResponse.Item,viewModel: BagViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xffFFD6F0),
                        Color.White,
                    ),
                ), shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 13.dp)) {
        AppImage(
            model = item.pic,
            modifier = Modifier.size(120.dp)
        )
        SpacerHeight(12.dp)
        Text(
            text = item.name.toString(),
            fontSize = 16.sp,
            color = Color(0xff333333),
            fontWeight = FontWeight.Bold
        )
        SpacerHeight(12.dp)
        Text(
            text = "Valid for ${item.expireTime} days",
            fontSize = 10.sp,
            color = Color(0xffcccccc),
        )
        SpacerHeight(10.dp)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .appBrushBackground(shape = RoundedCornerShape(12.dp))
                .padding(vertical = 7.dp)
                .onClick{
                    viewModel.use(item.id.toString(),item.use.orEmpty())
                }
                .wrapContentWidth(),
            text = stringResource(R.string.bag_use_now),
            fontSize = 14.sp,
            color = Color.White
        )
    }
}
