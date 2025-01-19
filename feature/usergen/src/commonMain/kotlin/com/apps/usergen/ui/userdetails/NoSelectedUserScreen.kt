package com.apps.usergen.ui.userdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apps.usergen.Res
import com.apps.usergen.select_user
import com.apps.usergen.ui.assets.ErrorHero
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun NoSelectedUserScreen(
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
                imageVector = ErrorHero,
                contentDescription = null,
            )
            Text(
                text = stringResource(Res.string.select_user),
                modifier = Modifier.widthIn(200.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}