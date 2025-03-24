package com.example.myapplication.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.res.stringResource
import com.example.notepadmvvm.R
import com.example.notepadmvvm.mainScreen.model.PersonDetailEntity
import com.example.notepadmvvm.mainScreen.viewModel.PersonViewModel
import java.util.Calendar
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notepadmvvm.AddAndEditScreen.view.DatePickerModal

@SuppressLint("SimpleDateFormat", "ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlan(navController: NavController, id: Long) {
    // Fetching person details
    val personDetail = remember { mutableStateOf<PersonDetailEntity?>(null) }

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var selectedOption by remember { mutableStateOf("Normal") } // Varsayılan değer "Normal"

    var isNameError by remember { mutableStateOf(false) }
    var isSurnameError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var updateCompleted by remember { mutableStateOf(false) }

    var color by remember { mutableStateOf("") }

    val personViewModel: PersonViewModel = viewModel()


    // Importance colors for different levels
    val importanceColors = listOf(
        Color.Red to stringResource(R.string.critical),
        Color(0xFFFFA500) to stringResource(R.string.important),
        Color.Yellow to stringResource(R.string.normal)
    )

    // Başlangıç rengi olarak güncelleme yapılmışsa seçilen rengi kullanın
    // Başlangıç rengi olarak güncelleme yapılmışsa seçilen rengi kullanın
    val selectedColor = remember {
        mutableStateOf(
            personDetail.value?.let {
                when (it.selectedOption11) {
                    "critical" -> Color.Red
                    "important" -> Color(0xFFFFA500)
                    else -> Color.Yellow
                }
            } ?: Color.Yellow
        )
    }

    var expanded by remember { mutableStateOf(false) } // Dropdown menu for importance selection

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (id == -1L) stringResource(R.string.plan_add) else stringResource(R.string.plan_editing),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(68.dp)) // Adjust width based on your needs
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    isNameError = name.isEmpty()
                    isSurnameError = surname.isEmpty()

                    if (!isNameError && !isSurnameError) {
                        val currentDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
                        val finalDate =
                            selectedDate ?: SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                .format(Calendar.getInstance().apply { add(Calendar.YEAR, 1) }.time)

                        if (id == -1L) {
                            personViewModel.insertDate(
                                name,
                                surname,
                                currentDate,
                                finalDate,
                                selectedOption.ifEmpty { "Normal" })
                            navController.navigate("save_page") {
                                popUpTo(R.string.edit_plan_id) { inclusive = true }
                            }
                        } else {
                            CoroutineScope(Dispatchers.IO).launch {
                                val realm = Realm.getDefaultInstance()
                                realm.executeTransaction { r ->
                                    val personToUpdate = r.where(PersonDetailEntity::class.java)
                                        .equalTo("_id", id)
                                        .findFirst()
                                    personToUpdate?.apply {
                                        namee = name
                                        surnamee = surname
                                        selectedOption11 = selectedOption
                                        selectedOptionn = finalDate
                                    }
                                    color = selectedOption
                                }
                                realm.close()
                                updateCompleted = true
                            }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.update),
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { innerPadding ->
            LaunchedEffect(id) {
                // personDetail güncelleme
                CoroutineScope(Dispatchers.IO).launch {
                    val realm = Realm.getDefaultInstance()
                    val person = realm.where(PersonDetailEntity::class.java)
                        .equalTo("_id", id)
                        .findFirst()
                    person?.let {
                        personDetail.value = realm.copyFromRealm(it)
                        name = it.namee ?: ""
                        surname = it.surnamee ?: ""
                        selectedOption = it.selectedOption11 ?: ""
                        selectedDate = it.selectedOptionn

                        // Update color based on fetched importance
                        selectedColor.value = when (it.selectedOption11) {
                            "Critical" -> Color.Red
                            "Important" -> Color(0xFFFFA500)
                            else -> Color.Yellow
                        }
                    }
                    realm.close()
                }
            }

            // Handle navigation when update is completed
            LaunchedEffect(updateCompleted) {
                if (updateCompleted) {
                    navController.popBackStack()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Name TextField
                TextField(
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(
                            text = stringResource(R.string.enter_title),
                            color = if (isNameError) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    isError = isNameError,
                    singleLine = false,
                    maxLines = 2,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                )

                // Surname TextField
                TextField(
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    value = surname,
                    onValueChange = { surname = it },
                    label = {
                        Text(
                            text = stringResource(R.string.enter_content),
                            color = if (isSurnameError) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    isError = isSurnameError,
                    modifier = Modifier
                        .weight(10f)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )

                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // IconButton with selected color
                    IconButton(onClick = { expanded = !expanded }) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(selectedColor.value, CircleShape)
                        )
                    }


                    // Dropdown Menu for Importance Color Selection
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        importanceColors.forEach { (color, label) ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedColor.value = color
                                    selectedOption = label
                                    expanded = false
                                },
                                text = {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(color, CircleShape)
                                    )
                                }
                            )
                        }
                    }

                    // DatePicker Button
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = stringResource(R.string.select_date),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    selectedDate?.let {
                        Text(
                            text = stringResource(R.string.selected_date, it),
                            modifier = Modifier.padding(16.dp)
                        )
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
        }
    )
}