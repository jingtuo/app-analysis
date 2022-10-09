package io.github.jingtuo.android.aa

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                onClickCollectLog = {
                    navController.navigate(LogListDestination.route)
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
            LogList(application = application)
        }
    }
}