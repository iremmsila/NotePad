package com.example.myapplication

import DatePickerModal
import androidx.benchmark.perfetto.ExperimentalPerfettoTraceProcessorApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPerfettoTraceProcessorApi::class)
@Composable
fun EditPlan(navController: NavController, id: Long) {
    val personDetail = remember { mutableStateOf<PersonDetailEntity?>(null) }

    // State for updated values
    val updatedName = remember { mutableStateOf("") }
    val updatedSurname = remember { mutableStateOf("") }
    val updatedImportance = remember { mutableStateOf("") }
    val updatedDate = remember { mutableStateOf<String?>(null) }

    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isUpdating by remember { mutableStateOf(false) } // New state to track update status
    var updateCompleted by remember { mutableStateOf(false) } // State to track when the update is done

    // Importance levels mapped to colors
    val importanceColors = listOf(
        Color.Red to "Critical",
        Color(0xFFFFA500) to "Important", // Orange
        Color.Yellow to "Normal"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.plan_editing),
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSurface, // Arka plan rengi
                    titleContentColor = MaterialTheme.colorScheme.surface // Başlık rengi
                )
            )
        },
        floatingActionButton = {
            // FAB oluşturma
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    isUpdating = true // Güncellemenin devam ettiğini işaretle
                    CoroutineScope(Dispatchers.IO).launch {
                        val realm = Realm.getDefaultInstance()
                        realm.executeTransaction { r ->
                            val personToUpdate = r.where(PersonDetailEntity::class.java)
                                .equalTo("_id", id)
                                .findFirst()
                            personToUpdate?.namee = updatedName.value
                            personToUpdate?.surnamee = updatedSurname.value
                            personToUpdate?.selectedOption11 = updatedImportance.value
                            personToUpdate?.selectedOptionn = updatedDate.value
                        }
                        realm.close()
                        isUpdating = false // Güncelleme tamamlandı
                        updateCompleted = true // Güncellemenin tamamlandığını işaretle
                    }
                },
                // Eğer isUpdating ile devre dışı bırakmak istemiyorsanız bu satırı kaldırabilirsiniz
                // enabled = !isUpdating,
                containerColor = MaterialTheme.colorScheme.primary // FAB arka plan rengi
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Update") // Kaydetme ikonu
            }


        },
        containerColor = MaterialTheme.colorScheme.onBackground,
        content = { innerPadding ->
            // Fetch data from Realm
            LaunchedEffect(id) {
                CoroutineScope(Dispatchers.IO).launch {
                    val realm = Realm.getDefaultInstance()
                    val person = realm.where(PersonDetailEntity::class.java)
                        .equalTo("_id", id)
                        .findFirst()

                    person?.let {
                        personDetail.value = realm.copyFromRealm(it)
                        updatedName.value = it.namee ?: ""
                        updatedSurname.value = it.surnamee ?: ""
                        updatedImportance.value = it.selectedOption11 ?: ""
                        updatedDate.value = it.selectedOptionn
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

            personDetail.value?.let { person ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(Modifier.height(16.dp))
                    // TextField for Name
                    TextField(
                        textStyle = TextStyle(color = Color.White),
                        value = updatedName.value,
                        onValueChange = { updatedName.value = it },
                        label = { Text("Name") },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.onBackground,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

                    // TextField for Surname
                    TextField(
                        textStyle = TextStyle(color = Color.White),
                        value = updatedSurname.value,
                        onValueChange = { updatedSurname.value = it },
                        label = { Text("Surname") },
                        modifier = Modifier
                            .weight(10f)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.onBackground,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    )
                    {

                        IconButton(onClick = { expanded = !expanded }) {
                            // Seçilen önem derecesine göre renkli ikon göster
                            val selectedColor = importanceColors.firstOrNull { it.second == updatedImportance.value }?.first ?: Color.Gray
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(selectedColor, CircleShape) // Seçili renge göre arkaplan renkli ikon
                            )
                        }

                         // Dropdown menü
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            importanceColors.forEach { (color, importanceValue) ->
                                DropdownMenuItem(
                                    onClick = {
                                        updatedImportance.value = importanceValue // Seçilen renk kaydediliyor
                                        expanded = false
                                    },
                                    text = {
                                        // Renkli daire ikonları göstermek için Box kullanıyoruz
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .background(color, CircleShape)
                                        )
                                    }
                                )
                            }
                        }


                        // DatePicker button
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date") // Takvim ikonu
                        }

                        updatedDate.value?.let {
                            Text(text = "Selected Date: $it", modifier = Modifier.padding(16.dp))
                        }

                        if (showDatePicker) {
                            DatePickerModal(
                                onDateSelected = { dateInMillis ->
                                    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                    updatedDate.value = dateInMillis?.let { sdf.format(Date(it)) }
                                    showDatePicker = false
                                },
                                onDismiss = { showDatePicker = false }
                            )
                        }
                    }

                }
            }
        }
    )
}
