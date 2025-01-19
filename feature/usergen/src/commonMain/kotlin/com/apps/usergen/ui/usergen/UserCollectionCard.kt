package com.apps.usergen.ui.usergen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.apps.usergen.Res
import com.apps.usergen.data.UserCollection
import com.apps.usergen.users
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserCollectionCard(
    onClick: (UserCollection) -> Unit,
    collection: UserCollection,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = { onClick(collection) },
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = collection.name.ifEmpty { collection.id },
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = buildAnnotatedString {
                    append("${collection.count}")
                    append(" ")
                    append(stringResource(Res.string.users))
                },
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}