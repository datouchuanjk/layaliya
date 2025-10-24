package com.module.bag.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helper.develop.util.toast
import com.module.bag.api.data.response.*
import com.module.bag.viewmodel.BagViewModel
import com.module.bag.*
import com.module.basic.ui.AppImage
import com.module.basic.util.onClick

@Composable
internal fun BagItem(viewModel: BagViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xffF5F5F5))
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = viewModel.selectedList,key= {
                it.id
            }) {
                Box(
                    modifier = Modifier
                        .then(
                            if (it.isSelected) Modifier.paint(
                                painter = painterResource(R.drawable.bag_bg_selected_item),
                                contentScale = ContentScale.Crop
                            ) else Modifier.background(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                        )
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                ) {
                    Item(it, viewModel::selectedListItem)
                }
            }
        }
        if(viewModel.selectedListItem!=null){
            Buy(viewModel)
        }
    }
}
@Composable
private fun Buy(viewModel: BagViewModel) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 12.dp)
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    color = Color(0xff333333),
                    shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp)
                )
                .padding(horizontal = 24.dp)
        ) {
            AppImage(R.drawable.bag_ic_coin)
            Text(
                text = viewModel.selectedListItem?.price.orEmpty(),
                fontSize = 24.sp,
                color = Color.White
            )
            Text(text = "/30 days", fontSize = 10.sp, color = Color(0xffcccccc))
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xffFF76BD),
                            Color(0xffFF4070),
                        )
                    ), shape = RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp)
                ).onClick{
                    viewModel.use()
                }
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                text = stringResource(R.string.bag_use),
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun Item(item: BagResponse.Item, onSelected: (BagResponse.Item) -> Unit) {
    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.onClick {
        onSelected(item)
    }) {
        Text(
            text = item.price.orEmpty(),
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier
                .height(18.dp)
                .fillMaxWidth()
                .padding(start = 4.dp)
                .background(
                    color = Color(0xffFBBB49),
                    shape = RoundedCornerShape(10.dp)
                ).padding(start = 20.dp)
                .wrapContentSize()
        )
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.bag_ic_id),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}
