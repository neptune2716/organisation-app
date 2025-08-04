package com.example.organisationapp.presentation.navigation

/**
 * Définition des écrans de l'application
 */
sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Accueil")
    object Tasks : Screen("tasks", "Tâches")
    object Calendar : Screen("calendar", "Calendrier")
    object Habits : Screen("habits", "Habitudes")
    object Notes : Screen("notes", "Notes")
    object Sport : Screen("sport", "Sport")
    object ScreenTime : Screen("screen_time", "Temps d'écran")
    object Profile : Screen("profile", "Profil")
    object Settings : Screen("settings", "Paramètres")
    object Help : Screen("help", "Aide")
    
    // Écrans d'authentification
    object SignIn : Screen("sign_in", "Connexion")
    object SignUp : Screen("sign_up", "Inscription")
}
