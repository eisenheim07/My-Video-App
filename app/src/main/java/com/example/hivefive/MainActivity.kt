package com.example.hivefive

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hivefive.ui.theme.HiveFiveTheme
import com.example.hivefive.ui.views.HomePage
import com.example.hivefive.ui.views.LoginPage
import com.example.hivefive.ui.views.SignupPage
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HiveFiveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        NavHostScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun NavHostScreen() {
    val auth = FirebaseAuth.getInstance()
    var start = "login"
    if (auth.currentUser != null) {
        start = "home"
    }
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = start) {
        composable("login") {
            LoginPage(navController)
        }
        composable("home") {
            HomePage(navController, auth)
        }
        composable("signup") {
            SignupPage(navController)
        }
    }
}
