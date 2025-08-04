package com.example.organisationapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.organisationapp.data.repository.AuthRepository

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: UserInfo? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    
    private val authRepository = AuthRepository()
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        // Vérifier l'état d'authentification au démarrage
        checkAuthState()
        
        // Observer les changements d'état d'authentification
        viewModelScope.launch {
            authRepository.getAuthStateFlow().collect { sessionStatus ->
                _uiState.value = _uiState.value.copy(
                    user = authRepository.getCurrentUser(),
                    isLoggedIn = authRepository.isLoggedIn()
                )
            }
        }
    }
    
    private fun checkAuthState() {
        _uiState.value = _uiState.value.copy(
            user = authRepository.getCurrentUser(),
            isLoggedIn = authRepository.isLoggedIn()
        )
    }
    
    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signInWithEmail(email, password)
                .onSuccess { userInfo ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = userInfo,
                        isLoggedIn = true,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erreur de connexion"
                    )
                }
        }
    }
    
    fun signUpWithEmail(email: String, password: String, username: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signUpWithEmail(email, password, username)
                .onSuccess { userInfo ->
                    // Vérifier si l'utilisateur est connecté ou s'il doit confirmer son email
                    if (userInfo != null && authRepository.isLoggedIn()) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = userInfo,
                            isLoggedIn = true,
                            error = null
                        )
                    } else {
                        // L'inscription s'est bien passée mais l'utilisateur doit confirmer son email
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = null,
                            isLoggedIn = false,
                            error = "Inscription réussie ! Un email de vérification a été envoyé à votre adresse. Veuillez cliquer sur le lien dans l'email pour activer votre compte, puis revenez vous connecter."
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erreur d'inscription"
                    )
                }
        }
    }
    
    fun signInWithGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.signInWithGoogle()
                .onSuccess { userInfo ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = userInfo,
                        isLoggedIn = true,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erreur de connexion Google"
                    )
                }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            authRepository.signOut()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = null,
                        isLoggedIn = false,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erreur de déconnexion"
                    )
                }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resendConfirmationEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.resendConfirmationEmail(email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Email de confirmation renvoyé ! Vérifiez votre boîte de réception."
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erreur lors de l'envoi de l'email"
                    )
                }
        }
    }

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.updateUsername(newUsername)
                .onSuccess { updatedUser ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = updatedUser,
                        error = "Pseudo mis à jour avec succès !"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erreur lors de la mise à jour du pseudo"
                    )
                }
        }
    }

    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.updateEmail(newEmail)
                .onSuccess { updatedUser ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = updatedUser,
                        error = "Email mis à jour avec succès ! Un email de confirmation a été envoyé à votre nouvelle adresse."
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erreur lors de la mise à jour de l'email"
                    )
                }
        }
    }

    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.updatePassword(newPassword)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Mot de passe mis à jour avec succès !"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Erreur lors de la mise à jour du mot de passe"
                    )
                }
        }
    }
}

/**
 * Fonction utilitaire pour extraire le pseudo des métadonnées utilisateur
 */
fun UserInfo.getDisplayName(): String? {
    return userMetadata?.let { metadata ->
        // Essayer différentes clés dans l'ordre de priorité
        // Utiliser toString() et nettoyer les guillemets
        val rawValue = metadata["display_name"]?.toString()
            ?: metadata["username"]?.toString()
            ?: metadata["full_name"]?.toString()
            ?: metadata["name"]?.toString()
        
        // Nettoyer les guillemets et espaces indésirables
        rawValue?.trim()?.removeSurrounding("\"")
    }
}
