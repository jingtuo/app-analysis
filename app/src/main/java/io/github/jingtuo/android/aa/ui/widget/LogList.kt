package io.github.jingtuo.android.aa.ui.widget

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.jingtuo.android.aa.ui.model.LogViewModel
import io.github.jingtuo.android.aa.R
import io.github.jingtuo.android.aa.db.model.LogInfo

@Composable
fun LogList(
    application: Application, viewModel: LogViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application = application)
    )
) {
    Scaffold(
        topBar = {
            MyTopAppBar(title = "日志列表", R.drawable.ic_baseline_point_line_list_24)
        }
    ) { innerPadding ->
        val logs = viewModel.logs().observeAsState(emptyList())
        viewModel.loadLogs()
        var priorityExpanded by remember { mutableStateOf(false) }
        var prioritySelected by remember { mutableStateOf("All") }
        val priorityList = listOf("All", "Debug",
            "Info", "Warn", "Error",
            "Fatal", "Silent", "Verbose")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(text = "Priority: ")
                OutlinedButton(
                    onClick = { priorityExpanded = true }
                ) {
                    Text(text = prioritySelected)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                        contentDescription = "drop down"
                    )
                }
            }
            LazyColumn {
                items(logs.value) { it ->
                    LogRow(logInfo = it, viewModel)
                    Divider()
                }
            }
            DropdownMenu(expanded = priorityExpanded, onDismissRequest = { priorityExpanded = false }) {
                for(item in priorityList) {
                    DropdownMenuItem(onClick = {
                        priorityExpanded = false
                        prioritySelected = item
                        //更新数据
                        viewModel.loadLogs("", item, "")
                    }) {
                        Text(text = item)
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun LogRow(logInfo: LogInfo, viewModel: LogViewModel) {
    ConstraintLayout(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        val (timeId, pidId, tidId, tagId, priorityId, contentId) = createRefs()
        Text(
            text = viewModel.format(logInfo.time),
            modifier = Modifier.constrainAs(timeId) {
                top.linkTo(parent.top, 0.dp)
                start.linkTo(parent.start, 0.dp)
            }
        )
        Text(
            text = "PID: ${logInfo.pid}",
            modifier = Modifier.constrainAs(pidId) {
                top.linkTo(timeId.bottom, 6.dp)
                start.linkTo(parent.start, 0.dp)
                end.linkTo(tidId.start, 0.dp)
                width = Dimension.fillToConstraints
                horizontalChainWeight = 1.0F
            },
            textAlign = TextAlign.Start
        )
        Text(
            text = "TID: ${logInfo.tid}",
            modifier = Modifier.constrainAs(tidId) {
                top.linkTo(pidId.top, 0.dp)
                bottom.linkTo(pidId.bottom, 0.dp)
                start.linkTo(pidId.end, 0.dp)
                end.linkTo(parent.end, 0.dp)
                width = Dimension.fillToConstraints
                horizontalChainWeight = 1.0F
            }
        )
        Text(
            text = "TAG: ${logInfo.tag}",
            modifier = Modifier.constrainAs(tagId) {
                top.linkTo(pidId.bottom, 6.dp)
                start.linkTo(parent.start, 0.dp)
                end.linkTo(priorityId.start, 0.dp)
                width = Dimension.fillToConstraints
                horizontalChainWeight = 1.0F
            },
            maxLines = 1
        )
        Text(
            text = "Priority: ${logInfo.priority}",
            modifier = Modifier.constrainAs(priorityId) {
                top.linkTo(tagId.top, 0.dp)
                bottom.linkTo(tagId.bottom, 0.dp)
                start.linkTo(tagId.end, 0.dp)
                end.linkTo(parent.end, 0.dp)
                width = Dimension.fillToConstraints
                horizontalChainWeight = 1.0F
            }
        )
        Text(
            text = logInfo.content,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(contentId) {
                    top.linkTo(tagId.bottom, 6.dp)
                    start.linkTo(parent.start, 0.dp)
                    end.linkTo(parent.end, 0.dp)
                }
        )
    }
}