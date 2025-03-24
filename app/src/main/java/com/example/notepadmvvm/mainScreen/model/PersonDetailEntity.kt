package com.example.notepadmvvm.mainScreen.model

//import androidx.room.PrimaryKey

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey



open class PersonDetailEntity : RealmObject(){

    @PrimaryKey
    var _id : Long? = null

    var namee : String? = null
    var surnamee : String? = null

    var timestamp: String?= null
    var selectedOptionn: String?= null
    var selectedOption11: String?= null
    //var isPinned: Boolean = false
}