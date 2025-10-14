package com.helper.develop.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Printer
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VideoOnly
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageAndVideo
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.SingleMimeType
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavBackStackEntry
import java.io.File

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageOnly(
    options: ActivityOptionsCompat? = null
) {
    launch(PickVisualMediaRequest(ImageOnly), options)
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchVideoOnly(
    options: ActivityOptionsCompat? = null,
) {
    launch(PickVisualMediaRequest(VideoOnly), options)
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageAndVideo(
    options: ActivityOptionsCompat? = null,
) {
    launch(PickVisualMediaRequest(ImageAndVideo), options)
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchMineType(
    mineType: String,
    options: ActivityOptionsCompat? = null,
) {
    launch(PickVisualMediaRequest(SingleMimeType(mineType)), options)
}

object CustomActivityResultContracts {

    class RequestNotificationPermission : ActivityResultContract<String?, Boolean>() {
        companion object {
            private const val IS_REQUEST = "IS_REQUEST"
            private const val SP_NAME = "NOTIFICATION_PERMISSION"
        }

        private var _input: String? = null
        private lateinit var _context: Context
        private val _requestPermission by lazy { ActivityResultContracts.RequestPermission() }
        private fun areNotificationsEnabled(): Boolean {
            val notificationManager = NotificationManagerCompat.from(_context)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !_input.isNullOrEmpty()) {
                notificationManager.getNotificationChannel(_input!!)?.let {
                    it.importance > NotificationManagerCompat.IMPORTANCE_NONE
                } == true
            } else {
                true
            } && notificationManager.areNotificationsEnabled()
        }

        override fun createIntent(context: Context, input: String?): Intent {
            val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                !sp.getBoolean(IS_REQUEST, false)
            ) {
                sp.edit(true) { putBoolean(IS_REQUEST, true) }
                _requestPermission.createIntent(context, Manifest.permission.POST_NOTIFICATIONS)
            } else {
                createNotificationSettingsIntent()
            }
        }

        private fun createNotificationSettingsIntent(): Intent {
            return Intent().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (_input.isNullOrEmpty()||!   NotificationManagerCompat.from(_context).areNotificationsEnabled()) {
                       action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, _context.packageName)
                    } else {
                        action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, _context.packageName)
                        putExtra(Settings.EXTRA_CHANNEL_ID, _input)
                    }
                } else {
                    action= "android.settings.APP_NOTIFICATION_SETTINGS"
                    putExtra("app_package", _context.packageName)
                    putExtra("app_uid", _context.applicationInfo.uid)
                }
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: String?
        ): SynchronousResult<Boolean>? {
            _context = context.applicationContext
            _input = input
            return SynchronousResult(true).takeIf { areNotificationsEnabled() }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return areNotificationsEnabled()
        }
    }

    class RequestUnknownAppInstallPermission : ActivityResultContract<Unit, Boolean>() {
        private lateinit var _context: Context
        private fun canRequestPackageInstalls(): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                _context.packageManager.canRequestPackageInstalls()
            } else {
                true
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun createIntent(context: Context, input: Unit): Intent {
            return Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
          data = "package:${context.packageName}".toUri()
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            _context = context.applicationContext
            return SynchronousResult(true).takeIf { canRequestPackageInstalls() }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return canRequestPackageInstalls()
        }
    }

    class RequestOverlayDrawPermission : ActivityResultContract<Unit, Boolean>() {

        private lateinit var _context: Context
        private fun canDrawOverlays(): Boolean {
            return Settings.canDrawOverlays(_context)
        }

        override fun createIntent(context: Context, input: Unit): Intent {
            return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            _context = context.applicationContext
            return SynchronousResult(true).takeIf { canDrawOverlays() }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return canDrawOverlays()
        }
    }

    class RequestExternalStorageAccessPermission(
        private val useSAF: Boolean = true
    ) : ActivityResultContract<Unit, Boolean>() {
        companion object {
            private val EXTERNAL_STORAGE_PERMISSIONS = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        private lateinit var _context: Context
        private fun isExternalStorageManager(): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (useSAF) {
                    true
                } else {
                    Environment.isExternalStorageManager()
                }
            } else {
                EXTERNAL_STORAGE_PERMISSIONS.all {
                    ContextCompat.checkSelfPermission(
                        _context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }
            }
        }

        override fun createIntent(
            context: Context,
            input: Unit
        ): Intent {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    .apply {
                     data = Uri.fromParts("package", context.packageName, null)
                    }
            } else {
                ActivityResultContracts.RequestMultiplePermissions()
                    .createIntent(context, EXTERNAL_STORAGE_PERMISSIONS)
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            _context = context.applicationContext
            return SynchronousResult(true).takeIf { isExternalStorageManager() }
        }

        override fun parseResult(
            resultCode: Int,
            intent: Intent?
        ): Boolean {
            return isExternalStorageManager()
        }
    }

    class PickVisualMedia(
        private val maxItems: Int = 1,
    ) : ActivityResultContract<PickVisualMediaRequest, List<Uri>>() {
        private val _pickMultipleVisualMedia by lazy {
            ActivityResultContracts.PickMultipleVisualMedia(maxItems)
        }
        private val _pickVisualMedia  by lazy {
            ActivityResultContracts.PickVisualMedia()
        }

        override fun createIntent(
            context: Context,
            input: PickVisualMediaRequest
        ): Intent {
            return if (maxItems > 1) {
                _pickMultipleVisualMedia
                    .createIntent(context, input)
            } else {
                _pickVisualMedia
                    .createIntent(context, input)
            }
        }

        override fun parseResult(
            resultCode: Int,
            intent: Intent?
        ): List<Uri> {
            return if (maxItems > 1) {
                _pickMultipleVisualMedia.parseResult(resultCode, intent)
            } else {
                _pickVisualMedia.parseResult(resultCode, intent)?.let { listOf(it) } ?: listOf()
            }
        }
    }

    class TakePicture(
        private val authority: String,
        private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
    ) : ActivityResultContract<File, File?>() {
        private val _takePicture = ActivityResultContracts.TakePicture()

        override fun createIntent(context: Context, input: File): Intent {
            savedStateHandle["TakePicture_input"] = input
            return _takePicture.createIntent(context, input.toProviderUri(context, authority)!!)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): File? {
            return if (_takePicture.parseResult(resultCode, intent)) {
                savedStateHandle["TakePicture_input"]
            } else {
                null
            }
        }
    }

}

