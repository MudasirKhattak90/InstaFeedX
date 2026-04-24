package com.example.instafeedx.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class PostRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getPosts(): List<Post> {
        return try {
            val snapshot = db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.map { doc ->
                Post(
                    id = doc.id,
                    uid = doc.getString("uid") ?: "",
                    username = doc.getString("username") ?: "user",
                    title = doc.getString("caption") ?: "",
                    url = doc.getString("imageUrl") ?: "",
                    timestamp = doc.getLong("timestamp") ?: 0L,
                    likes = (doc.getLong("likes") ?: 0L).toInt()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }

    }
}
