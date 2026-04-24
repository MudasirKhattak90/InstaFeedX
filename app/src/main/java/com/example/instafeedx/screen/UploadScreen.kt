package com.example.instafeedx.screen

import com.google.firebase.firestore.FirebaseFirestore
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

// colors
private val BgDark = Color(0xFF0D0D12)
private val AccentStart = Color(0xFF7B5EA7)
private val AccentEnd = Color(0xFF4F8EF7)
private val TextPrimary = Color(0xFFF1F1F5)
private val TextHint = Color(0xFF6B6B80)
private val FieldBg = Color(0xFF1E1E2A)
private val FieldBorder = Color(0xFF2A2A3A)


@Composable
fun UploadScreen() {
    var caption by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }
    var uploadSuccess by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val randomSeed by remember { mutableIntStateOf((1..1000).random()) }
    val previewUrl = "https://picsum.photos/seed/$randomSeed/600/600"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)

    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopStart)
                .offset(x = (-60).dp, y = (-60).dp)
                .background(
                    Brush.radialGradient(
                        listOf(AccentEnd.copy(alpha = 0.10f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Top Bar
            Text(
                text = "New Post",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)

            )
            Spacer(modifier = Modifier.height(8.dp))

            // Image Picker Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        brush =
                            Brush.radialGradient(listOf(AccentStart, AccentEnd)),
                        shape = RoundedCornerShape(16.dp)

                    ),
                contentAlignment = Alignment.Center


            ) {
                AsyncImage(
                    model = previewUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FieldBg.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddAPhoto,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.0f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Caption Field
            OutlinedTextField(
                value = caption,
                onValueChange = { caption = it },
                label = { Text("Write a caption...", color = TextHint) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(14.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentStart,
                    unfocusedBorderColor = FieldBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = AccentStart,
                    focusedContainerColor = FieldBg,
                    unfocusedContainerColor = FieldBg
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                if (caption.isBlank()) {
                    errorMsg = "Enter your caption"
                    return@Button
                }
                isUploading = true
                errorMsg = null

                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                val username = FirebaseAuth.getInstance().currentUser?.email
                    ?.substringBefore("@") ?: "user"

                val post = hashMapOf(
                    "uid" to uid,
                    "username" to username,
                    "imageUrl" to previewUrl,
                    "caption" to caption,
                    "likes" to 0,
                    "timestamp" to System.currentTimeMillis()
                )

                FirebaseFirestore.getInstance()
                    .collection("posts")
                    .add(post)
                    .addOnSuccessListener {
                        isUploading = false
                        uploadSuccess = true
                        caption = ""
                    }
                    .addOnFailureListener { e ->
                        isUploading = false
                        errorMsg = e.message ?: "Post cannot share"
                    }


            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = !isUploading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (!isUploading)
                                Brush.radialGradient(listOf(AccentStart, AccentEnd))
                            else
                                Brush.radialGradient(listOf(TextHint, TextHint)),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    else {
                        Text(
                            text = "Share Post",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))

            // FeedBack
            if (uploadSuccess){
                Text(
                    text = "Post share Successfully",
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            errorMsg?.let {
                Text(
                    text = it,
                    color = Color(0xFFFF5C5C),
                    fontSize = 13.sp
                )
            }



        }
    }

}

