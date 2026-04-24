package com.example.instafeedx.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp.Companion.Unspecified
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.instafeedx.screen.FeedScreen
import com.example.instafeedx.screen.LoginScreen
import com.example.instafeedx.screen.ProfileScreen
import com.example.instafeedx.screen.UploadScreen
import com.example.instafeedx.viewmodel.AuthState
import com.example.instafeedx.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException

// Colors(same as Login Screen theme)

private val BgDark = Color(0xFF0D0D12)
private val AccentStart = Color(0xFF7B5EA7)
private val TextHint = Color(0xFF6B6B80)
private val NavBg = Color(0xFF16161F)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(googleSignInClient: GoogleSignInClient) {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState = authViewModel.authState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                authViewModel.googleLogin(idToken)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom Nav Sirf in Screen pe Show hu ga
    val showBottomNav = currentRoute in listOf(
        Screen.Feed.route,
        Screen.Upload.route,
        Screen.Profile.route
    )
    Scaffold(
        containerColor = BgDark,
        bottomBar = {
            if (showBottomNav){
                NavigationBar(
                    containerColor = NavBg,
                    tonalElevation = Unspecified
                ) {
                    bottomNavigationItem.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route){
                                    popUpTo(navController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected)
                                    item.selectedIcon else item.selectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AccentStart,
                                selectedTextColor = TextHint,
                                unselectedIconColor = TextHint,
                                unselectedTextColor = TextHint,
                                indicatorColor = AccentStart.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)


        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onGoogleClick = {
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                    onLoginSuccess = {
                        navController.navigate(Screen.Feed.route) {
                            popUpTo(Screen.Login.route) { inclusive = true

                            }
                        }
                    },
                    viewModel = authViewModel
                )
            }
            composable(Screen.Feed.route) {
                FeedScreen()
            }
            composable(Screen.Upload.route) {
                UploadScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }

    }





}