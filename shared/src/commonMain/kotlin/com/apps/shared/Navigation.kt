package com.apps.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.apps.usergen.ui.navigation.UserGenNavDestination
import com.apps.usergen.ui.navigation.userGenNavGraph

@Composable
internal fun AppNavigation(
    navController: NavHostController,
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = UserGenNavDestination.UserGenList,
    ) {
        userGenNavGraph(navController)
    }
}