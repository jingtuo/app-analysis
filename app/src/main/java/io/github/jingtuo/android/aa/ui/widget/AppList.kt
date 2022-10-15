package io.github.jingtuo.android.aa.ui.widget

import android.content.pm.PackageInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import io.github.jingtuo.android.aa.ext.label
import io.github.jingtuo.android.aa.ext.packages

@Composable
fun AppList(onClickLog: () -> Unit, onClickItem: (pkgName: String) -> Unit) {
    val context = LocalContext.current
    val packages = context.packages()
    val liveDataCurPackages = MutableLiveData<List<PackageInfo>>()
    Scaffold(
        topBar = {
            HomeTopAppBar(onClickLog)
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                var searchText by remember { mutableStateOf("") }
                val curPackages = liveDataCurPackages.observeAsState(packages)
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            liveDataCurPackages.value = packages.filter { pkgInfo ->
                                pkgInfo.packageName.contains(searchText)
                                        || pkgInfo.label(context.packageManager)
                                    .contains(searchText)
                            }
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
                LazyColumn {
                    items(curPackages.value) { it ->
                        AppRow(it, onClickItem)
                        Divider()
                    }
                }
            }
        }

    }

}

@Composable
fun AppRow(packageInfo: PackageInfo, onClickItem: (pkgName: String) -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 10.dp)
            .clickable {
                onClickItem(packageInfo.packageName)
            }
    ) {
        Text(
            text = packageInfo.label(context.packageManager),
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = packageInfo.packageName,
            color = MaterialTheme.colors.onBackground,
            fontSize = 14.sp
        )
    }
}