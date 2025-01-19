package com.apps.usergen.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.apps.usergen.Res
import com.apps.usergen.retry
import com.apps.usergen.data.User
import com.apps.usergen.viewmodel.UserListViewModel
import com.library.paging.LazyPagingItems
import com.library.paging.collectAsLazyPagingItems
import com.library.paging.isEmpty
import com.library.paging.items
import com.library.placeholder.PlaceholderHighlight
import com.library.placeholder.placeholder
import com.library.placeholder.shimmer
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun UserListRoute(
    state: LazyListState = rememberLazyListState(),
    onUserClick: (User) -> Unit,
    viewModel: UserListViewModel,
    modifier: Modifier = Modifier
) {
    val users = viewModel.pagedUsers.collectAsLazyPagingItems()

    UserListScreen(
        state = state,
        onUserClick = onUserClick,
        users = users,
        modifier = modifier,
    )
}

@Composable
private fun UserListScreen(
    state: LazyListState,
    onUserClick: (User) -> Unit,
    users: LazyPagingItems<User>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (users.loadState.refresh == LoadState.Loading && users.isEmpty()) {
            items(10) {
                LoadingUserCard()
            }
        }

        items(users, key = { it.userId }, contentType = { "UserCard" }) { user ->
            UserCard(
                user = user,
                onClick = { onUserClick(user) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (users.loadState.append == LoadState.Loading) {
            item {
                LoadingUserCard()
            }
        }

        if (users.loadState.hasError && users.loadState.isIdle) {
            item {
                Box(Modifier.padding(16.dp).fillMaxWidth()) {
                    Button(onClick = { users.retry() }) {
                        Text(stringResource(Res.string.retry))
                    }
                }
            }
        }
    }
}

@Composable
internal fun LoadingUserCard() {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .size(80.dp)
                .placeholder(
                    visible = true,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.shimmer(
                        highlightColor = Color.White,
                    ),
                    shape = CircleShape,
                ),
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.7f)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        ),
                        shape = CircleShape,
                    ),
                text = "",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(0.8f)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White,
                        ),
                        shape = CircleShape,
                    ),
                text = "",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}