package io.github.jingtuo.android.aa.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.jingtuo.android.aa.ext.label
import io.github.jingtuo.android.aa.ext.packageInfo

@Composable
fun AppInfo(pkgName: String, onClickPageList: (pkgName: String) -> Unit,
            onClickPermissionList: (pkgName: String) -> Unit) {
    val context = LocalContext.current
    val pkgInfo = context.packageInfo(pkgName)
    Scaffold(topBar = {
        MyTopAppBar(title = pkgInfo.label(context.packageManager) ?: "应用信息")
    }) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column {
                        Row(modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .fillMaxWidth()
                            .clickable {
                                onClickPageList(pkgName)
                            }) {
                            Text(text = "页面列表", modifier = Modifier.weight(1.0F))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "arrow forward",
                                modifier = Modifier.wrapContentSize()
                            )
                        }
                        Divider()
                        Row(modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .fillMaxWidth()
                            .clickable {
                                onClickPermissionList(pkgName)
                            }) {
                            Text(text = "权限列表", modifier = Modifier.weight(1.0F))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "arrow forward",
                                modifier = Modifier.wrapContentSize()
                            )
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
