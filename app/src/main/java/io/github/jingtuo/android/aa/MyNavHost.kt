package io.github.jingtuo.android.aa

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.jingtuo.android.aa.service.LogService
import io.github.jingtuo.android.aa.ui.widget.*

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
                    MyApp.instance().inLogList = true
                },
                onClickItem = { pkgName ->
                    navController.navigate("app_info/$pkgName")
                }
            )
        }
        composable(route = AppInfoDestination.route) { entry ->
            AppInfo(entry.arguments?.getString("pkgName") ?: "",
                onClickPageList = {
                    navController.navigate("activity_list/$it")
                },
                onClickPermissionList = {
                    navController.navigate("permission_list/$it")
                })
        }
        composable(route = LogListDestination.route) {
            LogList(onClickBack = {
                navController.popBackStack()
                MyApp.instance().inLogList = false
            })
        }
        composable(route = ActivityListDestination.route) { entry ->
            ActivityList(pkgName = entry.arguments?.getString("pkgName") ?: "")
        }
        composable(route = PermissionListDestination.route) { entry ->
            PermissionList(pkgName = entry.arguments?.getString("pkgName") ?: "")
        }
    }
}