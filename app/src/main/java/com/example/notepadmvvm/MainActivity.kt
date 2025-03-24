package com.example.notepadmvvm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.EditPlan
import com.example.notepadmvvm.AddAndEditScreen.viewModel.GenericTextFieldViewModel
import com.example.notepadmvvm.mainScreen.view.MainScreen
import com.example.notepadmvvm.mainScreen.viewModel.PersonViewModel
import com.example.notepadmvvm.ui.theme.NotepadMVVMTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: PersonViewModel

    // Hilt tarafından enjekte edilen bağımlılık
    @Inject
    lateinit var myDependency: MyDependency

    class MyDependency @Inject constructor() {
        // MyDependency'in içerikleri
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            NotepadMVVMTheme {
                val navController = rememberNavController()
                Scaffold(
                    content = {
                        Box {
                            NavHost(
                                navController = navController,
                                startDestination = "main_screen"
                            ) {
                                composable("main_screen") {
                                    MainScreen(navController)
                                }
                                composable("edit_plan/{id}") { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                                    id?.let {
                                        EditPlan(navController, id )
                                    }
                                }
                                composable(getString(R.string.save_page)) {
                                    MainScreen(navController)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
