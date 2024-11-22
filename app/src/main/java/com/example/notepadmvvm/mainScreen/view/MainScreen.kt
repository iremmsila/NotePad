package com.example.notepadmvvm.mainScreen.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notepadmvvm.R
import com.example.notepadmvvm.mainScreen.model.PersonDetailEntity
import com.example.notepadmvvm.mainScreen.viewModel.PersonViewModel
import com.example.notepadmvvm.mainScreen.viewModel.SwipeToDismissViewModel
import com.example.notepadmvvm.mainScreen.viewModel.Utils.calculateDaysBetween
import com.example.notepadmvvm.ui.theme.Critical
import com.example.notepadmvvm.ui.theme.Important
import com.example.notepadmvvm.ui.theme.Normal
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    personViewModel: PersonViewModel = viewModel() // Obtain ViewModel in the Composable
) {
    val allData = remember { mutableStateOf<List<PersonDetailEntity>>(emptyList()) }
    val personList = remember { mutableStateOf<List<PersonDetailEntity>>(emptyList()) }
    val searchQuery = remember { mutableStateOf("") }
    val selectedFilter = remember { mutableStateOf("All") }

    // Fetch all data
    LaunchedEffect(Unit) {
        personViewModel.getAllData { data ->
            val sortedData = data.sortedBy { person ->
                when (person.selectedOption11) {
                    "Critical" -> 1
                    "Important" -> 2
                    "Normal" -> 3
                    else -> 4
                }
            }
            allData.value = sortedData
            personList.value = sortedData
        }
    }

    // Filter data based on search query
    LaunchedEffect(searchQuery.value) {
        if (searchQuery.value.isNotEmpty()) {
            val searchResults = allData.value.filter { person ->
                person.namee?.contains(searchQuery.value, ignoreCase = true) ?: false
            }
            personList.value = searchResults
        } else {
            personList.value = allData.value
        }
    }


    var selectedDayFilter by remember { mutableStateOf(3) } // Varsayılan olarak 3 gün seçili
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var customDayValue by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.my_notes),
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        expanded = !expanded
                    }) { // Toggle dropdown menu visibility
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = stringResource(R.string.days),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    CustomDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded= false },
                        selectedDayFilter = selectedDayFilter,
                        onDayFilterSelected = { selectedDay ->
                            println("Selected Day Filter: $selectedDay")
                        },
                        customDayValue = customDayValue,
                        showDialog = showDialog
                    )

                },
                actions = {
                    IconButton(onClick = {personViewModel.startIntroScreensActivity(context) }) {
                        Icon(
                            imageVector = Icons.Outlined.Info, // Use any icon of your choice
                            contentDescription = stringResource(R.string.intro),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.surface
                )
            )
        },

        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.tertiary,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    personViewModel.editItem((-1).toString(), navController) {
                        personViewModel.getAllData { updatedList ->
                            personList.value = updatedList
                        }
                    }
                },
                Modifier.size(56.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.tertiary //gray
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add),
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Filter the list based on the selected filter for priority
            val filteredList = personList.value.filter { person ->
                when (selectedFilter.value) {
                    "Critical" -> person.selectedOption11 == "Critical"
                    "Important" -> person.selectedOption11 == "Important"
                    "Normal" -> person.selectedOption11 == "Normal"
                    else -> true // If 'All' is selected, show all notes
                }
            }

            // Gün farkına göre listeyi filtrele
            val filteredListRow = personList.value.filter { person ->
                val targetDateString = person.selectedOptionn // Hedef tarih
                val daysLeft = targetDateString?.let { calculateDaysBetween(it) }

                // Seçilen gün filtresine göre filtreleme yap
                daysLeft != null && daysLeft <= selectedDayFilter
            }

            Text(
                stringResource(R.string.those_whose_day_is_approaching),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 15.sp
                ),
                modifier = Modifier.padding(16.dp)
            )


            //  LazyRow for showingya filtered items horizontally
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(screenWidth * 0.02f)
            ) {
                if (filteredListRow.isEmpty()) {
                    // Show a placeholder if the filteredList is empty
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_items_available),
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 18.sp
                            )
                        }

                    }
                } else {
                    // Display filtered items in LazyRow
                    items(filteredListRow) { person ->
                        val cardBackgroundColor = when (person.selectedOption11) {
                            "Critical" -> Color(Critical.value)
                            "Important" -> Color(Important.value)
                            "Normal" -> Color(Normal.value)
                            else -> MaterialTheme.colorScheme.onSurface
                        }

                        val daysLeft = calculateDaysBetween(
                            person.selectedOptionn ?: ""
                        ) // Gün sayısını hesapla


                        EditableCard(
                            person = person,
                            navController = navController,
                            cardBackgroundColor = cardBackgroundColor,
                            screenHeight = screenHeight,
                            screenWidth = screenWidth,
                            daysLeft = calculateDaysBetween(person.selectedOptionn ?: "") // Gün farkını hesaplayan bir fonksiyon
                        )
                    }
                }
            }


