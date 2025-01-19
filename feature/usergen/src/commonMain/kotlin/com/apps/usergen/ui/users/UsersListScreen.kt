package com.apps.usergen.ui.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apps.usergen.data.User
import com.apps.usergen.viewmodel.UserListViewModel
import com.library.paging.LazyPagingItems
import com.library.paging.collectAsLazyPagingItems
import com.library.paging.items

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
        items(users, key = { it.userId }, contentType = { "UserCard" }) { user ->
            UserCard(
                user = user,
                onClick = { onUserClick(user) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}