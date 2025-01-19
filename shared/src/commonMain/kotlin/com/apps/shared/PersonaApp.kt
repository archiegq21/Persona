package com.apps.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.apps.designsystem.theme.PersonaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PersonaApp() {
    PersonaTheme(
        darkTheme = isSystemInDarkTheme(),
    ) {
        AppNavigation(
            navController = rememberNavController(),
        )
    }
}