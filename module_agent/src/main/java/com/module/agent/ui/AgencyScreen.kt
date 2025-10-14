package com.module.agent.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

fun NavGraphBuilder.agentScreen() = composable(route = AppRoutes.Agent.static) {
    AgentScreen()
}

/**
 * 代理界面
 */
@Composable
internal fun AgentScreen() {
    Scaffold(topBar = {
        AppTitleBar(
            text = stringResource(R.string.agent_agency)
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(R.drawable.agent_bg),
                    contentScale = ContentScale.FillBounds
                )
                .padding(innerPadding)
        ) {
            var index by rememberSaveable {
                mutableIntStateOf(0)
            }
            Tabs(selectedIndex = index) {
                index = it
            }
            val stateHolder = rememberSaveableStateHolder()
            when (index) {
                0 -> stateHolder.SaveableStateProvider("BasicInformation") {
                    BasicInformation()
                }

                1 -> stateHolder.SaveableStateProvider("IdolList") {
                    IdolList()
                }

                2 -> stateHolder.SaveableStateProvider("Bill") {
                    Bill()
                }
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
        stringArrayResource(R.array.agent_tabs).forEachIndexed { index, text ->
            Text(
                text = text,
                fontSize = 16.sp,
                color = if (index == selectedIndex) Color.White else Color(0xff666666),
                modifier = Modifier
                    .onClick {
                        onIndexChange(index)
                    }
                    .then(
                        if (index == selectedIndex) Modifier.appBrushBackground(shape = RoundedCornerShape(15.dp)
                        ) else Modifier
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .wrapContentSize()
            )
        }
    }
}