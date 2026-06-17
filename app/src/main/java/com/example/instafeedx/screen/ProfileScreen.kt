package com.example.instafeedx.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.autofill.ContentDataType.Companion.List
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.instafeedx.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.List

// Colors
private val BgDark = Color(0xFF0D0D12)
private val CardBg = Color(0xFF16161F)
private val AccentStart = Color(0xFF7B5EA7)
private val AccentEnd = Color(0xFF4F8EF7)
private val TextPrimary = Color(0xFFF1F1F5)
private val TextHint = Color(0xFF6B6B80)


@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {}
) {
    val user = FirebaseAuth.getInstance().currentUser
    val displayName = user?.displayName?.takeIf { it.isNotBlank() } ?: "User"
    val email = user?.email ?: ""
    val photoUrl = user?.photoUrl?.toString()
    val uid = user?.uid ?: ""

    // User posts from FireStore
    var userPosts by remember { mutableStateOf<kotlin.collections.List<Pair<String, String>>>(emptyList()) }
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uid) {
        if (uid.isNotBlank()) {
            FirebaseFirestore.getInstance()
                .collection("posts")
                .whereEqualTo("uid", uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener
                    if (snapshot != null) {
                        userPosts = snapshot.documents.mapNotNull { doc ->
                            doc.getString("imageUrl") ?: return@mapNotNull null
                            Pair(doc.id, doc.getString("imageUrl") ?: ""  )
                        }
                    }
                }
        }
    }

    selectedImageUrl?.let { imageUrl ->
        Dialog(
            onDismissRequest = { selectedImageUrl = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = {selectedImageUrl = null},
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-100).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentStart.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-80).dp, y = 80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentEnd.copy(alpha = 0.12f), Color.Transparent)
                    )
                )
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(48.dp))

                // Top Bar

                Text(
                    text = "Profile",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Avatar
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(listOf(AccentStart, AccentEnd)),
                            shape = CircleShape
                        )
                ) {
                    if (photoUrl != null) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(CardBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = displayName,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Email
                Text(
                    text = email,
                    color = TextHint,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Stats Row

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBg)
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(value = "${userPosts.size}", label = "posts")
                    VerticalDivider()
                    StatItem(value = "0", label = "Follower")
                    VerticalDivider()
                    StatItem(value = "0", label = "Following")
                }
                Spacer(modifier = Modifier.height(32.dp))
                // LogOut Button
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(listOf(AccentStart, AccentEnd)),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Logout",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // posts Grid
                if (userPosts.isEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = "No posts yet",
                            color = TextHint,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Text(
                        text = "My Posts",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // column grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(((((userPosts.size + 2) / 3) * 130).dp)),
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                        userScrollEnabled = false
                    ) {
                        items(userPosts) { (postId, imageUrl) ->
                            Box {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(4.dp)).
                                        clickable{ selectedImageUrl = imageUrl },
                                    contentScale = ContentScale.Crop
                                )
                                IconButton(
                                    onClick = {
                                        FirebaseFirestore.getInstance()
                                            .collection("posts")
                                            .document(postId)
                                            .delete()
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    )
                                }
                            }

                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = Color(0xFFF1F1F5),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold

        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = Color(0xFF6B6B80),
            fontSize = 12.sp
        )

    }

}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(Color(0xFF2A2A3A))
    )

}