package com.example.notepadmvvm.mainScreen.viewModel


import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.notepadmvvm.IntroScreens
import com.example.notepadmvvm.mainScreen.model.PersonDetailEntity
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonViewModel() : ViewModel() {

    private val _personList = MutableLiveData<List<PersonDetailEntity>>()
    val personList: LiveData<List<PersonDetailEntity>> = _personList



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



        fun startIntroScreensActivity(context: Context) {/////////////////////////////////////////////////////////
            context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
                .edit().putBoolean("onboarding_shown", true).apply()  //bayrak sıfırlandı
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

    fun fetchDataFromDatabase(itemId: Long): String {
        // Veritabanından `itemId` ile veriyi çekip geri döndürün
        val realm = Realm.getDefaultInstance()
        val person = realm.where(PersonDetailEntity::class.java)
            .equalTo("_id", itemId)
            .findFirst()
        val name = person?.namee ?: "" // Veritabanında değer varsa çek, yoksa boş string döndür

        realm.close() // Realm işlemi tamamlandıktan sonra kapatın
        return name
    }

    fun fetchSurnameFromDatabase(itemId: Long): String {
        // Veritabanından `itemId` ile soyadını çekin
        val realm = Realm.getDefaultInstance()
        val person = realm.where(PersonDetailEntity::class.java)
            .equalTo("_id", itemId)
            .findFirst()
        val surname = person?.surnamee ?: "" // Veritabanında değer varsa çek, yoksa boş string döndür

        realm.close() // Realm işlemi tamamlandıktan sonra kapatın
        return surname
    }
}
