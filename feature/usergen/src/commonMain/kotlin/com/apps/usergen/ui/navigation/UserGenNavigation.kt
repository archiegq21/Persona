package com.apps.usergen.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.apps.usergen.Res
import com.apps.usergen.components.BackHandler
import com.apps.usergen.data.User
import com.apps.usergen.ui.request.UserGenRequestRoute
import com.apps.usergen.ui.usergen.UserCollectionRoute
import com.apps.usergen.ui.users.UserListRoute
import com.apps.usergen.viewmodel.GenUserParams
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.apps.usergen.back

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

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
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
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
    ) { backStackEntry ->
        val params = backStackEntry.toRoute<UserGenNavDestination.UsersList>()

        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        val navigator = rememberListDetailPaneScaffoldNavigator<String>()
        BackHandler(navigator.canNavigateBack()) {
            navigator.navigateBack()
        }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(params.id) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (navigator.canNavigateBack()) {
                                    navigator.navigateBack()
                                } else {
                                    navController.popBackStack()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = stringResource(Res.string.back),
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        ) { padding ->
            ListDetailPaneScaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    val state = rememberLazyListState()
                    AnimatedPane {
                        UserListRoute(
                            state = state,
                            viewModel = koinViewModel {
                                parametersOf(params.id)
                            },
                            onUserClick = { user ->
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, user.userId)
                            }
                        )
                    }
                },
                detailPane = {
                    AnimatedPane {
                        navigator.currentDestination?.content?.let { it ->
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
}