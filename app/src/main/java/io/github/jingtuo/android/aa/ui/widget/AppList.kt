package io.github.jingtuo.android.aa.ui.widget

import android.content.pm.PackageInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.jingtuo.android.aa.ext.label
import io.github.jingtuo.android.aa.ext.signInfo
import io.github.jingtuo.android.aa.ui.model.AppListViewModel

@Composable
fun AppList(vm: AppListViewModel = viewModel(), onClickLog: () -> Unit, onClickItem: (pkgName: String) -> Unit) {
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
                val curPackages = vm.curPackages().observeAsState(emptyList<PackageInfo>())
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            vm.search(it)
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
    val labelWidth = 70.dp
    val dividerHeight = 6.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable {
                onClickItem(packageInfo.packageName)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "应用名: ",
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.width(labelWidth)
            )
            Text(
                text = packageInfo.label(context.packageManager),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(dividerHeight),
            color = Color.Transparent
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "包名: ",
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.width(labelWidth)
            )
            Text(
                text = packageInfo.packageName,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(dividerHeight),
            color = Color.Transparent
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "MD5: ",
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.width(labelWidth)
            )
            Text(
                text = packageInfo.signInfo("MD5", true),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}