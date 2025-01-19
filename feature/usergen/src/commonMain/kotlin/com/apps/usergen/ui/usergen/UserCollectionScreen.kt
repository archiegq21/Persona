package com.apps.usergen.ui.usergen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apps.usergen.Res
import com.apps.usergen.add_generated_user
import com.apps.usergen.data.UserCollection
import com.apps.usergen.ui.assets.EmptyHero
import com.apps.usergen.user_gen_title
import com.apps.usergen.empty_user_collection
import com.apps.usergen.viewmodel.GenUserParams
import com.apps.usergen.viewmodel.UserCollectionViewModel
import com.library.paging.LazyPagingItems
import com.library.paging.collectAsLazyPagingItems
import com.library.paging.isEmpty
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import com.library.paging.items

@Composable
internal fun UserCollectionRoute(
    onGenerateUser: () -> Unit,
    onViewUserCollection: (GenUserParams) -> Unit,
    viewModel: UserCollectionViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val userCollection = viewModel.pagedUserCollection.collectAsLazyPagingItems()

    UserCollectionScreen(
        onGenerateUser = onGenerateUser,
        onViewUserCollection = onViewUserCollection,
        userCollection = userCollection,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserCollectionScreen(
    onGenerateUser: () -> Unit,
    onViewUserCollection: (GenUserParams) -> Unit,
    userCollection: LazyPagingItems<UserCollection>,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyStaggeredGridState()
    val fabVisibility by remember { derivedStateOf { userCollection.itemCount > 0 } }
    val expandFab by remember { derivedStateOf { state.firstVisibleItemIndex <= 0 } }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.user_gen_title)) },
                scrollBehavior = scrollBehavior,
                modifier = Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        floatingActionButton = {
            AddUserCollectionFab(
                onClick = onGenerateUser,
                visible = fabVisibility,
                expanded = expandFab,
                modifier = Modifier,
            )
        }
    ) { padding ->
        val layoutDirection = LocalLayoutDirection.current
        val contentPadding = PaddingValues(
            top = padding.calculateTopPadding() + 16.dp,
            bottom = padding.calculateBottomPadding() + 16.dp,
            start = padding.calculateStartPadding(layoutDirection) + 16.dp,
            end = padding.calculateEndPadding(layoutDirection) + 16.dp,
        )

        AnimatedContent(
            targetState = userCollection.isEmpty(),
            modifier = Modifier.fillMaxSize(),
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { isEmpty ->
            when (isEmpty) {
                true -> EmptyUserCollection(
                    onGenerateUser = onGenerateUser,
                    modifier = Modifier.padding(contentPadding).fillMaxSize()
                )

                false -> LazyVerticalStaggeredGrid(
                    state = state,
                    contentPadding = contentPadding,
                    columns = StaggeredGridCells.Adaptive(minSize = 180.dp),
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(userCollection, key = { it.id }) { _, item ->
                        UserCollectionCard(
                            onClick = { onViewUserCollection(GenUserParams(item.id)) },
                            modifier = Modifier.fillMaxWidth(),
                            collection = item,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyUserCollection(
    onGenerateUser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = BiasAlignment(0f, -0.5f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(
                modifier = Modifier
                    .widthIn(max = 320.dp),
                imageVector = EmptyHero,
                contentDescription = null,
            )
            Text(
                text = stringResource(Res.string.empty_user_collection),
                modifier = Modifier.widthIn(200.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                style = MaterialTheme.typography.titleMedium,
            )
            OutlinedButton(onClick = onGenerateUser) {
                Icon(Icons.Rounded.PersonAdd, null)
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text(stringResource(Res.string.add_generated_user))
            }
        }
    }
}