package io.github.jingtuo.android.aa.ui.widget

import android.content.pm.PermissionInfo
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
import io.github.jingtuo.android.aa.ext.*
import io.github.jingtuo.android.aa.ui.model.Permission

@Composable
fun PermissionList(pkgName: String) {
    val context = LocalContext.current
    val pkgInfo = context.packageInfo(pkgName)
    val permissions = context.permissions(pkgName)
    Scaffold(topBar = {
        MyTopAppBar(title = pkgInfo.label(context.packageManager) + "-权限列表")
    }) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column {
                var searchText by remember { mutableStateOf("") }
                val liveDataCurPermissions = MutableLiveData<List<Permission>>()
                val curPermissions = liveDataCurPermissions.observeAsState(permissions)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            liveDataCurPermissions.value = permissions.filter { permission ->
                                permission.code.contains(searchText)
                                        || permission.typeName().contains(searchText)
                            }
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "搜索")
                        },
                        trailingIcon = {
                            if (searchText.isNotEmpty()) {
                                Icon(imageVector = Icons.Default.Clear,
                                    contentDescription = "清除",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { searchText = "" }
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
                    items(curPermissions.value) { it ->
                        PermissionRow(it)
                        Divider()
                    }
                }
            }

        }
    }
}

@Composable
fun PermissionRow(permission: Permission) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(16.dp, 10.dp)
    ) {
        Text(
            text = permission.code,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = permission.typeName(),
            color = MaterialTheme.colors.onBackground,
            fontSize = 14.sp
        )
    }
}