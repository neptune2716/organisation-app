package com.example.organisationapp.data.repository

import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.user.UserInfo
import com.example.organisationapp.data.supabase.SupabaseClient
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository {
    
    private val client = SupabaseClient.client
    
    /**
     * Connexion avec email et mot de passe
     */
    suspend fun signInWithEmail(email: String, password: String): Result<UserInfo?> {
        return try {
            client.gotrue.loginWith(Email) {
                this.email = email
                this.password = password
            }
            // After successful login, get the current user
            val user = client.gotrue.currentUserOrNull()
            Result.success(user)
        } catch (e: Exception) {
            // Améliorer les messages d'erreur
            val errorMessage = when {
                e.message?.contains("Email not confirmed", ignoreCase = true) == true -> 
                    "Votre compte n'est pas encore vérifié. Veuillez consulter votre boîte email et cliquer sur le lien de confirmation pour activer votre compte."
                e.message?.contains("Invalid login credentials", ignoreCase = true) == true -> 
                    "Email ou mot de passe incorrect. Veuillez vérifier vos informations."
                e.message?.contains("Too many requests", ignoreCase = true) == true -> 
                    "Trop de tentatives de connexion. Veuillez attendre quelques minutes avant de réessayer."
                else -> e.message ?: "Erreur de connexion inconnue"
            }
            Result.failure(Exception(errorMessage))
        }
    }
    
    /**
     * Inscription avec email, mot de passe et pseudo
     */
    suspend fun signUpWithEmail(email: String, password: String, username: String? = null): Result<UserInfo?> {
        return try {
            client.gotrue.signUpWith(Email) {
                this.email = email
                this.password = password
                // Ajouter le pseudo dans les métadonnées utilisateur
                username?.let {
                    data = buildJsonObject {
                        put("username", it)
                        put("display_name", it) // Utiliser display_name pour plus de compatibilité
                        put("full_name", it) // Ajouter aussi full_name
                    }
                }
            }
            // After successful signup, get the current user
            val user = client.gotrue.currentUserOrNull()
            Result.success(user)
        } catch (e: Exception) {
            // Améliorer les messages d'erreur pour l'inscription
            val errorMessage = when {
                e.message?.contains("User already registered", ignoreCase = true) == true -> 
                    "Un compte avec cet email existe déjà. Essayez de vous connecter ou utilisez un autre email."
                e.message?.contains("Password should be at least", ignoreCase = true) == true -> 
                    "Le mot de passe doit contenir au moins 6 caractères."
                e.message?.contains("Unable to validate email address", ignoreCase = true) == true -> 
                    "Adresse email invalide. Veuillez vérifier le format de votre email."
                else -> e.message ?: "Erreur d'inscription inconnue"
            }
            Result.failure(Exception(errorMessage))
        }
    }
    
    /**
     * Connexion avec Google
     */
    suspend fun signInWithGoogle(): Result<UserInfo?> {
        return try {
            client.gotrue.loginWith(Google)
            val user = client.gotrue.currentUserOrNull()
            
            // Vérifier si l'authentification a réellement réussi
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Échec de l'authentification Google"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Déconnexion
     */
    suspend fun signOut(): Result<Unit> {
        return try {
            client.gotrue.logout()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtenir l'utilisateur actuel
     */
    fun getCurrentUser() = client.gotrue.currentUserOrNull()
    
    /**
     * Vérifier si l'utilisateur est connecté
     */
    fun isLoggedIn() = client.gotrue.currentUserOrNull() != null
    
    /**
     * Observer les changements d'état d'authentification
     */
    fun getAuthStateFlow() = client.gotrue.sessionStatus
    
    /**
     * Renvoyer l'email de confirmation
     */
    suspend fun resendConfirmationEmail(email: String): Result<Unit> {
        return try {
            // For gotrue-kt 1.4.7, we may need to use a different approach
            // This functionality might not be available in this version
            Result.failure(Exception("Fonctionnalité non disponible dans cette version"))
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("Unable to validate email address", ignoreCase = true) == true -> 
                    "Adresse email invalide."
                e.message?.contains("Too many requests", ignoreCase = true) == true -> 
                    "Trop de demandes. Veuillez attendre avant de renvoyer l'email."
                else -> e.message ?: "Erreur lors de l'envoi de l'email de confirmation"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    /**
     * Mettre à jour le pseudo de l'utilisateur
     */
    suspend fun updateUsername(newUsername: String): Result<UserInfo?> {
        return try {
            // For gotrue-kt 1.4.7, user update functionality might be limited
            // This functionality might not be available in this version
            Result.failure(Exception("Fonctionnalité non disponible dans cette version"))
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("User not found", ignoreCase = true) == true -> 
                    "Utilisateur non trouvé. Veuillez vous reconnecter."
                e.message?.contains("Invalid JWT", ignoreCase = true) == true -> 
                    "Session expirée. Veuillez vous reconnecter."
                else -> e.message ?: "Erreur lors de la mise à jour du pseudo"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    /**
     * Mettre à jour l'email de l'utilisateur
     */
    suspend fun updateEmail(newEmail: String): Result<UserInfo?> {
        return try {
            // For gotrue-kt 1.4.7, user update functionality might be limited
            // This functionality might not be available in this version
            Result.failure(Exception("Fonctionnalité non disponible dans cette version"))
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("Unable to validate email address", ignoreCase = true) == true -> 
                    "Adresse email invalide. Veuillez vérifier le format."
                e.message?.contains("Email already exists", ignoreCase = true) == true -> 
                    "Cette adresse email est déjà utilisée par un autre compte."
                e.message?.contains("User not found", ignoreCase = true) == true -> 
                    "Utilisateur non trouvé. Veuillez vous reconnecter."
                e.message?.contains("Invalid JWT", ignoreCase = true) == true -> 
                    "Session expirée. Veuillez vous reconnecter."
                else -> e.message ?: "Erreur lors de la mise à jour de l'email"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    /**
     * Mettre à jour le mot de passe de l'utilisateur
     */
    suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            // For gotrue-kt 1.4.7, password update functionality might be limited
            // This functionality might not be available in this version
            Result.failure(Exception("Fonctionnalité non disponible dans cette version"))
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("Password should be at least", ignoreCase = true) == true -> 
                    "Le mot de passe doit contenir au moins 6 caractères."
                e.message?.contains("User not found", ignoreCase = true) == true -> 
                    "Utilisateur non trouvé. Veuillez vous reconnecter."
                e.message?.contains("Invalid JWT", ignoreCase = true) == true -> 
                    "Session expirée. Veuillez vous reconnecter."
                else -> e.message ?: "Erreur lors de la mise à jour du mot de passe"
            }
            Result.failure(Exception(errorMessage))
        }
    }
}
