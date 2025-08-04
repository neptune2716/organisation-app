package com.example.organisationapp.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.organisationapp.presentation.viewmodel.AuthViewModel
import com.example.organisationapp.presentation.viewmodel.getDisplayName
import com.example.organisationapp.ui.theme.OrganisationAppTheme
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTasks: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToHabits: () -> Unit = {},
    onNavigateToNotes: () -> Unit = {},
    onNavigateToSport: () -> Unit = {},
    onNavigateToScreenTime: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (currentHour) {
        in 5..11 -> "Bonjour"
        in 12..17 -> "Bon après-midi"
        in 18..22 -> "Bonsoir"
        else -> "Bonne nuit"
    }
    
    val uiState by authViewModel.uiState.collectAsState()

    val widgets = listOf(
        HomeWidget(
            id = "tasks",
            title = "Prochaines tâches",
            icon = Icons.Default.CheckCircle,
            placeholder = "3-5 tâches prioritaires à venir",
            onClick = onNavigateToTasks
        ),
        HomeWidget(
            id = "screen_time",
            title = "Temps d'écran",
            icon = Icons.Default.Phone,
            placeholder = "Graphique + temps total d'aujourd'hui",
            onClick = onNavigateToScreenTime
        ),
        HomeWidget(
            id = "habits",
            title = "Habitudes du jour",
            icon = Icons.Default.TrendingUp,
            placeholder = "Progress bars des habitudes",
            onClick = onNavigateToHabits
        ),
        HomeWidget(
            id = "calendar",
            title = "Agenda",
            icon = Icons.Default.Event,
            placeholder = "Événements à venir",
            onClick = onNavigateToCalendar
        ),
        HomeWidget(
            id = "notes",
            title = "Notes rapides",
            icon = Icons.Default.Note,
            placeholder = "Dernières notes importantes",
            onClick = onNavigateToNotes
        ),
        HomeWidget(
            id = "sport",
            title = "Sport & Santé",
            icon = Icons.Default.FitnessCenter,
            placeholder = "Objectifs du jour",
            onClick = onNavigateToSport
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header avec salutation et icône de profil
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "$greeting !",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = if (uiState.isLoggedIn) {
                        val user = uiState.user
                        val username = user?.getDisplayName()
                        
                        "Bienvenue ${username ?: user?.email?.substringBefore("@") ?: "utilisateur"} !"
                    } else {
                        "Votre organisation du jour"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Icône de profil
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                color = if (uiState.isLoggedIn) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                onClick = {
                    if (uiState.isLoggedIn) {
                        onNavigateToProfile()
                    } else {
                        onNavigateToSignIn()
                    }
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (uiState.isLoggedIn) {
                            Icons.Default.Person // Photo de profil si connecté
                        } else {
                            Icons.Default.AccountCircle // Icône vide si non connecté
                        },
                        contentDescription = if (uiState.isLoggedIn) {
                            "Profil utilisateur"
                        } else {
                            "Se connecter"
                        },
                        tint = if (uiState.isLoggedIn) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Indicateurs de progression globaux (placeholder)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Progression du jour",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProgressIndicator("Tâches", 0.7f)
                    ProgressIndicator("Habitudes", 0.5f)
                    ProgressIndicator("Sport", 0.3f)
                }
            }
        }

        // Grille de widgets
        HomeWidgetGrid(
            widgets = widgets,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ProgressIndicator(
    label: String,
    progress: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.size(40.dp),
            strokeWidth = 4.dp
        )
        
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    OrganisationAppTheme {
        HomeScreen()
    }
}
