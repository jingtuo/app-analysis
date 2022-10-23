package io.github.jingtuo.android.aa.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.jingtuo.android.aa.ui.model.LogListViewModel
import io.github.jingtuo.android.aa.R
import io.github.jingtuo.android.aa.db.model.LogInfo

@Composable
fun LogList(
    viewModel: LogListViewModel = viewModel(),
    onClickBack: () -> Unit
) {
    var filterDialog by remember { mutableStateOf(false) }
    var tagText by remember { mutableStateOf("") }
    var prioritySelected by remember { mutableStateOf("All") }
    var priorityExpanded by remember { mutableStateOf(false) }
    var contentText by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            LogListAppBar(
                onClickBack = {
                    onClickBack()
                },
                onClickFilter = {
                    filterDialog = true
                })
        }
    ) { innerPadding ->
        val logs = viewModel.logs().observeAsState(emptyList())
        val priorityList = listOf(
            "All", "Debug",
            "Info", "Warn", "Error",
            "Fatal", "Silent", "Verbose"
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(logs.value) { it ->
                LogRow(logInfo = it, viewModel)
                Divider()
            }
        }

        if (filterDialog) {
            AlertDialog(
                onDismissRequest = {
                    filterDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            filterDialog = false
                            viewModel.loadLogs(tagText, prioritySelected, contentText)
                        }
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            filterDialog = false
                        }
                    ) {
                        Text("取消")
                    }
                },
                title = {
                    Text("过滤日志")
                },
                text = {
                    ConstraintLayout {
                        val (tagId, priorityId, contentId) = createRefs()
                        OutlinedTextField(
                            value = tagText,
                            onValueChange = {
                                tagText = it
                            },
                            label = {
                                Text("TAG")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(tagId) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }
                        )
                        OutlinedButton(
                            onClick = { priorityExpanded = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(priorityId) {
                                    top.linkTo(tagId.bottom, 10.dp)
                                    start.linkTo(parent.start)
                                }
                        ) {
                            Text(
                                text = prioritySelected,
                                modifier = Modifier.weight(1.0F, true)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                                contentDescription = "drop down"
                            )
                        }
                        DropdownMenu(
                            expanded = priorityExpanded,
                            onDismissRequest = { priorityExpanded = false },
                        ) {
                            for (item in priorityList) {
                                DropdownMenuItem(onClick = {
                                    priorityExpanded = false
                                    prioritySelected = item
                                }) {
                                    Text(text = item, modifier = Modifier.fillMaxWidth())
                                }
                                Divider()
                            }
                        }
                        OutlinedTextField(
                            value = contentText,
                            onValueChange = {
                                contentText = it
                            },
                            label = {
                                Text("内容")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp)
                                .constrainAs(contentId) {
                                    top.linkTo(priorityId.bottom, 10.dp)
                                    start.linkTo(parent.start)
                                }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun LogRow(logInfo: LogInfo, viewModel: LogListViewModel) {
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
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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