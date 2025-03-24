//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.outlined.Info
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.sp
//import com.example.notepadmvvm.R
//import com.example.notepadmvvm.mainScreen.view.CustomDropdownMenu
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainTopBar(
//    expanded: Boolean,
//    selectedDayFilter: Int,
//    showDialog: Boolean,
//    customDayValue: String,
//    onCustomDayChange: (String) -> Unit,
//    onCustomDayConfirm: (Int) -> Unit,
//    onIntroClick: () -> Unit
//) {
//    TopAppBar(
//        title = {
//            Text(
//                text = stringResource(R.string.my_notes),
//                fontSize = 20.sp,
//                textAlign = TextAlign.Center,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//        },
//        navigationIcon = {
//            IconButton(onClick = { /* Handle dropdown toggle */ }) {
//                Icon(
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = stringResource(R.string.days),
//                    tint = MaterialTheme.colorScheme.onSurface
//                )
//            }
//
//            // Custom Dropdown Menu
//            if (expanded) {
//                CustomDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { /* Handle dismiss */ },
//                    selectedDayFilter = selectedDayFilter,
//                    onDayFilterSelected = { /* Update selected filter */ },
//                    customDayValue = customDayValue,
//                    showDialog = showDialog
//                )
//            }
//        },
//        actions = {
//            IconButton(onClick = { onIntroClick() }) {
//                Icon(
//                    imageVector = Icons.Outlined.Info,
//                    contentDescription = stringResource(R.string.intro),
//                    tint = MaterialTheme.colorScheme.onSurface
//                )
//            }
//        }
//    )
//}
