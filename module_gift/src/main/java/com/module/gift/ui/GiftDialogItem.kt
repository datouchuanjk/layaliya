package com.module.gift.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.basic.sp.AppGlobal
import com.module.basic.ui.*
import com.module.basic.util.onClick
import com.module.gift.R
import com.module.gift.viewmodel.*
import java.io.File

@Composable
internal fun ColumnScope.GiftDialogItem(viewModel: GiftViewModel) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(horizontal = 15.dp)
            ) {
                items(items = viewModel.selectedList, key = { it.id }) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(78f / 98)
                            .background(
                                color = if (it.isSelected) Color(0xff474747) else Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .onClick {
                                viewModel.selectedListItem(it)
                            }
                    ) {
                        AppFileImage(
                            modifier = Modifier.size(48.dp),
                            model = AppGlobal.getGiftImageFileById(it.id.toString()),
                            defaultModel = R.drawable.gift_ic_default,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                        )
                        SpacerHeight(2.dp)
                        Text(it.name.orEmpty(), fontSize = 10.sp, color = Color.White)
                        SpacerHeight(2.dp)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(it.price.orEmpty(), fontSize = 8.sp, color = Color(0xffFF4070))
                            SpacerWidth(2.dp)
                            Image(
                                painter = painterResource(R.drawable.gift_ic_coin),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                            )
                        }
                    }
                }
        }
}