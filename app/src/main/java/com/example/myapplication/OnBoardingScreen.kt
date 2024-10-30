//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import com.example.myapplication.OnboardingPage
//import com.example.myapplication.onboardingPages
//import com.google.accompanist.pager.*
//
//@OptIn(ExperimentalPagerApi::class)
//@Composable
//fun OnboardingScreen(onFinish: () -> Unit) {
//    val pagerState = rememberPagerState()
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
//    ) {
//        // Pager içeriklerini göstermek için
//        HorizontalPager(
//            count = onboardingPages.size,
//            state = pagerState
//        ) { page ->
//            OnboardingPageContent(page = onboardingPages[page])
//        }
//
//        // İlerleme göstergesi (sayfa göstergesi)
//
//      /*  HorizontalPagerIndicator(
//            pagerState = pagerState,
//            modifier = Modifier.padding(16.dp),
//            activeColor = Color.Blue
//        )
//
//       */
//
//        // "Devam et" düğmesi
//        Button(
//            onClick = {
//                if (pagerState.currentPage == onboardingPages.size - 1) {
//                    onFinish()
//                } else {
//                    // Bir sonraki sayfaya geçiş
//                //    pagerState.animateScrollToPage(pagerState.currentPage + 1)
//                }
//            },
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(text = if (pagerState.currentPage == onboardingPages.size - 1) "Finish" else "Next")
//        }
//    }
//}
//
//@Composable
//fun OnboardingPageContent(page: OnboardingPage) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Image(
//            painter = painterResource(id = page.imageResId),
//            contentDescription = null,
//            modifier = Modifier.height(200.dp),
//            contentScale = ContentScale.Fit
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = page.title)
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(text = page.description)
//    }
//}
