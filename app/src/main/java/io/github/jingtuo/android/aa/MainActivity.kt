package io.github.jingtuo.android.aa

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.jingtuo.android.aa.service.LogService
import io.github.jingtuo.android.aa.ui.model.MainViewModel
import io.github.jingtuo.android.aa.ui.theme.AppAnalysisTheme

class MainActivity : ComponentActivity() {

    var mainViewModel: MainViewModel? = null

    var notificationManager: NotificationManagerCompat? = null

    private var requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                //授权成功
                createNotificationChannel()
                startLogCatService()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(
            this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]
        mainViewModel?.let {
            it.clearLog()
//            it.startLogCat()
        }
        notificationManager = NotificationManagerCompat.from(this)
        checkPermissions()

        setContent {
            AppAnalysisTheme {
                //NavController必须在最顶层定义, 因为它要管理所有页面
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination
                val currentScreen = allDestination
                    .find { it.route == currentDestination?.route } ?: AppListDestination
                MyNavHost(navController = navController, application = application)
            }
        }
    }

    /**
     * 检测权限
     */
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    //显示对话框
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle(R.string.request_permission)
                        .setMessage(R.string.notification_permission_rationale)
                        .setPositiveButton(R.string.grant) { dialog, which ->
                            requestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        .setNegativeButton(R.string.deny) { dialog, which ->
                            dialog.dismiss()
                        }
                        .create()
                    alertDialog.show()
                } else {
                    requestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                return
            }
        }
        createNotificationChannel()
        startLogCatService()
    }

    /**
     * 创建通知渠道
     */
    private fun createNotificationChannel() {
        notificationManager?.let { it ->
            if (it.areNotificationsEnabled()) {
                val channel = NotificationChannelCompat.Builder(
                    MyApp.CHANNEL_ID_LOG,
                    NotificationManagerCompat.IMPORTANCE_LOW
                )
                    .setName(getString(R.string.log))
                    .build()
                it.createNotificationChannel(channel)
            }
        }
    }

    private fun startLogCatService() {
        notificationManager?.let {
            if (it.areNotificationsEnabled()) {
                val intent = Intent(this, LogService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
            }
        }
    }
}