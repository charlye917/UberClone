package com.charlye934.uber.login.data.model

data class Driver(
    val id: String,
    val name: String,
    val email: String,
    val vehicleBrand: String,
    val vehiclePlate:String,
    val image: String = ""
)