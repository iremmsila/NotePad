package com.example.myapplication

import android.annotation.SuppressLint
import android.hardware.lights.LightState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.navigation.compose.composable
import com.example.myapplication.ui.theme.Critical
import com.example.myapplication.ui.theme.DividerColor
import com.example.myapplication.ui.theme.Important
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Normal
import com.example.myapplication.ui.theme.PrimaryColor
import com.example.myapplication.ui.theme.SecondaryColor
import com.example.notebook.AddPlan
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds


class MainActivity : ComponentActivity() {
    // Hilt tarafından enjekte edilen bağımlılık
    @Inject lateinit var myDependency: MyDependency

    class MyDependency @Inject constructor() {
        // MyDependency'in içerikleri
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initiateRealm()
        //insertDate(name= "", surname= "", mail = "", phone = "", city = "")
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DividerColor,
                    content = { innerPadding ->
                        // innerPadding doğru bir şekilde kullanılıyor
                        Box(modifier = Modifier.padding(innerPadding)) {
                            NavHost(
                                navController = navController,
                                startDestination = stringResource(R.string.first_page)
                            ) {
                                composable(getString(R.string.first_page)) {
                                    MainScreen()
                                }
                                composable(getString(R.string.add_plan)) {
                                    AddPlan(navController = navController)
                                }
                                composable(getString(R.string.edit_plan_id)) { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                                    id?.let {
                                        EditPlan(navController = navController, id = it)
                                    }
                                }
                            }

                        }
                    }
                )
            }
        }

        getAllData { personList ->
            // Okunan verilerle işlem yap
            // Örneğin, RecyclerView güncelleyebilirsiniz
        }
    }


    private fun initiateRealm(){
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name(getString(R.string.db_realm))
            //.allowWritesOnUiThread(false)
            //.allowQueriesOnUiThread(false)
            .schemaVersion(2)
            //.migration(WholeRealmMigration())
            .build()
        Realm.setDefaultConfiguration(config)
    }



    companion object{
        fun insertDate(
            name: String,
            surname: String,
            date: String,
            selectedOption: String,
            selectedOption1: String
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                // Benzersiz bir _id değeri oluşturmak için UUID kullanıyoruz
                val uniqueId = System.currentTimeMillis() // Benzersiz bir ID oluşturmanın bir yolu olarak anlık zamanı kullanıyoruz

                val personalDetail = PersonDetailEntity().apply {
                    _id = uniqueId
                    namee = name
                    surnamee = surname
                    timestamp = date
                    selectedOptionn = selectedOption
                    selectedOption11 = selectedOption1

                    customerAddressList = RealmList()
                }

                var realmDb = Realm.getDefaultInstance() // Get default Instance
                realmDb.beginTransaction()
                realmDb.copyToRealmOrUpdate(personalDetail) // Insert or update the data
                realmDb.commitTransaction()
                realmDb.close() // Close the database instance once the transaction is done
            }
        }



        fun deleteItemById(id: Long, onDeleteCompleted: () -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction { r ->
                    val personToDelete = r.where(PersonDetailEntity::class.java)
                        .equalTo("_id", id)
                        .findFirst()
                    personToDelete?.deleteFromRealm() // Veriyi sil
                }
                realm.close()

                // Silme işlemi tamamlandıktan sonra callback'i tetikle
                withContext(Dispatchers.Main) {
                    onDeleteCompleted() // UI'ı güncellemek için çağrı
                }
            }
        }

        fun editItem(id: String, navController: NavController, onEditCompleted: () -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                // ID'yi geçerek düzenleme sayfasına yönlendirme
                navController.navigate("edit_plan/$id") // ID'yi geçerek sayfaya yönlendirme

                // Düzenleme işlemi tamamlandığında UI güncellemesi için callback'i çağır
                onEditCompleted()
            }
        }


        fun updatePerson(person: PersonDetailEntity) {
            CoroutineScope(Dispatchers.IO).launch {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction { r ->
                    r.copyToRealmOrUpdate(person) // Mevcut kaydı güncelle
                }
                realm.close()
            }
        }

        fun restoreItem(person: PersonDetailEntity, onRestoreCompleted: () -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction { r ->
                    // Silinmiş olan öğeyi geri eklemek için copyToRealmOrUpdate kullanıyoruz
                    r.copyToRealmOrUpdate(person) // Öğeyi geri ekle veya güncelle
                }
                realm.close()

                // Geri alma işlemi tamamlandıktan sonra callback'i ana iş parçacığında tetikle
                withContext(Dispatchers.Main) {
                    onRestoreCompleted() // UI'ı güncellemek için çağrı
                }
            }
        }

    }





    fun getAllData(onDataLoaded: (List<PersonDetailEntity>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val realm = Realm.getDefaultInstance()
            val results = realm.where(PersonDetailEntity::class.java).findAll() // Tüm verileri al

            // Sonuçları bir listeye çevir
            val dataList = realm.copyFromRealm(results) // Gerçek nesneler yerine kopyalarını al
            realm.close() // Realm instance'ını kapat

            // Ana iş parçacığına dön
            withContext(Dispatchers.Main) {
                onDataLoaded(dataList) // Okunan verileri geri döndür
            }
        }
    }

    fun searchByName(name: String, onDataLoaded: (List<PersonDetailEntity>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val realm = Realm.getDefaultInstance()
            val results = realm.where(PersonDetailEntity::class.java)
                .equalTo("namee", name) // Burada "namee" alanı üzerinde arama yapılıyor
                .findAll()

            // Sonuçları bir listeye çevir
            val dataList = realm.copyFromRealm(results)
            realm.close()

            // Ana iş parçacığına dön
            withContext(Dispatchers.Main) {
                onDataLoaded(dataList)
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Greeting(navController: NavController) {
    val personList = remember { mutableStateOf<List<PersonDetailEntity>>(emptyList()) }
    val searchQuery = remember { mutableStateOf("") }
    val selectedFilter = remember { mutableStateOf("All") } // Default filter is 'All'

    var selectedDaysFilter2 by remember { mutableStateOf(3) } // Varsayılan olarak 3 gün seçili
    var customDaysInput by remember { mutableStateOf("") } // Kullanıcının gireceği özel gün sayısı

    val options = listOf(3, 5, 7) // Ön tanımlı seçenekler

    // Search functionality with LaunchedEffect
    LaunchedEffect(searchQuery.value) {
        if (searchQuery.value.isNotEmpty()) {
            MainActivity().searchByName(searchQuery.value) { data ->
                personList.value = data
            }
        }
    }

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
            personList.value = sortedData
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


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Notes",
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
                            contentDescription = "Days",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (selectedDayFilter == 3),
                                        onClick = { selectedDayFilter = 3 }
                                    )
                                    Text(text = "3 Gün", fontSize = 12.sp)
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
                                        onClick = { selectedDayFilter = 5 }
                                    )
                                    Text(text = "5 Gün", fontSize = 12.sp)
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
                                        onClick = { selectedDayFilter = 7 }
                                    )
                                    Text(text = "7 Gün", fontSize = 12.sp)
                                }
                            },
                            onClick = {

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
                                        onClick = { showDialog = true }, // Show the dialog when the button is clicked
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.LightGray,
                                            contentColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.padding(start = 8.dp) // Adds some spacing between the Row and the Button
                                    ) {
                                        Text(text = "Custom", fontSize = 10.sp)
                                    }
                                }
                            },
                            onClick = {
                                selectedDayFilter = customDayValue.toInt()
                                expanded = false
                            }
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
                onClick = { navController.navigate("add_plan") },
                Modifier.size(56.dp),
                shape = CircleShape,
                containerColor = Color.Gray
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
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
                horizontalArrangement = Arrangement.spacedBy(10.dp) // Space between items
            ) {
                if (filteredListRow.isEmpty()) {
                    // Show a placeholder if the filteredList is empty
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No items available",
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
                                .height(110.dp)
                                .width(150.dp)
                                .padding(20.dp),
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
                                        contentDescription = "Past Date Warning",
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
                    title = { Text(text = "Enter Custom Days") },
                    text = {
                        TextField(
                            value = customDayValue,
                            onValueChange = { customDayValue = it },
                            label = { Text("Days") },
                            placeholder = { Text("Enter number of days") }
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
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
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
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.surface
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = "Search For Notes", color = MaterialTheme.colorScheme.surface)
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
                        colors =  SuggestionChipDefaults.suggestionChipColors(
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
            val sortedFilteredList = filteredList.sortedBy { person ->
                calculateDaysBetween(person.selectedOptionn ?: "")
            }

            val snackbarHostState = remember { SnackbarHostState() }

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                containerColor = MaterialTheme.colorScheme.onBackground,
            ) {
                // LazyColumn for showing filtered items vertically
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
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
                            },
                            onEdit = { id ->
                                MainActivity.editItem(id, navController) {
                                    MainActivity().getAllData { updatedList ->
                                        personList.value = updatedList
                                    }
                                }
                            },
                            onRestore = { restoredPerson ->
                                MainActivity.restoreItem(restoredPerson) {
                                    MainActivity().getAllData { updatedList ->
                                        personList.value = updatedList
                                    }
                                }
                            },
                            snackbarHostState = snackbarHostState ,// SnackbarHostState'i parametre olarak geçir
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
                                            color = MaterialTheme.colorScheme.surface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${person.surnamee}",
                                            fontWeight = FontWeight.Light,
                                            fontSize = 20.sp,
                                            color = MaterialTheme.colorScheme.surface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        if (daysLeft < 0) {
                                            Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = "Past Date Warning",
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
                                            color = MaterialTheme.colorScheme.surface
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissItem(
    item: PersonDetailEntity,
    onRemove: (String) -> Unit,
    onRestore: (PersonDetailEntity) -> Unit,
    onEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState, // SnackbarHostState'i parametre olarak ekliyoruz
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var deletedItem by remember { mutableStateOf<PersonDetailEntity?>(null) }

    // Remember the swipe state to handle swipes
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            when (state) {
                SwipeToDismissBoxValue.EndToStart -> {
                    showDialog = true
                    false
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEdit((item._id ?: "").toString())
                    false
                }
                else -> false
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Deletion") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                TextButton(onClick = {
                    deletedItem = item
                    onRemove((item._id ?: "").toString())
                    showDialog = false
                    coroutineScope.launch {
                        swipeToDismissState.reset()

                        // Snackbar'ı göster
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = "Item deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Indefinite
                        )

                        // Özel bir süre bekleyip ardından Snackbar'ı gizleyebilirsiniz
                        delay(10000)
                        snackbarHostState.currentSnackbarData?.dismiss() // Snackbar'ı kapat

                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                            onRestore(deletedItem!!)
                            deletedItem = null
                        } else {
                            deletedItem = null
                        }
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    coroutineScope.launch {
                        swipeToDismissState.reset()
                    }
                }) {
                    Text("No")
                }
            }
        )
    }

    SwipeToDismissBox(
        state = swipeToDismissState,
        backgroundContent = {
            val backgroundColor by animateColorAsState(
                targetValue = when (swipeToDismissState.targetValue) {
                    SwipeToDismissBoxValue.StartToEnd -> Color(0xBD8BD54B)
                    SwipeToDismissBoxValue.EndToStart -> Color(0xB3FC3131)
                    else -> MaterialTheme.colorScheme.onBackground
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp)
            ) {
                if (swipeToDismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(36.dp)
                    )
                }

                if (swipeToDismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(36.dp)
                    )
                }
            }
        },
        modifier = modifier
    ) {
        content()
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        val navController = rememberNavController()
        Greeting(navController = navController)
    }
}