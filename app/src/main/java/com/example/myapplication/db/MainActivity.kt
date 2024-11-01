package com.example.myapplication.db

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.myapplication.screens.EditPlan
import com.example.myapplication.helper.PersonDetailEntity
import com.example.myapplication.R
import com.example.myapplication.ui.theme.DividerColor
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.withContext
import com.example.myapplication.screens.IntroScreens
import com.example.myapplication.screens.MainScreen



class MainActivity : ComponentActivity() {
    // Hilt tarafından enjekte edilen bağımlılık
    @Inject
    lateinit var myDependency: MyDependency

    class MyDependency @Inject constructor() {
        // MyDependency'in içerikleri
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)

        initiateRealm()
        //insertDate(name= "", surname= "", mail = "", phone = "", city = "")
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            NavHost(
                                navController = navController,
                                startDestination = getString(R.string.first_page) // Doğru rota adını kullanın
                            ) {
                                composable(getString(R.string.first_page)) {
                                    MainScreen(navController)
                                }
//                                composable("add_plan") {
//                                    AddPlan(navController = navController)
//                                }
                                composable("edit_plan/{id}") { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                                    id?.let {
                                        EditPlan(navController = navController, id = it)
                                    }
                                }
//                                composable("edit_plan/$id") { backStackEntry ->
//                                    val id =
//                                        backStackEntry.arguments?.getString("_id")?.toLongOrNull()
//                                    id?.let {
//                                        EditPlan(navController = navController, id = it)
//                                    }
//                                }
                                composable(getString(R.string.save_page)) {
                                    MainScreen(navController)
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


    fun initiateRealm() {
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


    companion object {
        fun insertDate(
            name: String,
            surname: String,
            date: String,
            selectedOption: String,
            selectedOption1: String
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                // Benzersiz bir _id değeri oluşturmak için UUID kullanıyoruz
                val uniqueId =
                    System.currentTimeMillis() // Benzersiz bir ID oluşturmanın bir yolu olarak anlık zamanı kullanıyoruz

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

                // Kopyalama işlemi: Mevcut veriyi geçici bir listeye kopyala
                val personListCopy = realm.where(PersonDetailEntity::class.java)
                    .findAll()
                    .map { person ->
                        // Yeni bir PersonDetailEntity nesnesi oluşturup her alanı kopyala
                        PersonDetailEntity().apply {
                            this._id = person._id
                            this.namee = person.namee
                            this.surnamee = person.surnamee
                            this.timestamp = person.timestamp
                            this.selectedOptionn = person.selectedOptionn
                            this.selectedOption11 = person.selectedOption11
                            // Diğer alanlar varsa onları da burada kopyalayın
                        }
                    }

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

                    // Yedek listeye erişim
                    println("Silinmeden önceki kopya liste: $personListCopy")
                }
            }
        }

        fun editItem(id: String, navController: NavController, onEditCompleted: () -> Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                // ID'yi geçerek düzenleme sayfasına yönlendirme
                println("Navigating to edit_plan with id: $id")
                navController.navigate("edit_plan/$id") // ID'yi geçerek sayfaya yönlendirme

                // Düzenleme işlemi tamamlandığında UI güncellemesi için callback'i çağır
                onEditCompleted()
            }
        }



        fun startIntroScreensActivity(context: Context) {
            val intent = Intent(context, IntroScreens::class.java)
            context.startActivity(intent)

        }


        fun restoreItem(copiedList: List<PersonDetailEntity>, onRestoreCompleted: () -> Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val realm = Realm.getDefaultInstance()

                realm.executeTransaction { r ->
                    copiedList.forEach { person ->
                        // Check if the person already exists in the database by ID
                        val existingPerson = r.where(PersonDetailEntity::class.java)
                            .equalTo("_id", person._id) // Make sure "_id" is the unique identifier
                            .findFirst()

                        if (existingPerson != null) {
                            // If the person exists, update their information
                            existingPerson.namee = person.namee
                            existingPerson.surnamee = person.surnamee
                            existingPerson.timestamp = person.timestamp
                            existingPerson.selectedOptionn = person.selectedOptionn
                            existingPerson.selectedOption11 = person.selectedOption11
                            // Update other fields as necessary
                        } else {
                            // If the person does not exist, insert a new record
                            r.copyToRealmOrUpdate(person)
                        }
                    }
                }

                realm.close()

                // Callback on the main thread after restoration is complete
                withContext(Dispatchers.Main) {
                    onRestoreCompleted() // Notify that restoration is done to update UI
                }
            }
        }
    }


    fun getAllData(onDataLoaded: (List<PersonDetailEntity>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {

            val realm = Realm.getDefaultInstance()

            // Fetch all data
            val results = realm.where(PersonDetailEntity::class.java).findAll()

            // Remove entries with null values in multiple fields
            realm.executeTransaction {
                val nullEntries = results.where()
                    .isNull("namee") // Replace with actual field name
                    .or()
                    .isNull("surnamee") // Add as many fields as necessary
                    .or()
                    .isNull("selectedOptionn") // Add as many fields as necessary
                    .or()
                    .isNull("selectedOption11") // Add as many fields as necessary
                    .or()
                    .isNull("timestamp") // Example of a third field
                    .findAll()

                nullEntries.deleteAllFromRealm() // Deletes entries with null in any of the specified fields
            }
           // veriler burada geliyor
            // Convert the remaining results to a list
            val dataList = realm.copyFromRealm(results) // Get a copy of the non-null results
            realm.close() // Close the Realm instance

            // Return to the main thread
            withContext(Dispatchers.Main) {
                onDataLoaded(dataList) // Return the cleaned-up data
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        val navController = rememberNavController()
       EditPlan(navController = navController, -1)
    }
}