// Dialog that opens when the button is clicked
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = stringResource(R.string.enter_custom_days)) },
                    text = {
                        TextField(
                            value = customDayValue,
                            onValueChange = { customDayValue = it },
                            label = { Text(stringResource(R.string.days)) },
                            placeholder = { Text(stringResource(R.string.enter_number_of_days)) }
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val days = customDayValue.toIntOrNull()
                                if (days != null && days > 0) {
                                    selectedDayFilter = days
                                }
                                showDialog = false
                                expanded = false// Close the dialog after setting the value
                            }
                        ) {
                            Text(stringResource(R.string.confirm))
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp,
                // modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                //  modifier = Modifier.padding(vertical = 8.dp)
            )


            // Search bar
            SearchTextField(
                searchQuery = searchQuery,
                placeholderText = stringResource(R.string.search_for_notes),
                onSearch = { query ->
                    // Arama sorgusu güncellendiğinde yapılacak işlemler
                    println("Arama Sorgusu: $query")
                }
            )


            val filters = listOf("All", "Critical", "Important", "Normal")
           // val selectedFilter = remember { mutableStateOf("All") }

            FilterButtons(
                filters = filters,
                selectedFilter = selectedFilter,
                onFilterSelected = { filter ->
                    // Filtre seçildiğinde yapılacak işlemler
                    println("Selected Filter: $filter")
                },
                criticalColor = Color.Red,
                importantColor = Color(0xFFFF7F00),
                normalColor = Color.Yellow
            )


            Divider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)//.padding(start = 16.dp, end = 16.dp)
            )




            val sortedFilteredList = filteredList.sortedBy { person ->
                calculateDaysBetween(person.selectedOptionn ?: "")
            }

            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            var personListCopy: List<PersonDetailEntity> = emptyList()


            //!!!!
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.padding(16.dp),
                        snackbar = { snackbarData ->
                            Snackbar(
                                snackbarData = snackbarData,
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .padding(horizontal = 8.dp),
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        },
                    )
                },
                containerColor = MaterialTheme.colorScheme.background,
            ) { paddingValues ->
                // LazyColumn for showing filtered items vertically
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues), //
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = sortedFilteredList,
                        key = { it._id!! }
                    ) { person ->
                        SwipeToDismissItem(
                            item = person,
                            onRemove = { id ->
                                personViewModel.deleteItemById(id.toLong()) {
                                    personViewModel.getAllData { updatedList ->
                                        personList.value = updatedList
                                    }
                                }


                                // Snackbar gösterimi
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        personViewModel.restoreItem(listOf(person)) {

                                            personViewModel.getAllData { data ->
                                                val sortedData = data.sortedBy { person ->
                                                    when (person.selectedOption11) {
                                                        "Critical" -> 1
                                                        "Important" -> 2
                                                        "Normal" -> 3
                                                        else -> 4
                                                    }
                                                }
                                                allData.value = sortedData
                                                personList.value = sortedData
                                            }
                                        }
                                    }
                                }
                            },

                            onEdit = { id ->
                                personViewModel.editItem(id, navController) {
                                    personViewModel.getAllData { updatedList ->
                                        personList.value = updatedList
                                    }
                                }
                            },
                            onRestore = { restoredPerson ->
                                // Restore an item if needed
                                restoredPerson._id?.let {
                                    personViewModel.restoreItem(listOf(restoredPerson)) {
                                        personViewModel.getAllData { updatedList ->
                                            personList.value = updatedList
                                        }
                                    }
                                }
                            },
                            snackbarHostState = snackbarHostState,// SnackbarHostState'i parametre olarak geçir
                            modifier = Modifier.animateItemPlacement(tween(400)),
                            viewModel = SwipeToDismissViewModel(),
                            ) {
                            val cardBackgroundColor = when (person.selectedOption11) {
                                "Critical" -> Color(Critical.value)
                                "Important" -> Color(Important.value)
                                "Normal" -> Color(Normal.value)
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                            val daysLeft = calculateDaysBetween(
                                person.selectedOptionn ?: ""
                            ) // Gün sayısını hesapla


                            PersonCard(
                                person = person,
                                daysLeft = daysLeft,
                                cardBackgroundColor = cardBackgroundColor
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(48.dp)) // Adjust the height as needed
                    }
                }
            }
        }
    }
}
