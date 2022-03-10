package com.mistershorr.birthdaytracker

import java.util.*

//Baas APIs often require no parameter constructor
//Give each field a default value so that you can use a no parameter constructor

data class Person(
    var name : String = "Default Person",
    var birthday : Date = Date(1646932081110),
    var budget : Double = 0.99,
    var desiredGift : String = "String",
    var previousGifts : List<String> = listOf(),
    var previousGiftsToMe: List<String> = listOf(),
    var giftPurchased: Boolean = false
) {
    //TODO: have methods to return the calculated values
}

