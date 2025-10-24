package com.module.basic.ui

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import com.module.basic.util.onClick
import org.koin.androidx.compose.get
import java.io.File

@Composable
fun AppImage(
    model: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    onClickable: (() -> Unit)? = null
) {
    val clickableModifier = modifier.then(if (onClickable == null) Modifier else Modifier.onClick {
        onClickable.invoke()
    })
    when (model) {
        is Int -> {
            Image(
                modifier = clickableModifier,
                painter = painterResource(model),
                contentScale = contentScale,
                contentDescription = contentDescription,
            )
        }

        else -> {
            AsyncImage(
                imageLoader = get<ImageLoader>(),
                model = model,
                contentScale = contentScale,
                contentDescription = contentDescription,
                modifier = clickableModifier,
            )
        }
    }
}

@Composable
fun AppFileImage(
    model: File,
    modifier: Modifier = Modifier,
    defaultModel: Int? = null,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
    onClickable: (() -> Unit)? = null
) {
    val clickableModifier = modifier.then(if (onClickable == null) Modifier else Modifier.onClick {
        onClickable.invoke()
    })

    BitmapFactory.decodeFile(model.absolutePath)?.asImageBitmap()?.let {
        Image(
            modifier = clickableModifier,
            painter = BitmapPainter(it),
            contentScale = contentScale,
            contentDescription = contentDescription,
        )
    } ?: defaultModel?.let {
        Image(
            modifier = clickableModifier,
            painter = painterResource(it),
            contentScale = contentScale,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun AppGifImage(
    model: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null,
) {
    AsyncImage(
        imageLoader = get<ImageLoader.Builder>()
            .components {
                add(GifDecoder.Factory())
            }.build(),
        model = model,
        contentScale = contentScale,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

