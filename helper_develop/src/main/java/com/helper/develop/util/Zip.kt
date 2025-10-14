package com.helper.develop.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun File.unzip(rootFile: File) {
    try {
        FileInputStream(this).use { fis ->
            ZipInputStream(fis).use { zis ->
                var entry: ZipEntry?
                while (zis.nextEntry.also { entry = it } != null) {
                    entry?.let { zipEntry ->
                        val outputFile = File(rootFile, zipEntry.name)
                        if (zipEntry.isDirectory) {
                            // 创建目录
                            outputFile.mkdirs()
                        } else {
                            outputFile.parentFile?.mkdirs()
                            // 解压文件
                            FileOutputStream(outputFile).use { fos ->
                                zis.copyTo(fos)
                            }
                        }
                    }
                    zis.closeEntry()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun getZipEntryCount(zipFile: File): Int {
    return try {
        FileInputStream(zipFile).use { fis ->
            ZipInputStream(fis).use { zis ->
                var count = 0
                while (zis.nextEntry != null) {
                    count++
                    zis.closeEntry()
                }
                count
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

fun getZipEntries(zipFile: File): List<String> {
    return try {
        FileInputStream(zipFile).use { fis ->
            ZipInputStream(fis).use { zis ->
                val entries = mutableListOf<String>()
                var entry: ZipEntry?
                while (zis.nextEntry.also { entry = it } != null) {
                    entry?.name?.let { entries.add(it) }
                    zis.closeEntry()
                }
                entries
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}