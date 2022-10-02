package io.github.jingtuo.android.aa

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import io.github.jingtuo.android.aa.ui.model.PkgInfo
import io.github.jingtuo.android.aa.ui.theme.AppAnalysisTheme
import io.github.jingtuo.android.aa.ui.widget.AppList

class MainActivity : ComponentActivity() {

    val pkgList = MutableLiveData<List<PkgInfo>>()
    val allPkgList = mutableListOf<PkgInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getInstalledPackages(0)
        }
        for (item in packageList) {
            val pkgName = item.packageName
            val appName = item.applicationInfo.loadLabel(packageManager).toString() ?: "未知"
            allPkgList.add(PkgInfo(pkgName, appName))
        }
        setContent {
            AppAnalysisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    var searchText by remember { mutableStateOf("") }
                    val pkgList = pkgList.observeAsState(allPkgList)
                    Column(
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = searchText,
                                onValueChange = {
                                    searchText = it
                                    search(searchText)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "搜索"
                                    )

                                },
                                trailingIcon = {
                                    if (searchText.isNotEmpty()) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "清除",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
                                                    searchText = ""
                                                }
                                        )
                                    }
                                },
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search
                                )
                            )
                        }
                        Surface(
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            AppList(pkgList.value)
                        }
                    }
                }
            }
        }
    }

    fun search(searchText: String) {
        val list = allPkgList.filter { pkgInfo ->
            pkgInfo.pkgName.contains(searchText)
                    || pkgInfo.appName.contains(searchText)
        }
        pkgList.value = list
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppAnalysisTheme {
        Greeting("Android")
    }
}