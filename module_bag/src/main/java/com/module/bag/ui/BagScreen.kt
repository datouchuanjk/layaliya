package com.module.bag.ui

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
import com.module.basic.viewmodel.*
import com.module.bag.viewmodel.BagViewModel
import com.module.bag.R
import com.module.basic.util.onClick

fun NavGraphBuilder.bagScreen() = composable(route = AppRoutes.Bag.static) {
    BagScreen()
}

@Composable
internal fun BagScreen(viewModel: BagViewModel = apiHandlerViewModel()) {
    Scaffold(
        topBar = {
            AppTitleBar(
                modifier = Modifier.background(color = Color.White),
                showLine = false,
                text = stringResource(R.string.bag_store)
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
                tabs = viewModel.tabs.map { it.name.orEmpty() }.toTypedArray(),
                selectedIndex = viewModel.index,
                onIndexChanged = viewModel::index
            )
            val context = LocalContext.current
            LaunchedEffect(viewModel) {
                viewModel.useSuccessfulFlow
                    .collect {
                        context.toast(R.string.bag_use_successful)
                    }
            }
            val stateHolder = rememberSaveableStateHolder()
            viewModel.tabs.forEach {
                if(it.index in arrayOf("Avatar Frame")){
                    stateHolder.SaveableStateProvider(it.name.toString()) { BagItemNew(viewModel) }
                }else{
                    stateHolder.SaveableStateProvider(it.name.toString()) { BagItem(viewModel) }
                }
            }
                BagItem(viewModel)

        }
    }
}


