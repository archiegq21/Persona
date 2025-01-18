package com.apps.usergen.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apps.usergen.ui.usergen.UserGenListRoute
import kotlinx.serialization.Serializable

sealed interface UserGenNavDestination {
    @Serializable
    data object UserGenList : UserGenNavDestination
    @Serializable
    data object UserGenRequest : UserGenNavDestination
}

fun NavGraphBuilder.userGenNavGraph(
    navController: NavController,
) {
    composable<UserGenNavDestination.UserGenList>(
        popExitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        UserGenListRoute(
            onGenerateUser = {
                navController.navigate(UserGenNavDestination.UserGenRequest)
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
    composable<UserGenNavDestination.UserGenRequest>(
        popExitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {

    }
}