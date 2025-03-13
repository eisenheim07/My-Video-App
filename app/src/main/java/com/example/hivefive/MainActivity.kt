package com.example.hivefive

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hivefive.ui.theme.ZegoCloudVideoCallTheme
import com.example.hivefive.ui.views.HomePage
import com.example.hivefive.ui.views.LoginPage
import com.example.hivefive.ui.views.SignupPage
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZegoCloudVideoCallTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NavHostScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()
    var start = "login"
    val auth = FirebaseAuth.getInstance()
    auth.currentUser?.let {
        start = "home"
    }
    NavHost(navController = navController, startDestination = start) {
        composable("login") {
            LoginPage(navController)
        }
        composable("signup") {
            SignupPage(navController)
        }
        composable("home") {
            HomePage(navController, auth)
        }
    }
}