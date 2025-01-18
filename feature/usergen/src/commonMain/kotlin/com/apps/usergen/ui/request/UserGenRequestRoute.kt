package com.apps.usergen.ui.request

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.Transgender
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import com.apps.usergen.Res
import com.apps.usergen.data.Gender
import com.apps.usergen.data.Nationality
import com.apps.usergen.user_count
import com.apps.usergen.request_form_title

@Composable
fun UserGenRequestRoute(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
    ) {
        UserGenListForm(
            onGenerateUser = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun UserGenListForm(
    onGenerateUser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var size by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf<Gender?>(null) }
    var selectedNationality by remember { mutableStateOf<Nationality?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(Res.string.request_form_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TextField(
                value = size,
                onValueChange = { size = it },
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
                modifier = Modifier.fillMaxWidth(),
            )
            GenderSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedGender = selectedGender,
                onGenderSelected = { selectedGender = it },
            )
        }
    }
}