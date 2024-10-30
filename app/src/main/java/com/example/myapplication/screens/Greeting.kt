package com.example.myapplication.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.db.MainActivity
import com.example.myapplication.helper.PersonDetailEntity
import com.example.myapplication.ui.theme.Critical
import com.example.myapplication.ui.theme.DividerColor
import com.example.myapplication.ui.theme.Important
import com.example.myapplication.ui.theme.Normal
import com.example.myapplication.ui_items.SwipeToDismissItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
) {
    val personList = remember { mutableStateOf<List<PersonDetailEntity>>(emptyList()) }
    val searchQuery = remember { mutableStateOf("") }
    val selectedFilter = remember { mutableStateOf("All") } // Default filter is 'All'
    val allData = remember { mutableStateOf<List<PersonDetailEntity>>(emptyList()) }

    // Fetch all data and sort it by importance level
    LaunchedEffect(Unit) {
        MainActivity().getAllData { data ->
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

    LaunchedEffect(searchQuery.value) {
        if (searchQuery.value.isNotEmpty()) {
            val searchResults = allData.value.filter { person ->
                person.namee?.contains(searchQuery.value, ignoreCase = true) ?: false
            }
            personList.value = searchResults
        } else {
            personList.value = allData.value // Reset to full list if search is empty
        }
    }

    // Gün farkını hesaplayan fonksiyon
    fun calculateDaysBetween(dateString: String): Long {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return try {
            val targetDate: Date = dateFormat.parse(dateString) ?: return -1
            val currentDate = Date()
            val diffInMillis: Long = targetDate.time - currentDate.time
            diffInMillis / (1000 * 60 * 60 * 24) // Milisaniye farkını gün cinsine çevir
        } catch (e: Exception) {
            -1 // Hata durumunda -1 döndür
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
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        expanded = !expanded
                    }) { // Toggle dropdown menu visibility
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = stringResource(R.string.days),
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.onSurface)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (selectedDayFilter == 3),
                                        onClick = { selectedDayFilter = 3
                                            expanded = false }
                                    )
                                    Text(text = stringResource(R.string._3_g_n),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.surface)
                                }
                            },
                            onClick = {
                                selectedDayFilter = 3
                                expanded = false // Close the dropdown menu after selection
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (selectedDayFilter == 5),
                                        onClick = { selectedDayFilter = 5
                                            expanded = false }
                                    )
                                    Text(text = stringResource(R.string._5_g_n),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.surface)
                                }
                            },
                            onClick = {
                                selectedDayFilter = 5
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (selectedDayFilter == 7),
                                        onClick = { selectedDayFilter = 7
                                            expanded = false }
                                    )
                                    Text(text = stringResource(R.string._7_g_n),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.surface)
                                }
                            },
                            onClick = {
                                selectedDayFilter = 7
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth() // Ensures that the Row takes the full width of the dropdown item
                                ) {

                                    // Add a button to the right side of the dropdown item
                                    Button(
                                        onClick = {
                                            showDialog = true
                                        }, // Show the dialog when the button is clicked
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.LightGray,
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.padding(start = 8.dp) // Adds some spacing between the Row and the Button
                                    ) {
                                        Text(text = stringResource(R.string.custom),
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurface)
                                    }

                                    // Optional: A dialog to input the custom day value
                                    if (showDialog) {
                                        AlertDialog(
                                            onDismissRequest = { showDialog = false },
                                            title = { Text(text = stringResource(R.string.enter_custom_day)) },
                                            text = {
                                                Column {
                                                    // TextField to take numeric input
                                                    TextField(
                                                        value = customDayValue,
                                                        onValueChange = { value ->
                                                            // Only allow numeric input (excluding non-numeric characters)
                                                            if (value.all { it.isDigit() }) {
                                                                customDayValue = value
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
                                                        showDialog = false
                                                        expanded = false
                                                    }
                                                ) {
                                                    Text(stringResource(R.string.ok))
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
                                }
                            },
                            onClick = {
                                // Parse the customDayValue safely to an integer
                                selectedDayFilter = customDayValue.toIntOrNull() ?: 0 // Handle null safely
                                expanded = false
                            }
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {MainActivity.startIntroScreensActivity(context) }) {
                        Icon(
                            imageVector = Icons.Outlined.Info, // Use any icon of your choice
                            contentDescription = stringResource(R.string.intro),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = Color.White
                )
            )
        },

        containerColor = MaterialTheme.colorScheme.onBackground,
        contentColor = DividerColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    MainActivity.editItem((-1).toString(), navController) {
                        MainActivity().getAllData { updatedList ->
                            personList.value = updatedList
                        }
                    }
                },
                Modifier.size(56.dp),
                shape = CircleShape,
                containerColor = Color.Gray
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
                "THOSE WHOSE DAY IS APPROACHING",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    fontSize = 15.sp
                ),
                modifier = Modifier.padding(16.dp)
            )

            // LazyRow for showing filtered items horizontally
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
                                color = Color.Gray,
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
                            else -> MaterialTheme.colorScheme.surface
                        }

                        val daysLeft = calculateDaysBetween(
                            person.selectedOptionn ?: ""
                        ) // Gün sayısını hesapla

                        Card(
                            modifier = Modifier
                                .height(screenHeight * 0.15f)
                                .width(screenWidth * 0.35f)
                                .padding(screenWidth * 0.05f)
                                .clickable {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        // ID'yi geçerek düzenleme sayfasına yönlendirme
                                        navController.navigate("edit_plan/${person._id}")
                                    }
                                },
                            shape = RoundedCornerShape(15.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onSurface
                            ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(80.dp)
                                        .width(8.dp)
                                        .background(color = cardBackgroundColor)
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 12.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "${person.namee}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.surface
                                    )
                                }

                                if (daysLeft < 0) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = stringResource(R.string.past_date_warning),
                                        modifier = Modifier.size(15.dp),
                                        tint = Color.Red // Uyarı ikonu için kırmızı renk
                                    )
                                }
                            }
                        }
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


            // Search bar
            TextField(
                shape = RoundedCornerShape(26.dp),
                textStyle = TextStyle(Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.DarkGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                label = {
                    Row(
                        modifier = Modifier.height(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_icon),
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.surface
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.search_for_notes),
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            )

            // Filter buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Yatay boşluk
            ) {
                val filters = listOf("All", "Critical", "Important", "Normal")
                filters.forEach { filter ->
                    val buttonColor = when (filter) {
                        "Critical" -> Color(Critical.value)
                        "Important" -> Color(Important.value)
                        "Normal" -> Color(Normal.value)
                        else -> Color.Gray
                    }

                    val isSelected = selectedFilter.value == filter
                    val backgroundColor = if (isSelected) buttonColor else Color.Transparent
                    val textColor =
                        if (isSelected) MaterialTheme.colorScheme.onSurface else buttonColor
                    val borderColor =
                        if (isSelected) MaterialTheme.colorScheme.onSurface else buttonColor

                    SuggestionChip(
                        onClick = { selectedFilter.value = filter },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = backgroundColor
                        ),
                        label = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .align(Alignment.CenterVertically) // Dikey merkezleme
                            ) {

                                Text(
                                    text = filter,
                                    color = textColor,
                                    modifier = Modifier.align(Alignment.Center), // Yatay merkezleme
                                    maxLines = 1,
                                    overflow = TextOverflow.Visible
                                )
                            }
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f),
                        border = BorderStroke(2.dp, borderColor),
                        shape = RoundedCornerShape(26.dp)
                    )
                }
            }
            // Sort the filteredList so that items with a negative day difference appear at the top
            /*
        test

         */
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
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                    )
                },
                /*      floatingActionButton = @androidx.compose.runtime.Composable {
                          ExtendedFloatingActionButton(
                              text = { Text("Show snackbar") },
                              icon = { Icon(Icons.Filled.FavoriteBorder, contentDescription = "") },
                              onClick = {
                                  scope.launch {
                                      snackbarHostState.showSnackbar("Snackbar")
                                  }
                              }
                          )
                      },*/
                containerColor = MaterialTheme.colorScheme.onBackground,
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
                                MainActivity.deleteItemById(id.toLong()) {
                                    MainActivity().getAllData { updatedList ->
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
                                        MainActivity.restoreItem(listOf(person)) {

                                                MainActivity().getAllData { data ->
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

//                                            MainActivity().getAllData { updatedList ->
//                                                personList.value = updatedList
//                                            }
                                        }
                                    }
                                  //  deletedItem = null
                                }
                            },

                            onEdit = { id ->
                                MainActivity.editItem(id, navController) {
                                    MainActivity().getAllData { updatedList ->
                                        personList.value = updatedList
                                    }
                                }
                            },
                            onRestore = { restoredPerson ->
                                // Restore an item if needed
                                restoredPerson._id?.let {
                                    MainActivity.restoreItem(listOf(restoredPerson)) {
                                        MainActivity().getAllData { updatedList ->
                                            personList.value = updatedList
                                        }
                                    }
                                }
                            },
                            snackbarHostState = snackbarHostState,// SnackbarHostState'i parametre olarak geçir
                            modifier = Modifier.animateItemPlacement(tween(400)),

                            ) {
                            val cardBackgroundColor = when (person.selectedOption11) {
                                "Critical" -> Color(Critical.value)
                                "Important" -> Color(Important.value)
                                "Normal" -> Color(Normal.value)
                                else -> MaterialTheme.colorScheme.surface
                            }
                            val daysLeft = calculateDaysBetween(
                                person.selectedOptionn ?: ""
                            ) // Gün sayısını hesapla

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface
                                ),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(60.dp)
                                            .width(8.dp)
                                            .background(color = cardBackgroundColor)
                                    )

                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 12.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${person.namee}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 22.sp,
                                            color = MaterialTheme.colorScheme.surface,
                                            maxLines = 1, // Metni 1 satırla sınırlandır
                                            overflow = TextOverflow.Ellipsis // 3 nokta ile kesilme
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${person.surnamee}",
                                            fontWeight = FontWeight.Light,
                                            fontSize = 20.sp,
                                            color = MaterialTheme.colorScheme.surface,
                                            maxLines = 1, // Metni 1 satırla sınırlandır
                                            overflow = TextOverflow.Ellipsis // 3 nokta ile kesilme
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        if (daysLeft < 0) {
                                            Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = stringResource(R.string.past_date_warning),
                                                modifier = Modifier.size(15.dp),
                                                tint = Color.Red // Uyarı ikonu için kırmızı renk
                                            )
                                        }
                                    }

                                    person.selectedOptionn?.let {
                                        Text(
                                            text = it,
                                            fontWeight = FontWeight.Light,
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.surface,
                                            maxLines = 1, // Metni 1 satırla sınırlandır
                                            overflow = TextOverflow.Ellipsis // 3 nokta ile kesilme
                                        )
                                    }
                                }
                            }

                        }
                    }
// Add a Spacer at the end of the LazyColumn
                    item {
                        Spacer(modifier = Modifier.height(48.dp)) // Adjust the height as needed
                    }
                }
            }
        }
    }
}

