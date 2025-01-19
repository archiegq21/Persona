package com.apps.usergen.ui.users

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apps.usergen.data.User

@Composable
internal fun UserListRoute(
    onBack: () -> Unit,
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
    ) {
        items(1000) {
//            UserCard(
//                onClick = {
////                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, "Item $it")
//                }
//            )
        }
    }
}