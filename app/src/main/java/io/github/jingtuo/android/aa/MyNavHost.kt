package io.github.jingtuo.android.aa

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.work.WorkManager
import io.github.jingtuo.android.aa.service.LogService
import io.github.jingtuo.android.aa.ui.widget.AppInfo
import io.github.jingtuo.android.aa.ui.widget.AppList
import io.github.jingtuo.android.aa.ui.widget.LogList

@Composable
fun MyNavHost(navController: NavHostController, application: Application) {
    NavHost(
        navController = navController,
        startDestination = AppListDestination.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = AppListDestination.route) {
            AppList(
                onClickLog = {
                    //停止收集日志
                    val intent = Intent(LogService.ACTION_LOGCAT)
                    intent.putExtra(LogService.KEY_OPERATE, LogService.OPERATE_STOP)
                    application.sendBroadcast(intent)
                    navController.navigate(
                        route = LogListDestination.route,
                        navOptions = NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .build()
                    )
                },
                onClickItem = { pkgName ->
                    navController.navigate("app_info/$pkgName")
                }
            )
        }
        composable(route = AppInfoDestination.route) { entry ->
            AppInfo(entry.arguments?.getString("pkgName") ?: "")
        }
        composable(route = LogListDestination.route) {
            LogList(application = application, onClickBack = {
                navController.popBackStack()
            })
        }
    }
}