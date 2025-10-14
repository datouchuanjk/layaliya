package com.module.basic.ui

import android.Manifest
import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.Gravity
import androidx.activity.compose.*
import androidx.activity.result.*
import androidx.activity.result.contract.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.helper.develop.util.*
import com.module.basic.constant.*
import com.module.basic.util.*
import com.module.basic.viewmodel.BaseViewModel
import com.module.basic.viewmodel.apiHandlerViewModel
import kotlinx.coroutines.*
import org.koin.androidx.compose.*
import java.io.*
import kotlin.random.*


@Composable
fun AppBottomPickVisualSelected(
    isShow: Boolean,
    onResult: (List<File>) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val localOwner = LocalLifecycleOwner.current as NavBackStackEntry
    val scope = rememberCoroutineScope()
    val localContext = LocalContext.current
    val pickVisualMedia = rememberLauncherForActivityResult(
        CustomActivityResultContracts.PickVisualMedia(1)
    ) {
        if (it.isNotEmpty()) {
            scope.launch {
                val files = withContext(Dispatchers.IO) {
                    it.map { uri ->
                        uri.copyAsFile(
                            localContext,
                            "${System.currentTimeMillis()}${Random.nextInt(100)}.JPEG"
                        )
                    }
                }
                onResult(files)
            }
        }
    }
    val takePicture = rememberLauncherForActivityResult(
        CustomActivityResultContracts.TakePicture(
            AppConstant.AUTHORITY,
            localOwner.savedStateHandle
        )
    ) {
        if (it != null) {
            onResult(listOf(it))
        }
    }
    val requestExternalStorageAccessPermission = rememberLauncherForActivityResult(
        CustomActivityResultContracts.RequestExternalStorageAccessPermission()
    ) {
        if (it) {
            pickVisualMedia.launchImageOnly()
        }
    }

    val requestTakePicturePermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            takePicture.launch(
                File(
                    localContext.cacheDir,
                    "${System.currentTimeMillis()}${Random.nextInt(100)}.JPEG"
                )
            )
        }
    }
    if (isShow) {
        AppDialog(
            layoutParamsSetting = {
                it.gravity = Gravity.BOTTOM
            },
            usePlatformDefaultWidth = false,
            onDismissRequest = onDismissRequest,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
                    .padding(bottom = 15.dp)
            ) {
                Text(
                    text = "Take  photo",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onClick {
                            requestTakePicturePermission.launch(Manifest.permission.CAMERA)
                            onDismissRequest()
                        }
                        .wrapContentWidth()
                        .padding(vertical = 15.dp)
                )
                Text(
                    text = "Gallery",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onClick {
                            requestExternalStorageAccessPermission.launch()
                            onDismissRequest()
                        }
                        .wrapContentWidth()
                        .padding(vertical = 15.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(color = Color(0xfff5f5f5))
                )
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onClick {
                            onDismissRequest()
                        }
                        .wrapContentWidth()
                        .padding(vertical = 15.dp)
                )
            }
        }
    }
}