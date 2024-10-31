package com.example.myapplication.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.db.MainActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

// Main Activity Class
class IntroScreens : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val firstScreenRoute = getString(R.string.first_page)

                // Retrieve onboarding status from SharedPreferences
                val sharedPreferences = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
                var showOnboarding by remember {
                    mutableStateOf(sharedPreferences.getBoolean("onboarding_shown", true))
                }

                if (showOnboarding) {
                    NavHost(
                        navController = navController,
                        startDestination = firstScreenRoute
                    ) {
                        // Onboarding screen route
                        composable(firstScreenRoute) {
                            OnboardScreen(
                                navController = navController,
                                onboardPages = getOnboardPages(),
                                applicationContext = applicationContext,
                                onFinish = {
                                    // Update SharedPreferences to indicate onboarding is completed
                                    sharedPreferences.edit().putBoolean("onboarding_shown", false).apply()
                                    showOnboarding = false // Update the state
                                    startActivity(Intent(applicationContext, MainActivity::class.java))
                                    finish()
                                }
                            )
                        }
                    }
                } else {
                    // Directly navigate to MainActivity if onboarding is already shown
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    // Method to initialize the onboarding pages list
    fun getOnboardPages(): List<OnboardPage> {
        return listOf(
            OnboardPage(
                imageRes = R.mipmap.screen7,
                title = getString(R.string.intro1),
                description = getString(R.string.intro1_1).uppercase()
            ),
            OnboardPage(
                imageRes = R.mipmap.screen1,
                title = getString(R.string.intro2),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen2,
                title = getString(R.string.intro3),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen3,
                title = getString(R.string.intro4),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen4,
                title = getString(R.string.intro5),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen6,
                title = getString(R.string.intro6),
                description = ""
            ),
            OnboardPage(
                imageRes = R.mipmap.screen8,
                title = getString(R.string.intro7),
                description = ""
            )
        )
    }

    // Data class representing each onboarding page
    data class OnboardPage(
        val imageRes: Int,
        val title: String,
        val description: String
    )

    // Onboarding Image View
    @Composable
    fun OnBoardImageView(modifier: Modifier = Modifier, imageRes: Int) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp * 0.4).dp),
            contentScale = ContentScale.Crop
        )
    }

    // Onboarding Page Details
    @Composable
    fun OnBoardDetails(modifier: Modifier = Modifier, currentPage: OnboardPage) {
        Column(
            modifier = Modifier.background(Color.Black)
        ) {
            Spacer(modifier = Modifier.padding(36.dp))
            Text(
                text = currentPage.title,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = currentPage.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
        }
    }

    // Tab Selector
    @Composable
    fun TabSelector(
        onboardPages: List<OnboardPage>,
        currentPage: Int,
        onTabSelected: (Int) -> Unit
    ) {
        TabRow(
            selectedTabIndex = currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black),
            contentColor = Color.Black
        ) {
            onboardPages.forEachIndexed { index, _ ->
                Tab(
                    selected = index == currentPage,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.background(Color.Black)
                )
            }
        }
    }

    // Onboarding Screen
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun OnboardScreen(
        navController: NavController,
        onboardPages: List<OnboardPage>,
        applicationContext: Context,
        onFinish: () -> Unit
    ) {
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val screenHeight = configuration.screenHeightDp.dp

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .align(Alignment.TopStart)
            ) {
                HorizontalPager(
                    state = pagerState,
                    count = onboardPages.size,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) { page ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = (screenWidth * 0.05f)),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OnBoardDetails(
                            modifier = Modifier
                                .padding(vertical = (screenHeight * 0.05f))
                                .fillMaxWidth()
                                .weight(0.2f),
                            currentPage = onboardPages[page]
                        )

                        Spacer(Modifier.height((screenHeight * 0.1f)))

                        OnBoardImageView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.8f),
                            imageRes = onboardPages[page].imageRes
                        )
                    }
                }

                TabSelector(
                    onboardPages = onboardPages,
                    currentPage = pagerState.currentPage
                ) { page ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page)
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < onboardPages.size - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            onFinish() // Trigger onFinish callback when onboarding completes
                        }
                    }
                },
                containerColor = Color.Transparent,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = (screenWidth * 0.05f), bottom = (screenHeight * 0.05f))
                    .size(56.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                if (pagerState.currentPage < onboardPages.size - 1) {
                    Icon(
                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.next),
                        tint = Color.White
                    )
                } else {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = stringResource(R.string.finish),
                        tint = Color.White
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MyApplicationTheme {
            val navController = rememberNavController()
            OnboardScreen(navController, getOnboardPages(), LocalContext.current, onFinish = {})
        }
    }
}
