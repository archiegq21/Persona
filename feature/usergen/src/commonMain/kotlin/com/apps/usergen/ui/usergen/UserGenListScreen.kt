package com.apps.usergen.ui.usergen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun UserGenListRoute(
    onGenerateUser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    UserGenListScreen(
        modifier = modifier,
    )
}

@Composable
private fun UserGenListScreen(
    modifier: Modifier = Modifier,
) {

}