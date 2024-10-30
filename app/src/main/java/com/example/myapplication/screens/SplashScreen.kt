package com.example.myapplication.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.ui.theme.DividerColor
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val firstScreenRoute = getString(R.string.first_page)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black,
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            NavHost(
                                navController = navController,
                                startDestination = firstScreenRoute
                            ) {
                                composable(firstScreenRoute) {
                                    SplashScreen(navController = navController)
                                }

                                composable("edit_plan/$id") { backStackEntry ->
                                    val id =
                                        backStackEntry.arguments?.getString("id")?.toLongOrNull()
                                    id?.let {
                                        EditPlan(navController = navController, id = it)
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun SplashScreen(navController: NavController) {
        // Retrieve the context early
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.mipmap.pencil), // Ensure this is a valid resource
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier.size(64.dp) // Adjust logo size
            )
        }

        // Navigate to IntroScreens after a delay
        LaunchedEffect(Unit) {
            delay(1500) // Delay for splash screen
            context.startActivity(Intent(context, IntroScreens::class.java))
            (context as? Activity)?.finish() // Ensure the splash activity finishes
        }
    }

}


