package com.helper.develop.util

import java.io.File
import java.net.HttpURLConnection
import java.net.URL

fun String.downloadToFile(outputFile: File) {
    try {
        val httpURLConnection = URL(this).openConnection() as HttpURLConnection
        httpURLConnection.inputStream.use { input ->
            outputFile.outputStream()
                .use { output ->
                    input.copyTo(output)
                }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}