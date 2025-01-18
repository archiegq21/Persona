package com.apps.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.apps.designsystem.theme.PersonaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun PersonaApp() {
    PersonaTheme(
        darkTheme = isSystemInDarkTheme(),
    ) {

    }
}