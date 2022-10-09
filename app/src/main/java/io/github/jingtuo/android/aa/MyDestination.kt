package io.github.jingtuo.android.aa

interface MyDestination {
    val title: String
    val route: String
}

object AppListDestination: MyDestination {
    override val title: String
        get() = "应用列表"
    override val route: String
        get() = "app_list"
}

object AppInfoDestination: MyDestination {
    override val title: String
        get() = "应用信息"
    override val route: String
        get() = "app_info/{pkgName}"
}

object LogListDestination: MyDestination {
    override val title: String
        get() = "日志列表"
    override val route: String
        get() = "log_list"
}


val allDestination = listOf(AppListDestination, AppInfoDestination, LogListDestination)