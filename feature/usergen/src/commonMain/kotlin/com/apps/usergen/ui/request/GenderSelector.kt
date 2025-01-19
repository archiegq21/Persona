package com.apps.usergen.ui.request

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material.icons.rounded.Transgender
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apps.model.Gender


@Composable
internal fun GenderIcon(
    gender: Gender?,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier.size(24.dp),
        imageVector = when (gender) {
            null -> Icons.Rounded.Transgender
            Gender.Male -> Icons.Rounded.Male
            Gender.Female -> Icons.Rounded.Female
        },
        contentDescription = null,
    )
}