package com.apps.usergen.ui.usergen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun UserGenListRoute(
    onGenerateUser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    UserGenListScreen(
        onGenerateUser = onGenerateUser,
        modifier = modifier,
    )
}

@Composable
private fun UserGenListScreen(
    onGenerateUser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var visible by remember { mutableStateOf(true) }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            AddGenUserFab(
                onClick = onGenerateUser,
                visible = visible,
                modifier = Modifier,
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Button(onClick = { visible = !visible }) {
                Text("Test")
            }
        }
    }
}