package com.example.instafeedx.data

data class Post(
    val id: String,
    val uid: String,
    val username: String,
    val timestamp: Long = 0L,
    val title: String,
    val url: String,
    var isLiked: Boolean = false,
    var likes: Int = 0
)
