package com.charlye934.uber.providers

import com.charlye934.uber.login.data.model.Driver
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object DriverProvider {
    val mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Driver")

    fun create(driver: Driver): Task<Void>{
        return mDatabase.child(driver.id).setValue(driver)
    }

    fun getDriver(idDriver: String): DatabaseReference{
        return mDatabase.child(idDriver)
    }

    fun update(driver: Driver): Task<Void>{
        val map = HashMap<String, Any>()
        map["name"] = driver.name
        map["image"] = driver.image
        map["vehicleBrand"] = driver.vehicleBrand
        map["vehiclePlate"] = driver.vehiclePlate
        return mDatabase.child(driver.id).updateChildren(map)
    }
}