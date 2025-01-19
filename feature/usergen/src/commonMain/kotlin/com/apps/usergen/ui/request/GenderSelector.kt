package com.apps.usergen.ui.request

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material.icons.rounded.Transgender
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.apps.usergen.Res
import com.apps.model.Gender
import com.apps.usergen.gender_any
import com.apps.usergen.gender_female
import com.apps.usergen.gender_male
import com.apps.usergen.select_gender
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val genderOptions: Map<StringResource, Gender?> = mapOf(
    Res.string.gender_any to null,
    Res.string.gender_male to Gender.Male,
    Res.string.gender_female to Gender.Female,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun GenderSelector(
    selectedGender: Gender? = null,
    onGenderSelected: (Gender?) -> Unit,
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
            value = when (selectedGender) {
                null -> stringResource(Res.string.gender_any)
                Gender.Male -> stringResource(Res.string.gender_male)
                Gender.Female -> stringResource(Res.string.gender_female)
            },
            singleLine = true,
            onValueChange = {},
            label = { Text(stringResource(Res.string.select_gender)) },
            prefix = { GenderIcon(selectedGender) },
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
            genderOptions.forEach { genderSelection ->
                DropdownMenuItem(
                    onClick = {
                        onGenderSelected(genderSelection.value)
                        expanded = false
                    },
                    leadingIcon = { GenderIcon(genderSelection.value) },
                    text = { Text(stringResource(genderSelection.key)) }
                )
            }
        }
    }
}

@Composable
private fun GenderIcon(
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