package com.example.notepadmvvm


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notepadmvvm.mainScreen.view.MainScreen
import com.example.notepadmvvm.model.repository.OnboardingRepository
import com.example.notepadmvvm.modelview.OnboardingViewModel
import com.example.notepadmvvm.ui.theme.NotepadMVVMTheme
import com.example.notepadmvvm.view.OnboardScreen
import com.example.notepadmvvm.view.SplashScreen
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          //  val viewModel: PersonViewModel = hiltViewModel() // Hilt ile ViewModel çağrılır
          //  val personList by viewModel.personList.collectAsState()

            NotepadMVVMTheme {
                val navController = rememberNavController()


                Scaffold {
                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashScreen()
                        }
                        composable("main") {
                            // Ana ekran burada yönlendirilir
                            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                            finish()
                        }
                        composable("main_screen") {
                            MainScreen(navController = navController)
                        }
                        composable("intro") {
                            val repository = OnboardingRepository(applicationContext)
                            val viewModel = OnboardingViewModel(repository)

                            OnboardScreen(viewModel, navController) {}
                        }
                    }
                }
            }
        }
    }
}
