package io.github.jingtuo.android.aa.ui.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import io.github.jingtuo.android.aa.R

@Composable
fun HomeTopAppBar(onClickLog: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar() {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (logoId, titleId, logId) = createRefs()
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_apps_24),
                    contentDescription = "app bar icon",
                    modifier = Modifier.constrainAs(logoId) {
                        top.linkTo(parent.top, 0.dp)
                        bottom.linkTo(parent.bottom, 0.dp)
                        start.linkTo(parent.start, 10.dp)
                    }
                )
                Text(
                    text = "应用列表",
                    modifier = Modifier.constrainAs(titleId) {
                        top.linkTo(parent.top, 0.dp)
                        bottom.linkTo(parent.bottom, 0.dp)
                        start.linkTo(logoId.end, 10.dp)
                    }
                )
                OutlinedButton(
                    modifier = Modifier
                        .constrainAs(logId) {
                            top.linkTo(parent.top, 0.dp)
                            bottom.linkTo(parent.bottom, 0.dp)
                            end.linkTo(parent.end, 10.dp)
                        },
                    onClick = onClickLog,
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = MaterialTheme.colors.onPrimary,
                    ),
                    border = BorderStroke(0.5.dp, MaterialTheme.colors.onPrimary)
                ) {
                    Text("Log")
                }
            }
        }
    }
}

@Composable
fun MyTopAppBar(title: String, @DrawableRes id: Int) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painter = painterResource(id), contentDescription = "app bar icon")
            Text(text = title, modifier = Modifier.padding(horizontal = 10.dp))
        }
    }
}

@Composable
fun MyTopAppBar(title: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, modifier = Modifier.padding(horizontal = 10.dp))
        }
    }
}

