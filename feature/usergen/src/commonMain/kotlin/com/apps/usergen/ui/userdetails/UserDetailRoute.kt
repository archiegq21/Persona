package com.apps.usergen.ui.userdetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.apps.model.Gender
import com.apps.usergen.Res
import com.apps.usergen.data.User
import com.apps.usergen.gender_female
import com.apps.usergen.gender_male
import com.apps.usergen.md5
import com.apps.usergen.password
import com.apps.usergen.salt
import com.apps.usergen.sha1
import com.apps.usergen.sha256
import com.apps.usergen.ui.users.DisplayAddress
import com.apps.usergen.ui.users.PlaceholderImage
import com.apps.usergen.ui.util.toFullname
import com.apps.usergen.username
import com.apps.usergen.uuid
import com.apps.usergen.viewmodel.UserDetailViewModel
import com.apps.usergen.viewmodel.UserState
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserDetailRoute(
    userId: String,
    viewModel: UserDetailViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val userState by remember(userId) {
        viewModel.getUser(userId)
    }.collectAsStateWithLifecycle()

    UserDetailScreen(
        userState = userState,
        modifier = modifier,
    )
}

@Composable
private fun UserDetailScreen(
    userState: UserState,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = userState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.fillMaxSize(),
    ) { state ->
        when (state) {
            UserState.Loading -> { Box(Modifier.fillMaxSize()) }
            is UserState.Found -> FoundUserDetailsScreen(user = state.user)
        }
    }
}

@Composable
private fun FoundUserDetailsScreen(
    user: User,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        userDetails(
            name = user.name,
            picture = user.picture,
            gender = user.gender,
            location = user.location,
            email = user.email,
            dob = user.dob,
        )
        loginDetails(
            login = user.login,
        )
    }
}

private fun LazyListScope.userDetails(
    name: User.Name,
    picture: User.Picture,
    gender: Gender,
    location: User.Location,
    email: String,
    dob: User.Dob,
) {
    item {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Box(Modifier.align(Alignment.CenterHorizontally)) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .size(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    SubcomposeAsyncImage(
                        modifier = Modifier.matchParentSize(),
                        model = picture.medium,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        loading = {},
                        error = { PlaceholderImage(Modifier.matchParentSize()) }
                    )
                }
                GenderCircle(
                    gender = gender,
                    modifier = Modifier.align(Alignment.BottomEnd),
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = name.toFullname(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = dob.date.toLocalDateTime(TimeZone.currentSystemDefault())
                    .format(LocalDateTime.Format {
                        dayOfMonth()
                        char(' ')
                        monthName(MonthNames.ENGLISH_FULL)
                        char(' ')
                        year()
                    }),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = email,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(8.dp))
            DisplayAddress(
                modifier = Modifier
                    .fillMaxSize(0.7f)
                    .align(Alignment.CenterHorizontally),
                location = location,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

fun LazyListScope.loginDetails(
    login: User.Login,
) {
    divider()
    item {
        DetailRow(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.uuid)) },
            value = { Text(login.uuid) },
        )
    }
    item {
        DetailRow(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.username)) },
            value = { Text(login.username) },
        )
    }
    item {
        DetailRow(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.password)) },
            value = { Text(login.password) },
        )
    }
    item {
        DetailRow(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.salt)) },
            value = { Text(login.salt) },
        )
    }
    item {
        DetailRow(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.md5)) },
            value = { Text(login.md5) },
        )
    }
    item {
        DetailRow(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.sha1)) },
            value = { Text(login.sha1) },
        )
    }
    item {
        DetailRow(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.sha256)) },
            value = { Text(login.sha256) },
        )
    }
    divider()
}

fun LazyListScope.divider() = item(contentType = "Divider") {
    HorizontalDivider(
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 4.dp,
        )
    )
}

@Composable
internal fun DetailRow(
    label: @Composable () -> Unit,
    value: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 4.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(Modifier.weight(0.3f)) {
            ProvideTextStyle(MaterialTheme.typography.titleSmall) {
                label()
            }
        }
        Box(Modifier.weight(0.7f)) {
            ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                value()
            }
        }
    }
}

@Composable
internal fun GenderCircle(
    gender: Gender,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.offset {
            IntOffset(
                x = (8).dp.roundToPx(),
                y = (8).dp.roundToPx()
            )
        },
        shape = CircleShape,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Icon(
            imageVector = when (gender) {
                Gender.Male -> Icons.Rounded.Male
                Gender.Female -> Icons.Rounded.Female
            },
            contentDescription = when (gender) {
                Gender.Male -> stringResource(Res.string.gender_male)
                Gender.Female -> stringResource(Res.string.gender_female)
            },
            modifier = Modifier.padding(8.dp),
        )
    }
}