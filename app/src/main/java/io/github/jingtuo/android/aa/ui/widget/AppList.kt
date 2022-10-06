package io.github.jingtuo.android.aa.ui.widget

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
import io.github.jingtuo.android.aa.ext.getAllPkg
import io.github.jingtuo.android.aa.ui.model.PkgInfo

@Composable
fun AppList(onClickCollectLog: () -> Unit, onClickItem: (pkgName: String) -> Unit) {
    val context = LocalContext.current
    val allPkg = context.getAllPkg()
    val liveDataPkgList = MutableLiveData<List<PkgInfo>>()
    Scaffold(
        topBar = {
            HomeTopAppBar(onClickCollectLog)
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                var searchText by remember { mutableStateOf("") }
                val curPkgList = liveDataPkgList.observeAsState(allPkg)
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            liveDataPkgList.value = allPkg.filter { pkgInfo ->
                                pkgInfo.pkgName.contains(searchText)
                                        || pkgInfo.appName.contains(searchText)
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
                    items(curPkgList.value) { it ->
                        AppRow(it, onClickItem)
                        Divider()
                    }
                }
            }
        }

    }

}

@Composable
fun AppRow(pkgInfo: PkgInfo, onClickItem: (pkgName: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 10.dp)
            .clickable {
                onClickItem(pkgInfo.pkgName)
            }
    ) {
        Text(
            text = pkgInfo.appName,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = pkgInfo.pkgName,
            color = MaterialTheme.colors.onBackground,
            fontSize = 14.sp
        )
    }
}