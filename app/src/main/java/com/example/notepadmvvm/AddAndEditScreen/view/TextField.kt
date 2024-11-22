package com.example.notepadmvvm.AddAndEditScreen.view


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.notepadmvvm.AddAndEditScreen.viewModel.GenericTextFieldViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTextField(
    viewModel: GenericTextFieldViewModel,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    val text by viewModel.text.collectAsState()
    val isError by viewModel.isError.collectAsState()

    TextField(
        value = text,
        onValueChange = { viewModel.onTextChange(it) },
        label = {
            Text(
                text = label,
                color = if (isError) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        },
        isError = isError,
        singleLine = singleLine,
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}
