package com.example.notepadmvvm.view


import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notepadmvvm.model.OnboardPage
import com.example.notepadmvvm.modelview.OnboardingViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch



@Composable
fun OnboardScreen (
    viewModel: OnboardingViewModel,
    navController: NavController,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val onboardPages = viewModel.onboardPages
    val showOnboarding by viewModel.showOnboarding.collectAsState()

    if (!showOnboarding) {
        onFinish()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(
                    state = pagerState,
                    count = onboardPages.size,
                    modifier = Modifier.weight(1f)
                        .background(MaterialTheme.colorScheme.background)
                ) { page ->
                    OnboardingPageView(onboardPage = onboardPages[page])
                }

//                PagerIndicator(
//                    pageCount = onboardPages.size,
//                    currentPage = pagerState.currentPage
//                ) { page ->
//                    coroutineScope.launch {
//                        pagerState.animateScrollToPage(page)
//                    }
//                }
            }
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < onboardPages.size - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            viewModel.completeOnboarding()
                            onFinish()
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.background, // Arka plan rengi
                elevation = FloatingActionButtonDefaults.elevation(0.dp), // Gölgeyi kaldırır
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {

                if (pagerState.currentPage < onboardPages.size - 1) {
                    Icon(
                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

            }

        }
    }
}
    @Composable
    fun OnboardingPageView(onboardPage: OnboardPage) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(onboardPage.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.4f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = onboardPage.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = onboardPage.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
//
//@Composable
//fun PagerIndicator(
//    pageCount: Int,
//    currentPage: Int,
//    onPageSelected: (Int) -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.Center
//    ) {
//        repeat(pageCount) { index ->
//            Box(
//                modifier = Modifier
//                    .size(8.dp)
//                    .padding(4.dp)
//                    .background(
//                        if (index == currentPage) MaterialTheme.colorScheme.primary
//                        else MaterialTheme.colorScheme.onPrimary
//                    )
//            )
//        }
//    }
//}