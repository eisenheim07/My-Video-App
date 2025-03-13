package com.example.hivefive.ui.views

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.hivefive.helper.FirestoreUtils
import com.example.hivefive.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

@Composable
fun HomePage(navController: NavHostController, auth: FirebaseAuth) {
    FirestoreUtils.updateUserStatus(true)

    val url = URL("https://meet.ffmuc.net/")
    val options = JitsiMeetConferenceOptions.Builder()
        .setServerURL(url)
        .build()
    JitsiMeet.setDefaultConferenceOptions(options)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${auth.currentUser?.email}",
                color = Color.Red,
                fontSize = 26.sp,
                modifier = Modifier.wrapContentWidth(Alignment.Start)
            )

            IconButton(onClick = {
                auth.signOut()
            }) {
                Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "Sign Out")
            }
        }
        AllUsers(auth)
    }
}

@Composable
fun AllUsers(auth: FirebaseAuth) {
    var usersList by remember { mutableStateOf<List<User>>(emptyList()) }

    LaunchedEffect(Unit) {
        getUsersStream().collectLatest { users ->
            usersList = users
        }
    }

    LazyColumn {
        items(usersList) { user ->
            if (user.email != auth.currentUser?.email)
                UserItem(user)
        }
    }
}

@Composable
fun UserItem(user: User) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.email,
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp
            )
            IconButton(onClick = {
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom("user")
                    .build()
                JitsiMeetActivity.launch(context, options)
            }) {
                Icon(imageVector = Icons.Filled.VideoCall, contentDescription = "")
            }
        }
    }
}

fun getUsersStream(): Flow<List<User>> {
    val db = FirebaseFirestore.getInstance()

    return callbackFlow {
        val listener = db.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
//                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.map { doc ->
                    User("", doc.data?.get("email") as String)
                } ?: emptyList()

                trySend(users)
            }

        awaitClose { listener.remove() }
    }
}