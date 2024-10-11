package com.example.notebook

import DatePickerModal
import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.ui.theme.DividerColor
import com.example.myapplication.ui.theme.PrimaryColor
import com.example.myapplication.ui.theme.SecondaryColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SimpleDateFormat")
@Composable
fun AddPlan(navController: NavController) {

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }

    // Açılır menü için state'ler
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    var textFieldValue by remember { mutableStateOf("Select an option") } // Varsayılan değer

    // DatePicker için state'ler
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<String?>(null) }

    // Hata mesajı için state
    var errorMessage by remember { mutableStateOf("") }

    // Renk seçimi için state'ler
    var colorMenuExpanded by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.Gray) }
    var selectedColorr by remember { mutableStateOf(Color.Yellow) }

    val colors = listOf(
        Color.Red to "Critical",
        Color(0xFFFFA500) to "Important",
        Color.Yellow to "Normal"
    )

    var isNameError by remember { mutableStateOf(false) }
    var isSurnameError by remember { mutableStateOf(false) }




    Scaffold(
        topBar = @androidx.compose.runtime.Composable {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center // Metni tam ortalayın
                    ) {
                        Text(
                            text = "Add Note",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface // İkon rengini ayarlayın
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSurface, // Arka plan rengi
                    titleContentColor = MaterialTheme.colorScheme.surface // Başlık rengi
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = DividerColor,
                shape = CircleShape,
                onClick = {
                    // Alanların boş olup olmadığını kontrol et
                    isNameError = name.isEmpty()
                    isSurnameError = surname.isEmpty()

                    if (name.isEmpty() || surname.isEmpty()) {
                      /*  errorMessage = "Please fill in all fields"*/
                    } else {
                        val currentDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
                        val finalDate = selectedDate ?: run {
                            val calendar = Calendar.getInstance()
                            calendar.add(Calendar.YEAR, 1)
                            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
                        }

                        MainActivity.insertDate(
                            name,
                            surname,
                            currentDate,
                            finalDate,
                            selectedOption.ifEmpty { "Normal" }
                        )

                        navController.navigate("first_page") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                },
                contentColor = Color.White,      // İkonun rengini beyaz yapıyoruz (isteğe bağlı)
                modifier = Modifier.size(56.dp)  // FAB boyutunu ayarlayabilirsiniz, default boyut genellikle 56.dp'dir
            ) {
                Icon(
                    imageVector = Icons.Default.Check,  // Kaydetme ikonu
                    contentDescription = "Save Plan"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.onBackground,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {

                Spacer( Modifier.height(16.dp))

                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = "" // Kullanıcı yazı girdiğinde hata mesajını sıfırla
                    },
                    textStyle = TextStyle(color = Color.White),
                    label = {
                        Text(
                            text = "Enter Title",
                            color = if (isNameError) Color.Red else MaterialTheme.colorScheme.surface
                        )
                    },
                    modifier = Modifier
                        .weight(1f) // Genişliğin küçük bir kısmını kapla (oran 1)
                        .fillMaxWidth(), // TextField'i yatayda tamamen doldur
                    colors = TextFieldDefaults.textFieldColors(
                        errorContainerColor = Color.DarkGray,
                        containerColor = if (isNameError) Color.DarkGray else MaterialTheme.colorScheme.onBackground, // Boşluk durumunda arka planı beyaz yap
                        focusedIndicatorColor =  Color.Transparent, // Boşsa kırmızı kenarlık
                        unfocusedIndicatorColor =  Color.Transparent, // Boşsa kırmızı kenarlık
                    ),
                    isError = isNameError // TextField hata durumunu belirtiyoruz
                )

                TextField(
                    value = surname,
                    onValueChange = {
                        surname = it
                        errorMessage = "" // Kullanıcı yazı girdiğinde hata mesajını sıfırla
                    },
                    textStyle = TextStyle(color = Color.White),
                    label = { Text(
                        text = "Enter Content",
                        color = if (isSurnameError) Color.Red else MaterialTheme.colorScheme.surface
                    ) },
                    modifier = Modifier
                        .weight(10f) // Genişliğin büyük kısmını kapla
                        .fillMaxWidth(), // TextField'i yatayda tamamen doldur
                    colors = TextFieldDefaults.textFieldColors(
                        errorContainerColor = Color.DarkGray,
                        containerColor = if (isNameError) Color.DarkGray else MaterialTheme.colorScheme.onBackground, // Boşluk durumunda arka planı beyaz yap
                        focusedIndicatorColor =  Color.Transparent, // Boşsa kırmızı kenarlık
                        unfocusedIndicatorColor = Color.Transparent, // Boşsa kırmızı kenarlık
                    ),
                    isError = isSurnameError // TextField hata durumunu belirtiyoruz
                )

                // Hata mesajı
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.weight(1f))



                @Composable
                fun CustomColorMenu(
                    expanded: Boolean,
                    onDismissRequest: () -> Unit,
                    onColorSelected: (Color, String) -> Unit
                ) {
                    if (expanded) {
                        // Kendi arka plan ve yerleşim düzeninizi belirlemek için Box kullanın
                        Box(
                            modifier = Modifier
                                .background(Color.DarkGray)
                                .padding(16.dp)
                        ) {
                            Column {

                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color.Red, shape = CircleShape)
                                        .clickable {
                                            onColorSelected(Color.Red, "Critical")
                                            onDismissRequest()
                                        }
                                        .padding(8.dp)
                                )

                                Spacer(Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color(0xFFFFA500), shape = CircleShape)
                                        .clickable {
                                            onColorSelected(Color(0xFFFFA500), "Important")
                                            onDismissRequest()
                                        }
                                        .padding(8.dp)
                                )

                                Spacer(Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color.Yellow, shape = CircleShape)
                                        .clickable {
                                            onColorSelected(Color.Yellow, "Normal")
                                            onDismissRequest()
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }

                CustomColorMenu(
                    expanded = colorMenuExpanded,
                    onDismissRequest = { colorMenuExpanded = false }, // Menü kapatıldığında expanded'i false yap
                    onColorSelected = { color, option ->
                        // Renk ve metin seçimini güncelleyin
                        selectedColor = color
                        selectedOption = option
                        colorMenuExpanded = false  // Menü kapandıktan sonra expanded'i false yap
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        // DatePicker butonu ve seçilen tarih gösterimi
                        Button(
                            onClick = { showDatePicker = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Butonun arka plan rengi
                        ) {
                            // Buton içine hem ikonu hem de metni yerleştirmek için Row kullanılır
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Tarih seçmek için bir takvim ikonu eklenir
                                Icon(
                                    imageVector = Icons.Default.DateRange, // Takvim ikonu
                                    contentDescription = "Select Date",
                                    modifier = Modifier.size(24.dp) // İkon boyutu
                                )
                            }
                        }


                        selectedDate?.let {
                            Text(text = "Selected Date: $it", modifier = Modifier.padding(16.dp))
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

                        // Renk seçici menü
                        Button(
                            onClick = { colorMenuExpanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Butonun arka plan rengi
                        ) {
                            // İkon ve metni yan yana göstermek için Row kullanıyoruz
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Varsayılan olarak sarı rengi gösteren ikon
                                Box(
                                    modifier = Modifier
                                        .size(24.dp) // İkon boyutu
                                        .background(selectedColorr, shape = CircleShape) // Seçili renge göre ikon rengi
                                )

                            }
                        }
                    }

                }

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AddPlanPreview() {
    val navController = rememberNavController()
    AddPlan(navController = navController)
}

