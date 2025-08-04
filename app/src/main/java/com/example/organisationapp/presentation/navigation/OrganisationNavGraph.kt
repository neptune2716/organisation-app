package com.example.organisationapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.organisationapp.presentation.screens.home.HomeScreen
import com.example.organisationapp.presentation.screens.tasks.TasksScreen
import com.example.organisationapp.presentation.screens.calendar.CalendarScreen
import com.example.organisationapp.presentation.screens.habits.HabitsScreen
import com.example.organisationapp.presentation.screens.notes.NotesScreen
import com.example.organisationapp.presentation.screens.sport.SportScreen
import com.example.organisationapp.presentation.screens.screentime.ScreenTimeScreen
import com.example.organisationapp.presentation.screens.profile.ProfileScreen
import com.example.organisationapp.presentation.screens.settings.SettingsScreen
import com.example.organisationapp.presentation.screens.help.HelpScreen
import com.example.organisationapp.presentation.screens.auth.SignInScreen
import com.example.organisationapp.presentation.screens.auth.SignUpScreen
import com.example.organisationapp.presentation.viewmodel.AuthViewModel

@Composable
fun OrganisationNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    // ViewModel partagé pour l'authentification
    val authViewModel: AuthViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToTasks = { navController.navigate(Screen.Tasks.route) },
                onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) },
                onNavigateToHabits = { navController.navigate(Screen.Habits.route) },
                onNavigateToNotes = { navController.navigate(Screen.Notes.route) },
                onNavigateToSport = { navController.navigate(Screen.Sport.route) },
                onNavigateToScreenTime = { navController.navigate(Screen.ScreenTime.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) },
                authViewModel = authViewModel
            )
        }
        
        composable(Screen.SignIn.route) {
            SignInScreen(
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                onSignInSuccess = { 
                    navController.popBackStack(Screen.Home.route, false)
                },
                onNavigateBack = { navController.popBackStack() },
                authViewModel = authViewModel
            )
        }
        
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) },
                onSignUpSuccess = { 
                    navController.popBackStack(Screen.Home.route, false)
                },
                onNavigateBack = { navController.popBackStack() },
                authViewModel = authViewModel
            )
        }
        
        composable(Screen.Tasks.route) {
            TasksScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Calendar.route) {
            CalendarScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Habits.route) {
            HabitsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Notes.route) {
            NotesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Sport.route) {
            SportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.ScreenTime.route) {
            ScreenTimeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSignIn = { 
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                authViewModel = authViewModel
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Help.route) {
            HelpScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
