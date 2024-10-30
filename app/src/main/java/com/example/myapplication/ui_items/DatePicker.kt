import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val currentDateInMillis = System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDateInMillis // Set default selected date to today
    )

    val isPastDateSelected = datePickerState.selectedDateMillis?.let { it < currentDateInMillis } ?: false

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                },
                enabled = !isPastDateSelected // Disable button if past date is selected
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }

    if (isPastDateSelected) {
        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.please_select_a_date_from_today_onward),
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerModalPreview() {
    DatePickerModalExample()
}

@Composable
fun DatePickerModalExample() {
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = { showDatePicker = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.select_date),
                    modifier = Modifier.size(24.dp)
                )
                Text(stringResource(R.string.select_date), modifier = Modifier.padding(start = 8.dp))
            }
        }

        selectedDate?.let {
            Text(text = stringResource(R.string.selected_date, it), modifier = Modifier.padding(16.dp))
        }

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { dateInMillis ->
                    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    selectedDate = dateInMillis?.let { sdf.format(Date(it)) }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}
