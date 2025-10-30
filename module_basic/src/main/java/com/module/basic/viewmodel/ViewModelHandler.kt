package com.module.basic.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import com.helper.develop.nav.LocalNavController
import com.helper.develop.nav.popToOrNavigate
import com.module.basic.api.ApiException
import com.module.basic.route.AppRoutes
import com.module.basic.sp.AppGlobal
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

/**
 * 处理viewmodel的信息 比如说菊花 比如说错误信息
 */
@Composable
inline fun <reified T : BaseViewModel> apiHandlerViewModel(
    qualifier: Qualifier? = null,
    key: String? = null,
    noinline parameters: ParametersDefinition? = null,
) = koinViewModel<T>(
    parameters = parameters,
    qualifier = qualifier,
    key = key
).withApiHandler()

@PublishedApi
@Composable
internal fun <T : BaseViewModel> T.withApiHandler(): T =
    apply {
        val localNav = LocalNavController.current
        if (loading) {
            Dialog(onDismissRequest = {}) {
                val window = (LocalView.current.parent as DialogWindowProvider).window
                val attributes = window.attributes
                attributes.dimAmount = 0f
                window.attributes = attributes
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Black.copy(0.7f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(15.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp),
                        color = Color.White,
                        strokeWidth = 1.dp
                    )
                }
            }
        }
        val context: Context = LocalContext.current
        LaunchedEffect(this) {
            errorFlow.collect {
                if (it is ApiException && it.code in arrayOf(1,2)) {
                    AppGlobal.exit()
                } else {
                    if (it.message.isNullOrEmpty()) {
                        return@collect
                    }
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }