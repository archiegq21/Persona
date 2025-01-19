package com.apps.usergen.ui.request

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.apps.usergen.Res
import com.apps.usergen.data.Nationality
import org.jetbrains.compose.resources.stringResource
import com.apps.usergen.nationality_any
import com.apps.usergen.select_nationality

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
internal fun NationalitySelector(
    selectedNationality: Nationality? = null,
    onNationalitySelected: (Nationality?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded  },
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            readOnly = true,
            value = selectedNationality?.name ?: stringResource(Res.string.nationality_any),
            singleLine = true,
            onValueChange = {},
            label = { Text(stringResource(Res.string.select_nationality)) },
            prefix = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    onNationalitySelected(null)
                    expanded = false
                },
                leadingIcon = {},
                text = { Text(stringResource(Res.string.nationality_any)) }
            )
            Nationality.entries.forEach { nationality ->
                DropdownMenuItem(
                    onClick = {
                        onNationalitySelected(nationality)
                        expanded = false
                    },
                    leadingIcon = {
                        // TODO: Add flag icon
                    },
                    text = { Text(nationality.name) }
                )
            }
        }
    }
}