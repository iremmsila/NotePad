//package com.example.notepadmvvm.AddAndEditScreen.viewModel
//
//
//import androidx.compose.ui.graphics.Color
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.notepadmvvm.mainScreen.model.PersonDetailEntity
//import io.realm.Realm
//import kotlinx.coroutines.launch
//
//
//import androidx.compose.runtime.mutableStateOf
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Date
//import java.util.Locale
//
//class MyEditPlanViewModel : ViewModel() {
//
//    val name = mutableStateOf("")
//    val surname = mutableStateOf("")
//    val selectedDate = mutableStateOf<String?>(null)
//    val selectedOption = mutableStateOf("Normal")
//    val selectedColor = mutableStateOf(Color.Yellow)
//    val isNameError = mutableStateOf(false)
//    val isSurnameError = mutableStateOf(false)
//    val updateCompleted = mutableStateOf(false)
//    val showDatePicker = mutableStateOf(false)
//
//    val importanceColors = mapOf(
//        "Critical" to Color.Red,
//        "Important" to Color(0xFFFFA500),
//        "Normal" to Color.Yellow
//    )
//
//
//    fun fetchPersonDetails(id: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val realm = Realm.getDefaultInstance()
//            val person = realm.where(PersonDetailEntity::class.java)
//                .equalTo("_id", id)
//                .findFirst()
//            person?.let {
//                name.value = it.namee ?: ""
//                surname.value = it.surnamee ?: ""
//                selectedOption.value = it.selectedOption11 ?: "Normal"
//                selectedDate.value = it.selectedOptionn
//                selectedColor.value = importanceColors[it.selectedOption11] ?: Color.Yellow
//            }
//            realm.close()
//        }
//    }
//
//    fun updatePerson(id: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val realm = Realm.getDefaultInstance()
//            realm.executeTransaction { r ->
//                val personToUpdate = r.where(PersonDetailEntity::class.java)
//                    .equalTo("_id", id)
//                    .findFirst()
//                personToUpdate?.apply {
//                    namee = name.value
//                    surnamee = surname.value
//                    selectedOption11 = selectedOption.value
//                    selectedOptionn = selectedDate.value
//                }
//            }
//            realm.close()
//            updateCompleted.value = true
//        }
//    }
//
//    fun insertPerson() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val realm = Realm.getDefaultInstance()
//            realm.executeTransaction { r ->
//                val newPerson = r.createObject(PersonDetailEntity::class.java)
//                newPerson.namee = name.value
//                newPerson.surnamee = surname.value
//                newPerson.selectedOption11 = selectedOption.value
//                newPerson.selectedOptionn = selectedDate.value ?: SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//                    .format(Calendar.getInstance().apply { add(Calendar.YEAR, 1) }.time)
//            }
//            realm.close()
//            updateCompleted.value = true
//        }
//    }
//
//
//
//    fun onDateSelected(dateInMillis: Long?) {
//        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//        selectedDate.value = dateInMillis?.let { sdf.format(Date(it)) }
//        showDatePicker.value = false
//    }
//
//    fun selectImportance(option: String) {
//        selectedOption.value = option
//        selectedColor.value = importanceColors[option] ?: Color.Yellow
//    }
//}
