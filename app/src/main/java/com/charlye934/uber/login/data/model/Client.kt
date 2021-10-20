package com.charlye934.uber.login.data.model

data class Client(
    val id: String,
    val name: String,
    val email: String,
    val image: String = ""
)