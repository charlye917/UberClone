package com.charlye934.uber.providers

import com.charlye934.uber.login.data.model.Client
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.collections.HashMap

object ClientProvider {
    val mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Clients")

    fun create(client: Client): Task<Void>{
        val map = HashMap<String, Any>()
        map["name"] = client.name
        map["email"] = client.email
        return mDatabase.child(client.id).setValue(map)
    }

    fun update(client: Client): Task<Void>{
        val map = HashMap<String, Any>()
        map["name"] = client.name
        map["image"] = client.image
        return mDatabase.child(client.id).updateChildren(map)
    }

    fun getClient(idClient: String): DatabaseReference{
        return mDatabase.child(idClient)
    }

}