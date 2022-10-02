package io.github.jingtuo.android.aa.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jingtuo.android.aa.ui.model.PkgInfo

@Preview
@Composable
fun AppListPreview() {
    val pkg1 = PkgInfo("应用1", "com.pkg1")
    val pkg2 = PkgInfo("应用1", "com.pkg1")
    val list = mutableListOf<PkgInfo>()
    list.add(pkg1)
    list.add(pkg2)

    Surface(
        color = MaterialTheme.colors.background
    ) {
        AppList(appList = list)
    }
}

@Composable
fun AppList(appList: List<PkgInfo>) {
    LazyColumn {
        items(appList) { it ->
            AppRow(it)
            Divider()
        }
    }
}

@Composable
fun AppRow(pkgInfo: PkgInfo) {
    Column(
        modifier = Modifier.padding(16.dp, 10.dp)
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