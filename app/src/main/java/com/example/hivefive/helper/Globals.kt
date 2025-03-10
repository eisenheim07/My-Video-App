package com.example.hivefive.helper

import com.google.firebase.firestore.FirebaseFirestore

object Globals {
    fun checkIfCollectionExists(collectionName: String, firestore: FirebaseFirestore, callback: (Boolean) -> Unit) {
        firestore.collection(collectionName)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                callback(!querySnapshot.isEmpty)
            }
            .addOnFailureListener { exception ->
                callback(false)
                exception.printStackTrace()
            }
    }
}