package com.example.hivefive.ui.views

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hivefive.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

fun firebaseAuthWithGoogle(idToken: String, auth: FirebaseAuth, navController: NavHostController) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("home") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            } else {
                Log.e("GoogleSignIn", "Firebase Auth Failed", task.exception)
            }
        }
}

@Composable
fun LoginPage(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as Activity
    val oneTapClient = remember { Identity.getSignInClient(context) }
    val auth = Firebase.auth

    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("460816281562-4csbe41uu643cn9ke9oseprk5udt61qv.apps.googleusercontent.com")
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken, auth, navController)
                }
            } catch (e: ApiException) {
                Log.e("GOOGLE", "Sign-in failed", e)
            }
        }
    }

    fun signInWithGoogle() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(activity) { result ->
                launcher.launch(IntentSenderRequest.Builder(result.pendingIntent).build())
            }
            .addOnFailureListener { e ->
                Log.e("GOOGLE", "One-Tap sign-in failed", e)
            }
    }

    Column(
        modifier = Modifier
            .padding(28.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome Again", color = Color.Red, fontSize = 26.sp)
        Text(text = "Login to continue", color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(52.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .border(1.dp, Color.Gray, CircleShape)
                .clip(CircleShape),
            value = "",
            placeholder = { Text(text = "Enter Email or Username") },
            onValueChange = {},
            leadingIcon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "") },
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .border(1.dp, Color.Gray, CircleShape)
                .clip(CircleShape),
            value = "",
            placeholder = { Text(text = "Enter Password") },
            onValueChange = {},
            leadingIcon = { Icon(imageVector = Icons.Default.Warning, contentDescription = "") },
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
        }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { signInWithGoogle() }) {
            Row(
                modifier = Modifier
                    .height(22.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Sign In using Google Account", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    LoginPage(rememberNavController())
}