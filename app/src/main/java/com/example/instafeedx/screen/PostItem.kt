package com.example.instafeedx.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.instafeedx.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Colors
private val CardBg = Color(0xFF16161F)
private val TextPrimary = Color(0xFFF1F1F5)
private val TextHint = Color(0xFF6B6B80)
private val FieldBg = Color(0xFF1E1E2A)
private val FieldBorder = Color(0xFF2A2A3A)
private val AccentStart = Color(0xFF7B5EA7)
private val AccentEnd = Color(0xFF4F8EF7)
private val AvatarBg = Color(0xFF2A2A3A)


@Composable
fun PostItem(
    post: Post, onLikeClick: (String) -> Unit
) {
    // Format timestamp
    val timeText = if (post.timestamp > 0L) {
        val sdf = SimpleDateFormat("MM d", Locale.getDefault())
        sdf.format(Date(post.timestamp))
    } else {
        "just now"
    }

    var showComments by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var isPostingComment by remember { mutableStateOf(false) }

    LaunchedEffect(post.id) {
        if (post.id.isNotBlank()) {
            FirebaseFirestore.getInstance().collection("posts").document(post.id)
                .collection("comments").orderBy("timestamp").addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        comments = snapshot.documents.map { doc ->
                            val username = doc.getString("username") ?: "user"
                            val text = doc.getString("text") ?: ""
                            Pair(username, text)
                        }
                    }
                }

        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, top = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column {

            // Header

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        "username", fontWeight = FontWeight.SemiBold, fontSize = 16.sp
                    )

                    Text(
                        text = "just now",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )

                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "...", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), shape = RoundedCornerShape(0.dp)
            ) {

                AsyncImage(
                    model = "https://picsum.photos/seed/${post.id}/600/600",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    contentScale = ContentScale.Crop
                )

            }


            // Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onLikeClick(post.id)
                    }) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.Favorite
                        else

                            Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) Color.Red else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(26.dp)
                    )
                }

                IconButton(onClick = { showComments = !showComments }) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = if (showComments) AccentStart else TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Share",
                        modifier = Modifier.size(24.dp)
                    )
                }


            }

            // Likes Count
            if (post.likes > 0) {
                Text(
                    text = "${post.likes}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            if (post.title.isNotBlank()) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.SemiBold, color = TextPrimary
                            )
                        ) {
                            append(post.username.ifBlank { "user" })
                        }
                        append(" ")
                        withStyle(SpanStyle(color = TextPrimary)) {
                            append(post.title)
                        }
                    },
                    fontSize = 13.sp,
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

            }

            if (showComments) {
                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(thickness = 0.5.dp, color = FieldBorder)

                Spacer(modifier = Modifier.height(8.dp))

                if (comments.isEmpty()) {
                    Text(
                        text = "No comment yet - be the first",
                        color = TextHint,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                } else {
                    comments.forEach { (username, text) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        SpanStyle(
                                            fontWeight = FontWeight.SemiBold, color = TextPrimary
                                        )
                                    ) {
                                        append(username)
                                    }
                                    append(" ")
                                    withStyle(SpanStyle(color = TextPrimary)) {
                                        append(text)
                                    }
                                }, fontSize = 13.sp
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // comment input Field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = {
                            Text("Add a comment...", color = TextHint, fontSize = 12.sp)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (commentText.isNotBlank() && !isPostingComment) {
                                    isPostingComment = true
                                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                                        ?: return@KeyboardActions
                                    val username =
                                        FirebaseAuth.getInstance().currentUser?.email?.substringBefore(
                                                "@"
                                            ) ?: ""

                                    val comment = hashMapOf(
                                        "uid" to uid,
                                        "username" to username,
                                        "text" to commentText,
                                        "timestamp" to System.currentTimeMillis()
                                    )

                                    FirebaseFirestore.getInstance().collection("posts")
                                        .document(post.id).collection("comments").add(comment)
                                        .addOnSuccessListener {
                                            commentText = ""
                                            isPostingComment = false
                                        }.addOnFailureListener {
                                            isPostingComment = false
                                        }
                                }
                            }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentStart,
                            unfocusedBorderColor = FieldBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = AccentStart,
                            focusedContainerColor = FieldBg
                        ))
                    Spacer(modifier = Modifier.width(8.dp))

                    // Send Button
                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank() && !isPostingComment) {
                                isPostingComment = true
                                val uid =
                                    FirebaseAuth.getInstance().currentUser?.uid ?: return@IconButton
                                val username =
                                    FirebaseAuth.getInstance().currentUser?.email?.substringBefore("@")
                                        ?: "user"

                                val comment = hashMapOf(
                                    "uid" to uid,
                                    "username" to username,
                                    "text" to commentText,
                                    "timestamp" to System.currentTimeMillis()
                                )
                                FirebaseFirestore.getInstance().collection("posts")
                                    .document(post.id).collection("comments").add(comment)
                                    .addOnSuccessListener {
                                        commentText = ""
                                        isPostingComment = false
                                    }.addOnFailureListener {
                                        isPostingComment = false
                                    }
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "send",
                            tint = AccentStart,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

                //Divider
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp, color = FieldBorder
                )


            }


        }
    }

}