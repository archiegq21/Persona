package com.apps.usergen.ui.request

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apps.usergen.Res
import com.apps.model.Gender
import com.apps.usergen.generate
import com.apps.usergen.request_form_title
import com.apps.usergen.user_count
import com.apps.usergen.zero_count
import com.apps.usergen.invalid_count
import com.apps.usergen.viewmodel.GenUserParams
import com.apps.usergen.viewmodel.UserGenRequestViewModel
import com.apps.usergen.viewmodel.UserGenUiState.ValidatedCount
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserGenRequestRoute(
    onClose: () -> Unit,
    generateUsers: (GenUserParams) -> Unit,
    viewModel: UserGenRequestViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ElevatedCard(
        modifier = modifier,
    ) {
        UserGenListForm(
            onClose = onClose,
            count = uiState.count,
            onCountChanged = viewModel::setCount,
            validatedCount = uiState.validatedCount,
            onCountFocused = viewModel::onCountFocused,
            gender = uiState.gender,
            onGenderChanged = viewModel::setGender,
            enabled = uiState.isFormValid,
            generateUserParams = uiState.generateUserParams,
            onGenerateUserClick = viewModel::onGenerateUser,
            consumeGenerateUserParams = viewModel::consumeGenerateUserParams,
            generateUsers = generateUsers,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun UserGenListForm(
    onClose: () -> Unit,
    generateUsers: (GenUserParams) -> Unit,
    count: String,
    onCountChanged: (String) -> Unit,
    validatedCount: ValidatedCount?,
    onCountFocused: () -> Unit,
    gender: Gender?,
    onGenderChanged: (Gender?) -> Unit,
    enabled: Boolean = true,
    generateUserParams: GenUserParams?,
    onGenerateUserClick: (
        validatedCount: ValidatedCount?,
        gender: Gender?,
    ) -> Unit,
    consumeGenerateUserParams: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (generateUserParams != null) {
        LaunchedEffect(generateUserParams) {
            generateUsers(generateUserParams)
            consumeGenerateUserParams()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.request_form_title),
                style = MaterialTheme.typography.headlineLarge,
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.Top),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    contentDescription = null,
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TextField(
                value = count,
                onValueChange = onCountChanged,
                label = { Text(stringResource(Res.string.user_count)) },
                prefix = {
                    Icon(
                        imageVector = Icons.Rounded.Tag,
                        contentDescription = null,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                isError = validatedCount != null && validatedCount.isError,
                supportingText = {
                    when (validatedCount) {
                        ValidatedCount.Invalid -> Text(stringResource(Res.string.invalid_count))
                        ValidatedCount.Zero -> Text(stringResource(Res.string.zero_count))
                        is ValidatedCount.Valid, null -> {}
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) onCountFocused()
                    },
            )
            GenderSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedGender = gender,
                onGenderSelected = onGenderChanged,
            )
        }
        Button(
            enabled = enabled,
            onClick = {
                onGenerateUserClick(
                    validatedCount,
                    gender,
                )
            },
            modifier = Modifier.align(Alignment.End),
        ) {
            Text(stringResource(Res.string.generate))
        }
    }
}