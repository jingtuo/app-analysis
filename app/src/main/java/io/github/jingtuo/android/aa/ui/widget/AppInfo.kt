package io.github.jingtuo.android.aa.ui.widget

import android.content.pm.ActivityInfo
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
import io.github.jingtuo.android.aa.ext.activities
import io.github.jingtuo.android.aa.ext.clsName
import io.github.jingtuo.android.aa.ext.label
import io.github.jingtuo.android.aa.ext.packageInfo

@Composable
fun AppInfo(pkgName: String) {
    val context = LocalContext.current
    val pkgInfo = context.packageInfo(pkgName)
    val activities = context.activities(pkgName)
    Scaffold(topBar = {
        MyTopAppBar(title = pkgInfo.label(context.packageManager) ?: "应用信息")
    }) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                var searchText by remember { mutableStateOf("") }
                val liveDataCurActivities = MutableLiveData<List<ActivityInfo>>()
                val curActivities = liveDataCurActivities.observeAsState(activities)
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(value = searchText, onValueChange = {
                        searchText = it
                        liveDataCurActivities.value = activities.filter { activityInfo ->
                            activityInfo.label(context.packageManager).contains(searchText)
                                    || activityInfo.clsName().contains(searchText)
                        }
                    }, leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = "搜索"
                        )
                    }, trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            Icon(imageVector = Icons.Default.Clear,
                                contentDescription = "清除",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        searchText = ""
                                    })
                        }
                    }, maxLines = 1, keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    )
                    )
                }
                LazyColumn {
                    items(curActivities.value) { it ->
                        ActivityRow(it)
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityRow(activityInfo: ActivityInfo) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(16.dp, 10.dp)
    ) {
        Text(
            text = activityInfo.label(context.packageManager),
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = activityInfo.clsName(),
            color = MaterialTheme.colors.onBackground,
            fontSize = 14.sp
        )
    }
}