package com.example.instafeedx.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instafeedx.data.Post
import com.example.instafeedx.data.PostRepository
import com.example.instafeedx.data.RetrofitInstance
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val repository = PostRepository()

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set




    init {
        loadPost()
    }


    fun toggleLike(postId: String) {
        posts = posts.map { post ->
            if (post.id == postId) {
                val newLikeState = !post.isLiked
                post.copy(
                    isLiked = newLikeState,
                    likes = if (newLikeState) post.likes + 1 else post.likes - 1
                )

            } else {
                post
            }

        }
    }

    fun loadPost() {
        if (isLoading) return

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                posts = repository.getPosts()

            } catch (e: Exception) {
                errorMessage = "No internet connection"
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            try {

                isLoading = true
                errorMessage = null
                posts = repository.getPosts()

            } catch (e: Exception) {
                errorMessage = "No internet connection"
            } finally {
                isLoading = false
            }
        }

    }

}