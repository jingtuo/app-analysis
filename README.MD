# 应用分析(App Analysis)

基于Compose开发应用分析, 熟悉Compose的基本使用以及部分组件(navigation, worker)

## 版本

### 1.0.0

1. 基于logcat命令收集系统运行日志


## 系统版本差异

1. 系统 \> Android 10(API 29), 只有部分应用对当前应用可见


## 问题

1. 应用升级之后未打开, PackageManager.getPackageInfo获取activities为空, 

## 参考资料

- [Compose Document](https://developer.android.google.cn/jetpack/compose/documentation)
- [Compose Library](https://developer.android.google.cn/jetpack/androidx/explorer?case=all)
- [Compose Navigation CodeLab](https://developer.android.google.cn/codelabs/jetpack-compose-navigation#0)
- [Compose Material Design Components](https://developer.android.google.cn/reference/kotlin/androidx/compose/material/package-summary)
- [Kotlin Coroutines On Android](https://developer.android.google.cn/kotlin/coroutines)
- [Accompanist Permissions](https://google.github.io/accompanist/permissions/)
- [Background Task](https://developer.android.google.cn/guide/background)
- [Long Running Worker](https://developer.android.google.cn/topic/libraries/architecture/workmanager/advanced/long-running#foreground-service-type)
    使用WorkManager启动一个前台服务, OPPO Reno3出现如下错误:
    ```text
    java.lang.IllegalArgumentException: foregroundServiceType 0x00000001 is not a subset of foregroundServiceType attribute 0x00000000 in service element of manifest file
    ```
- [Notification](https://developer.android.google.cn/develop/ui/views/notifications)

### 课外资料

- [Kotlin Symbol Processor](https://kotlinlang.org/docs/ksp-overview.html)