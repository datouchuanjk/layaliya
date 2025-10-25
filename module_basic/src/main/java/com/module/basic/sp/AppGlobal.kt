package com.module.basic.sp

import android.annotation.*
import android.content.*
import android.provider.*
import android.util.*
import androidx.compose.runtime.*
import androidx.core.content.*
import androidx.lifecycle.*
import com.google.android.gms.ads.identifier.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.helper.develop.util.*
import com.module.basic.api.data.response.*
import com.module.basic.api.service.*
import com.module.basic.ui.base.*
import kotlinx.coroutines.*
import java.io.*
import java.util.*

object AppGlobal {

    init {
        getGoogleAdId()
    }

    private var _googleId: String? = null
    fun getGoogleAdId() {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (GoogleApiAvailability.getInstance()
                        .isGooglePlayServicesAvailable(context) != ConnectionResult.SUCCESS
                ) {
                    _googleId = null
                    Log.e("1234","炸了")
                    return@withContext
                }
                _googleId = try {
                    val advertisingIdInfo =
                        AdvertisingIdClient.getAdvertisingIdInfo(BaseApplication.INSTANCE)
                    val gaId: String? = advertisingIdInfo.id
                    Log.e("1234","gaId=${gaId}")
                    // 检查用户是否限制了广告追踪
                    val isLATEnabled: Boolean = advertisingIdInfo.isLimitAdTrackingEnabled
                    if (!isLATEnabled && gaId != "00000000-0000-0000-0000-000000000000") {
                        gaId
                    } else {
                        Log.e("1234","炸了1")
                        null
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("1234","炸了2 ${e.message}")
                    null
                }
            }
        }
    }

    private val _androidId = Settings.Secure.getString(
        BaseApplication.INSTANCE.contentResolver,
        Settings.Secure.ANDROID_ID
    )
    
    private val uuid = UUID.randomUUID().toString()

    val deviceId get() =  _googleId ?: _androidId ?: uuid

    private var _userResponse by mutableStateOf<UserResponse?>(null)
    val userResponse get() = _userResponse
    fun userResponse(value: UserResponse?) {
        _userResponse = value
    }

    private var _configResponse by mutableStateOf<ConfigResponse?>(null)
    val configResponse get() = _configResponse
    fun configResponse(value: ConfigResponse?) {
        _configResponse = value
    }

    fun getLanguageByCode(code: String?): ConfigResponse.Language? {
        return _configResponse?.language?.find { it.code == code }
    }

    fun getCountryByCode(code: String?): ConfigResponse.Country? {
        return _configResponse?.country?.find { it.code == code }
    }

    fun getRoomTypeById(id: Int?): ConfigResponse.RoomType? {
        return _configResponse?.roomType?.find { it.id == id }
    }


    private val context by lazy {
        BaseApplication.INSTANCE
    }

    fun getGiftImageFileById(id: String) =
        File(BaseApplication.INSTANCE.filesDir, "gift/${id}/pic.png")


    fun getGiftSvgFileById(id: String) =
        File(BaseApplication.INSTANCE.filesDir, "gift/${id}/svg.svga")

    fun getEmojiFileById(id: String) =
        File(BaseApplication.INSTANCE.filesDir, "emoji/${id}/gif.gif")

    val emojiIds = mutableListOf<String>()

    fun preloadGift() {
        Log.e("1234", "开始预加载礼物图片 ${configResponse?.giftConfig?.map { it.svg }}")
        val sp = context.getSharedPreferences("gift", Context.MODE_PRIVATE)
        val map =
            sp.getString("gift_cache_table", "")?.fromTypeJson<Map<String, String>>().orEmpty()
                .toMutableMap()
        var needDownloadCount = 0
        var needDeleteCount = 0
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                configResponse?.giftConfig?.forEach { item ->
                    try {
                        val id = item.id
                        val pic = item.pic
                        val svg = item.svg
                        if (!id.isNullOrEmpty()) {
                            val rootFile = File(context.filesDir, "gift/${id}")
                            rootFile.mkdirs()
                            if (!svg.isNullOrEmpty()) {
                                val file = File(rootFile, "svg.svga")
                                if (!file.exists() || file.length() == 0L || map[file.absolutePath] != svg) {
                                    svg.downloadToFile(file)
                                    map[file.absolutePath] = svg
                                    needDownloadCount++
                                }
                            } else {
                                File(rootFile, "svg.SVG").apply {
                                    if (exists()) {
                                        delete()
                                        map.remove(absolutePath)
                                        needDeleteCount++
                                    }
                                }
                            }
                            if (!pic.isNullOrEmpty()) {
                                val file = File(rootFile, "pic.png")
                                if (!file.exists() || file.length() == 0L || map[file.absolutePath] != pic) {
                                    pic.downloadToFile(file)
                                    map[file.absolutePath] = pic
                                    needDownloadCount++
                                }
                            } else {
                                File(rootFile, "pic.PNG").apply {
                                    if (exists()) {
                                        delete()
                                        map.remove(absolutePath)
                                        needDeleteCount++
                                    }
                                }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                Log.e(
                    "1234",
                    "预加载礼物图片完成  下载的图片有 $needDownloadCount 个  需要删除的图片有 $needDeleteCount 个"
                )
                sp.edit {
                    putString("gift_cache_table", map.toJson())
                }
            }
        }
    }

    fun preloadEmoji() {
        Log.e("1234", "开始预加载表情")
        val context = BaseApplication.INSTANCE
        val url = configResponse?.meme ?: return
        val rootFile = File(context.filesDir, "emoji")
        rootFile.mkdirs()
        if (rootFile.exists() && rootFile.length() > 0 && (!rootFile.listFiles().isNullOrEmpty())) {
            rootFile.listFiles()?.filter { it.isDirectory }?.forEach {
                emojiIds.add(it.name)
            }
            if (emojiIds.isNotEmpty()) {
                Log.e("1234", "本地有表情 return")
                return
            }
        }
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    Log.e("1234", "开始下载表情")
                    val zipFile = File(context.filesDir, "meme.zip")
                    val zipDownloadCompleteFile = File(context.filesDir, "meme")
                    url.downloadToFile(zipFile)
                    Log.e("1234", "表情下载完成 开始解压")
                    zipFile.unzip(zipDownloadCompleteFile)
                    Log.e("1234", "表情下载完成 解压完成")
                    zipDownloadCompleteFile.listFiles()?.filter { it.isDirectory }?.forEach {
                        it.listFiles()?.filter { it.isFile }?.withIndex()?.forEach { itemGift ->
                            val file = File(rootFile, itemGift.index.toString())
                            file.mkdirs()
                            itemGift.value.copyTo(File(file, "gif.gif"))
                            emojiIds.add(itemGift.index.toString())
                        }
                    }
                    Log.e("1234", "一共有${emojiIds.size}个表情")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun hearBeat(api: BasicApiService) {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                while (true) {
                    try {
                        api.hearBeat()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    delay(30 * 1000)
                }
            }
        }
    }
}