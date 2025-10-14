package com.module.basic.ui

import android.util.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.viewinterop.*
import com.opensource.svgaplayer.*
import java.io.File

@Composable
fun AppSVGA(
    modifier: Modifier = Modifier,
    file: File,
    loops:Int =0,
    onFinish: () -> Unit,
) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                SVGAImageView(context).apply {
                    this.loops = loops
                    callback = object : SVGACallback {
                        override fun onFinished() {
                            onFinish()
                        }

                        override fun onPause() {
                        }

                        override fun onRepeat() {
                        }

                        override fun onStep(frame: Int, percentage: Double) {
                        }
                    }
                    val parser = SVGAParser(context)
                    val inputStream = file.inputStream()
                    parser.decodeFromInputStream(
                        inputStream,
                        file.absolutePath,
                        object : SVGAParser.ParseCompletion {
                            override fun onComplete(videoItem: SVGAVideoEntity) {
                                setVideoItem(videoItem)
                                startAnimation()
                            }

                            override fun onError() {
                                Log.e("SVGA", "播放失败")
                            }
                        })
                }
                }
        )
    }
