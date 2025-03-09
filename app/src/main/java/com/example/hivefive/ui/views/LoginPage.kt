package com.example.hivefive.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hivefive.R


@Composable
fun LoginPage(navController: NavHostController) {
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
            placeholder = { Text(text = "Enter Email") },
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

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Don't Have Account ? ", color = Color.Black, fontSize = 12.sp)
            Button(onClick = {
                navController.navigate("signup")
            }) {
                Row(
                    modifier = Modifier
                        .height(22.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Sign Up here", color = Color.White, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(120.dp))

        Button(onClick = { }) {
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