package com.apps.usergen.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.apps.database.data.UserEntity
import com.apps.usergen.data.User
import com.apps.usergen.ui.util.toFullname



@Composable
internal fun UserCard(
    user: User,
    onClick: (User) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier,
        onClick = { onClick(user) },
    ) {
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
                    .size(80.dp),
                contentAlignment = Alignment.Center,
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.matchParentSize(),
                    model = user.picture.medium,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    loading = {},
                    error = { PlaceholderImage(Modifier.matchParentSize()) }
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = user.name.toFullname(),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.height(8.dp))
                DisplayAddress(
                    location = user.location,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

private const val PlaceIconId = "PlaceIconId"

@Composable
internal fun DisplayAddress(
    location: User.Location,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            appendInlineContent(PlaceIconId, "Place Icon")
            append(" ")
            append("${location.street.number} ${location.street.name}, ${location.city}, ${location.state} ${location.postcode}")
        },
        inlineContent = mapOf(
            Pair(
                PlaceIconId,
                InlineTextContent(
                    Placeholder(
                        width = 16.sp,
                        height = 16.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                    )
                }
            )
        ),
        textAlign = textAlign,
        style = style,
    )
}

@Composable
internal fun PlaceholderImage(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.Image,
            contentDescription = null,
        )
    }
}