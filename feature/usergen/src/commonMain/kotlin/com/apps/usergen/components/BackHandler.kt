package com.apps.usergen.components

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
)