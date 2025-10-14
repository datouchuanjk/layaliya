package com.module.store.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.helper.develop.util.toast
import com.module.basic.route.AppRoutes
import com.module.basic.ui.*
import com.module.basic.util.onClick
import com.module.basic.viewmodel.*
import com.module.store.R
import com.module.store.viewmodel.*

fun NavGraphBuilder.storeScreen() = composable(route = AppRoutes.Store.static) {
    StoreScreen()
}

@Composable
internal fun StoreScreen(viewModel: StoreViewModel = apiHandlerViewModel()) {
    Scaffold(
        topBar = {
            AppTitleBar(
                modifier = Modifier.background(color = Color.White),
                showLine = false,
                text = stringResource(R.string.store_store)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AppTabRow(
                contentPadding = PaddingValues(horizontal = 15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                tabs = viewModel.tabs.map { tab -> tab.name.orEmpty() }.toTypedArray(),
                selectedIndex = viewModel.index,
                onIndexChanged = viewModel::index
            )
            val context = LocalContext.current
            LaunchedEffect(viewModel) {
                viewModel.buyOrSendSuccessfulFlow
                    .collect {
                        context.toast(R.string.store_buy_successful)
                    }
            }
            val stateHolder = rememberSaveableStateHolder()
            viewModel.tabs.forEach {
                if(it.index in arrayOf("Avatar Frame")){
                    stateHolder.SaveableStateProvider(it.name.toString()) { StoreItemNew(viewModel) }
                }else{
                    stateHolder.SaveableStateProvider(it.name.toString()) { StoreItem(viewModel) }
                }

            }
        }
    }
}

