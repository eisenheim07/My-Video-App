package com.example.hivefive.helper

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreUtils {
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun updateUserStatus(isOnline: Boolean) {
        val userId = auth.currentUser?.email ?: return
        val userRef = db.collection("users").document(userId)
        val statusMap = mapOf(
            "status" to isOnline,
        )
        userRef.update(statusMap)
            .addOnFailureListener { e -> println("Failed to update status: $e") }
    }
}