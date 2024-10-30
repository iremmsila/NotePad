package com.example.myapplication.helper



import io.realm.RealmList
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

    var customerAddressList : RealmList<AddressEntity>? = RealmList()


}

open class AddressEntity : RealmObject(){

    @PrimaryKey
    var _id : Long = 0

    var namee : String? = null
    var surnamee : String? = null
    var maill : String? = null
    var phonee : String? = null
    var cityy : String? = null
    var timestamp: String?= null
}


