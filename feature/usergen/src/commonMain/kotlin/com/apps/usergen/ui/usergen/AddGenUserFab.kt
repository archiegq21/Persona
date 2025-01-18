package com.apps.usergen.ui.usergen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import com.apps.usergen.Res
import com.apps.usergen.add_generated_user
import org.jetbrains.compose.resources.stringResource

private val FabTransformOrigin = TransformOrigin(1f, 0.5f)

@Composable
internal fun AddGenUserFab(
    onClick: () -> Unit,
    visible: Boolean = true,
    expanded: Boolean = true,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(transformOrigin = FabTransformOrigin) + fadeIn(),
        exit = fadeOut() + scaleOut(transformOrigin = FabTransformOrigin),
    ) {
        ExtendedFloatingActionButton(
            modifier = modifier,
            expanded = expanded,
            onClick = { onClick() },
            icon = { Icon(Icons.Rounded.PersonAdd, null) },
            text = { Text(stringResource(Res.string.add_generated_user)) },
        )
    }
}