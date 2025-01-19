package com.apps.usergen.ui.usergen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apps.model.Gender
import com.apps.usergen.Res
import com.apps.usergen.data.UserCollection
import com.apps.usergen.gender_any
import com.apps.usergen.gender_female
import com.apps.usergen.gender_male
import com.apps.usergen.ui.request.GenderIcon
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
                text = collection.id,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(16.dp))
            GenderChip(
                gender = collection.gender
            )
        }
    }
}

@Composable
internal fun GenderChip(
    gender: Gender?,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GenderIcon(
                gender = gender,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = when (gender) {
                    null -> stringResource(Res.string.gender_any)
                    Gender.Male -> stringResource(Res.string.gender_male)
                    Gender.Female -> stringResource(Res.string.gender_female)
                },
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}