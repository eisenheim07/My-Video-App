package com.example.hivefive.ui.views

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomePage(navController: NavHostController, auth: FirebaseAuth) {

    Button(onClick = {
        auth.signOut()
        navController.navigate("login") {
            popUpTo("home") {
                inclusive = true
            }
        }
    }) {
        Text(text = "SIGNOUT")
    }

}