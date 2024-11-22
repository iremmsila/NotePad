package com.example.notepadmvvm.mainScreen.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notepadmvvm.R
import androidx.compose.runtime.*

@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    selectedDayFilter: Int,
    onDayFilterSelected: (Int) -> Unit,
    customDayValue: String,
    showDialog: Boolean,
    modifier: Modifier = Modifier
) {
    // Bu state'ler parent composable tarafından sağlanmalıdır
    var showDialogState by remember { mutableStateOf(showDialog) }
    var customDayValueState by remember { mutableStateOf(customDayValue) }
    var selectedDayFilterState by remember { mutableStateOf(selectedDayFilter) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        val predefinedOptions = listOf(3, 5, 7)

        predefinedOptions.forEach { day ->
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedDayFilterState == day),
                            onClick = {
                                selectedDayFilterState = day
                                onDayFilterSelected(day)
                                onDismissRequest()
                            }
                        )
                        Text(
                            text = stringResource(id = R.string.day_option, day),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                onClick = {
                    selectedDayFilterState = day
                    onDayFilterSelected(day)
                    onDismissRequest()
                }
            )
        }

        DropdownMenuItem(
            text = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { showDialogState = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.custom),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }

                    if (showDialogState) {
                        AlertDialog(
                            onDismissRequest = { showDialogState = false },
                            title = { Text(text = stringResource(R.string.enter_custom_day)) },
                            text = {
                                Column {
                                    TextField(
                                        value = customDayValueState,
                                        onValueChange = { value ->
                                            if (value.all { it.isDigit() }) {
                                                customDayValueState = value
                                            }
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        label = { Text(text = stringResource(R.string.custom_day)) }
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        val customValue = customDayValueState.toIntOrNull() ?: 0
                                        selectedDayFilterState = customValue
                                        onDayFilterSelected(customValue)
                                        showDialogState = false
                                        onDismissRequest()
                                    }
                                ) {
                                    Text(stringResource(R.string.ok))
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { showDialogState = false }
                                ) {
                                    Text(stringResource(R.string.cancel))
                                }
                            }
                        )
                    }
                }
            },
            onClick = {
                val customValue = customDayValueState.toIntOrNull() ?: 0
                selectedDayFilterState = customValue
                onDayFilterSelected(customValue)
                onDismissRequest()
            }
        )
    }
}
