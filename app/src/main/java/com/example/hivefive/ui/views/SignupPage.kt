package com.example.hivefive.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun SignupPage(navController: NavHostController) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }

    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun isValidPassword(password: String): Boolean {
        val uppercase = password.count { it.isUpperCase() } >= 1
        val lowercase = password.count { it.isLowerCase() } >= 1
        val specialChar = password.count { it in "!@#$%^&*()-_=+[]{};:'\",.<>?/`~" } >= 1
        val digit = password.count { it.isDigit() } >= 1
        val length = password.length in 15..20
        return uppercase && lowercase && specialChar && digit && length
    }

    Column(
        modifier = Modifier
            .padding(28.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome Again", color = Color.Red, fontSize = 26.sp)
        Text(text = "Signup to continue", color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(52.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            value = email,
            onValueChange = {
                email = it
                isEmailValid = emailPattern.matches(it)
            },
            label = { Text("Email") },
            isError = !isEmailValid,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            trailingIcon = {
                if (isEmailValid) {
                    Icon(Icons.Default.Check, contentDescription = "Valid", tint = Color.Green)
                } else {
                    Icon(Icons.Default.Warning, contentDescription = "Invalid", tint = Color.Red)
                }
            }
        )
        if (!isEmailValid) {
            Text(
                text = "Invalid email format",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            value = password,
            onValueChange = {
                password = it
                isPasswordValid = isValidPassword(it)
            },
            label = { Text("Password") },
            isError = !isPasswordValid,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        if (!isPasswordValid) {
            Text(
                text = "Password must have 15+ characters, 1 uppercase, 1 lowercase, 1 digits, 1 special characters",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            if (isPasswordValid && isEmailValid && password.isNotEmpty() && email.isNotEmpty()) {
                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val firestore = FirebaseFirestore.getInstance()
                        insertData(firestore, email, password) { callBack ->
                            if (callBack) {
                                Toast.makeText(context, "Signup Successfully.", Toast.LENGTH_LONG).show()
                                navController.navigate("home") {
                                    popUpTo("signup") {
                                        inclusive = true
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Invalid User.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Something went wrong, ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }) {
            Text(text = "Signup")
        }
    }
}

fun insertData(firestore: FirebaseFirestore, email: String, password: String, callback: (Boolean) -> Unit) {
    val user = hashMapOf(
        "email" to email,
        "password" to password,
        "status" to false,
        "createdAt" to LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
    )

    firestore.collection("users").document(email).set(user)
        .addOnSuccessListener {
            callback(true)
        }
        .addOnFailureListener {
            callback(false)
        }
}
