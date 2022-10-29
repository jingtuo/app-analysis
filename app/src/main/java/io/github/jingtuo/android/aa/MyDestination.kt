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

object ActivityListDestination: MyDestination {
    override val title: String
        get() = "页面列表"
    override val route: String
        get() = "activity_list/{pkgName}"
}

object LogListDestination: MyDestination {
    override val title: String
        get() = "日志列表"
    override val route: String
        get() = "log_list"
}

object PermissionListDestination: MyDestination {
    override val title: String
        get() = "权限列表"
    override val route: String
        get() = "permission_list/{pkgName}"
}



val allDestination = listOf(AppListDestination, AppInfoDestination, LogListDestination,
    ActivityListDestination, PermissionListDestination)