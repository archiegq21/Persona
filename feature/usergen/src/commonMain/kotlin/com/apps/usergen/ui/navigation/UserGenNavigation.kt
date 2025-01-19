package com.apps.usergen.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.apps.usergen.ui.request.UserGenRequestRoute
import com.apps.usergen.ui.usergen.UserCollectionRoute
import com.apps.usergen.viewmodel.GenUserParams
import kotlinx.serialization.Serializable

sealed interface UserGenNavDestination {
    @Serializable
    data object UserGenList : UserGenNavDestination

    @Serializable
    data object UserGenRequest : UserGenNavDestination

    @Serializable
    data class UsersList(
        val id: String,
    ) : UserGenNavDestination {

        companion object {
            operator fun invoke(params: GenUserParams) = UsersList(
                id = params.id,
            )
        }
    }
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
        UserCollectionRoute(
            onGenerateUser = {
                navController.navigate(UserGenNavDestination.UserGenRequest)
            },
            onViewUserCollection = { params ->
                navController.navigate(UserGenNavDestination.UsersList(params))
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
    dialog<UserGenNavDestination.UserGenRequest> {
        UserGenRequestRoute(
            onClose = { navController.popBackStack() },
            generateUsers = { params ->
                navController.navigate(UserGenNavDestination.UsersList(params))
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
    composable<UserGenNavDestination.UsersList>(
        popExitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) { backStackEntry ->
        val params = backStackEntry.toRoute<UserGenNavDestination.UsersList>()

    }
}