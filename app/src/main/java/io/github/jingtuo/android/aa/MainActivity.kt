package io.github.jingtuo.android.aa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.jingtuo.android.aa.ui.model.MainViewModel
import io.github.jingtuo.android.aa.ui.theme.AppAnalysisTheme

class MainActivity : ComponentActivity() {

    var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this, ViewModelProvider
            .AndroidViewModelFactory.getInstance(application))[MainViewModel::class.java]
        mainViewModel?.let {
            it.clearLog()
            it.startLogCat()
        }
        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.collecting_logs))
            .build()


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
}