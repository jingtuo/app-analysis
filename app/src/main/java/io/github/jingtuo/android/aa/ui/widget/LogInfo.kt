package io.github.jingtuo.android.aa.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun LogInfo() {
    Scaffold(
        topBar = {
            MyTopAppBar(title = "日志信息")
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            var text by remember { mutableStateOf("测试") }
            Text(text = text)
        }
    }
}

fun readLog() {
    val reader = BufferedReader(
        InputStreamReader(
        Runtime.getRuntime().exec("logcat ").inputStream)
    )
    while (true) {
        val line = reader.readLine()
        if (line != null) {
            Log.i("Test", line)
        } else {
            break
        }
    }
}