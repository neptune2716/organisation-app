package com.example.organisationapp.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.organisationapp.presentation.viewmodel.AuthViewModel
import com.example.organisationapp.presentation.viewmodel.getDisplayName
import com.example.organisationapp.ui.theme.OrganisationAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by authViewModel.uiState.collectAsState()
    
    // États pour l'édition
    var isEditingUsername by remember { mutableStateOf(false) }
    var isEditingEmail by remember { mutableStateOf(false) }
    var isEditingPassword by remember { mutableStateOf(false) }
    
    var editedUsername by remember { mutableStateOf("") }
    var editedEmail by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    // Rediriger vers l'écran de connexion si déconnecté
    LaunchedEffect(uiState.isLoggedIn) {
        if (!uiState.isLoggedIn) {
            onNavigateToSignIn()
        }
    }
    
    // Initialiser les valeurs d'édition
    LaunchedEffect(uiState.user) {
        uiState.user?.let { user ->
            val username = user.getDisplayName()
            editedUsername = username ?: ""
            editedEmail = user.email ?: ""
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { 
                Text(
                    text = "Profil",
                    fontWeight = FontWeight.SemiBold
                ) 
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Retour"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Photo de profil (placeholder)
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Photo de profil",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Informations utilisateur
            uiState.user?.let { user ->
                // Obtenir le pseudo depuis les métadonnées
                val username = user.getDisplayName()
                
                Text(
                    text = username ?: user.email ?: "Utilisateur",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                if (username != null && user.email != null) {
                    Text(
                        text = user.email!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Statut de vérification email
                if (user.emailConfirmedAt == null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Email non vérifié",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Text(
                                        text = "Veuillez vérifier votre compte en cliquant sur le lien envoyé par email",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            OutlinedButton(
                                onClick = { 
                                    val userEmail = user.email
                                    if (userEmail != null) {
                                        authViewModel.resendConfirmationEmail(userEmail)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !uiState.isLoading,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text("Renvoyer l'email de vérification")
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Informations personnelles",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Champ Pseudo
                        ProfileEditableField(
                            label = "Pseudo",
                            value = username ?: "Non défini",
                            isEditing = isEditingUsername,
                            editValue = editedUsername,
                            onEditValueChange = { editedUsername = it },
                            onEditClick = { isEditingUsername = true },
                            onSaveClick = {
                                authViewModel.updateUsername(editedUsername)
                                isEditingUsername = false
                            },
                            onCancelClick = {
                                editedUsername = username ?: ""
                                isEditingUsername = false
                            },
                            icon = Icons.Default.Person
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Champ Email
                        ProfileEditableField(
                            label = "Email",
                            value = user.email ?: "Non défini",
                            isEditing = isEditingEmail,
                            editValue = editedEmail,
                            onEditValueChange = { editedEmail = it },
                            onEditClick = { isEditingEmail = true },
                            onSaveClick = {
                                authViewModel.updateEmail(editedEmail)
                                isEditingEmail = false
                            },
                            onCancelClick = {
                                editedEmail = user.email ?: ""
                                isEditingEmail = false
                            },
                            icon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Champ Mot de passe
                        ProfilePasswordField(
                            isEditing = isEditingPassword,
                            newPassword = newPassword,
                            confirmPassword = confirmPassword,
                            passwordVisible = passwordVisible,
                            confirmPasswordVisible = confirmPasswordVisible,
                            onNewPasswordChange = { newPassword = it },
                            onConfirmPasswordChange = { confirmPassword = it },
                            onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
                            onConfirmPasswordVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible },
                            onEditClick = { isEditingPassword = true },
                            onSaveClick = {
                                if (newPassword == confirmPassword && newPassword.length >= 6) {
                                    authViewModel.updatePassword(newPassword)
                                    isEditingPassword = false
                                    newPassword = ""
                                    confirmPassword = ""
                                }
                            },
                            onCancelClick = {
                                isEditingPassword = false
                                newPassword = ""
                                confirmPassword = ""
                                passwordVisible = false
                                confirmPasswordVisible = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Messages d'erreur ou de succès
            uiState.error?.let { errorMessage ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (errorMessage.contains("succès", ignoreCase = true) || 
                                              errorMessage.contains("renvoyé", ignoreCase = true))
                            MaterialTheme.colorScheme.primaryContainer
                        else 
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (errorMessage.contains("succès", ignoreCase = true) || 
                                            errorMessage.contains("renvoyé", ignoreCase = true))
                                Icons.Default.CheckCircle
                            else 
                                Icons.Default.Error,
                            contentDescription = null,
                            tint = if (errorMessage.contains("succès", ignoreCase = true) || 
                                     errorMessage.contains("renvoyé", ignoreCase = true))
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else 
                                MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (errorMessage.contains("succès", ignoreCase = true) || 
                                      errorMessage.contains("renvoyé", ignoreCase = true))
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else 
                                MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { authViewModel.clearError() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer",
                                tint = if (errorMessage.contains("succès", ignoreCase = true) || 
                                         errorMessage.contains("renvoyé", ignoreCase = true))
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else 
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Actions
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Bouton Paramètres (placeholder)
                OutlinedButton(
                    onClick = { /* TODO: Navigate to settings */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Paramètres")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Bouton Déconnexion
                Button(
                    onClick = { authViewModel.signOut() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onError
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Se déconnecter")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileEditableField(
    label: String,
    value: String,
    isEditing: Boolean,
    editValue: String,
    onEditValueChange: (String) -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        if (isEditing) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = editValue,
                    onValueChange = onEditValueChange,
                    label = { Text(label) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancelClick) {
                        Text("Annuler")
                    }
                    TextButton(
                        onClick = onSaveClick,
                        enabled = editValue.isNotBlank()
                    ) {
                        Text("Enregistrer")
                    }
                }
            }
        } else {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Modifier $label",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfilePasswordField(
    isEditing: Boolean,
    newPassword: String,
    confirmPassword: String,
    passwordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onConfirmPasswordVisibilityChange: () -> Unit,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        if (isEditing) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = onNewPasswordChange,
                    label = { Text("Nouveau mot de passe") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = onPasswordVisibilityChange) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text("Confirmer le mot de passe") },
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = onConfirmPasswordVisibilityChange) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    isError = confirmPassword.isNotEmpty() && newPassword != confirmPassword,
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (confirmPassword.isNotEmpty() && newPassword != confirmPassword) {
                    Text(
                        text = "Les mots de passe ne correspondent pas",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
                
                if (newPassword.isNotEmpty() && newPassword.length < 6) {
                    Text(
                        text = "Le mot de passe doit contenir au moins 6 caractères",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancelClick) {
                        Text("Annuler")
                    }
                    TextButton(
                        onClick = onSaveClick,
                        enabled = newPassword.isNotBlank() && 
                                newPassword == confirmPassword && 
                                newPassword.length >= 6
                    ) {
                        Text("Enregistrer")
                    }
                }
            }
        } else {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Mot de passe",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "••••••••",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Modifier le mot de passe",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    OrganisationAppTheme {
        ProfileScreen()
    }
}
