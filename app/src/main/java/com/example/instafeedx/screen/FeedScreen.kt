package com.example.instafeedx.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instafeedx.component.ErrorView
import com.example.instafeedx.viewmodel.PostViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun FeedScreen(viewModel: PostViewModel = viewModel()) {

    val isRefreshing = viewModel.isLoading

    Column {
        // Instagram style top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "InstaFeedX",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
        HorizontalDivider(thickness = 0.5.dp)
    }

    if (viewModel.errorMessage != null && viewModel.posts.isEmpty()) {
        ErrorView(
            message = viewModel.errorMessage!!,
            onRetry = { viewModel.loadPost() }
        )
        return

    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            viewModel.refreshPosts()
        }
    ) {


        LazyColumn {

            if (viewModel.posts.isEmpty() && viewModel.isLoading) {
                items(5) {
                    ShimmerPostItem()
                }
            }

            items(viewModel.posts) { post ->
                PostItem(
                    post = post,
                    onLikeClick = { postId ->
                        viewModel.toggleLike(postId.toString())

                    }
                )


            }

            item {
                if (
                    viewModel.isLoading
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }


        }

    }

}