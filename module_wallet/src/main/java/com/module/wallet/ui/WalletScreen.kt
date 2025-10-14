package com.module.wallet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.module.agent.R
import com.module.basic.route.AppRoutes
import com.module.basic.ui.AppTitleBar
import com.module.basic.util.*

fun NavGraphBuilder.walletScreen() = composable(route = AppRoutes.Wallet.static) {
    WalletScreen()
}

@Composable
internal fun WalletScreen() {
    Scaffold(
        topBar = {
            AppTitleBar(
                text = stringResource(R.string.wallet_wallet),
                showLine = false
            )
        }
    ) { innerPadding->
        var index by rememberSaveable {
            mutableIntStateOf(0)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(R.drawable.wallet_bg),
                    contentScale = ContentScale.FillBounds
                )
                .padding(innerPadding)
        ) {
            Tabs(index) {
                index = it
            }
            val stateHolder = rememberSaveableStateHolder()
            when (index) {
                0 -> stateHolder.SaveableStateProvider("Diamond") { Diamond() }
                1 -> stateHolder.SaveableStateProvider("Coins") { Coins() }
            }
        }
    }
}

@Composable
private fun Tabs(selectedIndex: Int, onIndexChange: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        stringArrayResource(R.array.wallet_tabs).forEachIndexed { index, text ->
            Text(
                text = text,
                fontSize = 16.sp,
                color = if (index == selectedIndex) Color.White else Color(0xff999999),
                modifier = Modifier
                    .width(92.dp)
                    .height(30.dp)
                    .onClick {
                        onIndexChange(index)
                    }
                    .then(
                        if (index == selectedIndex) Modifier.appBrushBackground(
                            shape = RoundedCornerShape(15.dp)
                        ) else Modifier.background(
                            color = Color.White,
                            shape = RoundedCornerShape(15.dp)
                        )
                    )
                    .wrapContentSize()
            )
        }
    }
}


