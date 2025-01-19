package com.apps.usergen.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.apps.usergen.components.BackHandler
import com.apps.usergen.ui.request.UserGenRequestRoute
import com.apps.usergen.ui.usergen.UserCollectionRoute
import com.apps.usergen.ui.users.UserListRoute
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
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

        val navigator = rememberListDetailPaneScaffoldNavigator<String>()
        BackHandler(navigator.canNavigateBack()) {
            navigator.navigateBack()
        }

        ListDetailPaneScaffold(
            modifier = Modifier.fillMaxSize(),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    UserListRoute(
                        onBack = { navigator.navigateBack() },
                        onUserClick = {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it.name.first)
                        }
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    navigator.currentDestination?.content?.let {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = it)
                        }
                    }
                }
            },
        )
    }
}