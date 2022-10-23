package io.github.jingtuo.android.aa.ui.model

import android.app.Application
import android.content.pm.PackageInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.jingtuo.android.aa.MyApp
import io.github.jingtuo.android.aa.ext.label
import io.github.jingtuo.android.aa.ext.packages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppListViewModel(application: Application) : AndroidViewModel(application) {

    private var packages: List<PackageInfo>? = null

    private val liveDataCurPackages = MutableLiveData<List<PackageInfo>>()

    init {
        viewModelScope.launch {
            packages = loadPackages()
            liveDataCurPackages.value = packages
        }
    }

    fun curPackages(): LiveData<List<PackageInfo>> {
        return liveDataCurPackages
    }

    fun search(text: String) {
        viewModelScope.launch {
            liveDataCurPackages.value = searchPackages(text)
        }
    }

    private suspend fun loadPackages(): List<PackageInfo> {
        return withContext(Dispatchers.IO) {
            getApplication<MyApp>().packages()
        }
    }

    private suspend fun searchPackages(text: String): List<PackageInfo> {
        return withContext(Dispatchers.IO) {
            packages?.filter { pkgInfo ->
                pkgInfo.packageName.contains(text)
                        || pkgInfo.label(getApplication<MyApp>().packageManager)
                    .contains(text)
            } ?: emptyList()
        }
    }
}