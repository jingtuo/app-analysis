package io.github.jingtuo.android.aa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.jingtuo.android.aa.ui.widget.AppInfo
import io.github.jingtuo.android.aa.ui.widget.AppList
import io.github.jingtuo.android.aa.ui.widget.LogInfo

@Composable
fun MyNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppListDestination.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = AppListDestination.route) {
            AppList(onClickCollectLog = {
                navController.navigate(LogInfoDestination.route)
            }, onClickItem = { pkgName ->
                navController.navigate("app_info/$pkgName")
            })
        }
        composable(route = AppInfoDestination.route) { entry ->
            AppInfo(entry.arguments?.getString("pkgName") ?: "")
        }
        composable(route = LogInfoDestination.route) {
            LogInfo()
        }
    }
}