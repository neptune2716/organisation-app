package com.example.organisationapp.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Données d'un widget sur la page d'accueil
 */
data class HomeWidget(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val placeholder: String,
    val isEnabled: Boolean = true,
    val onClick: () -> Unit
)

/**
 * Composant réutilisable pour les widgets de la page d'accueil
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeWidgetCard(
    widget: HomeWidget,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = if (widget.isEnabled) widget.onClick else { {} },
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (widget.isEnabled) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = widget.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    color = if (widget.isEnabled) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    }
                )
                
                Icon(
                    imageVector = widget.icon,
                    contentDescription = widget.title,
                    tint = if (widget.isEnabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    },
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Text(
                text = widget.placeholder,
                style = MaterialTheme.typography.bodySmall,
                color = if (widget.isEnabled) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                },
                textAlign = TextAlign.Start,
                lineHeight = 16.sp
            )
        }
    }
}

/**
 * Grille de widgets réorganisable (future fonctionnalité glisser-déposer)
 */
@Composable
fun HomeWidgetGrid(
    widgets: List<HomeWidget>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(widgets) { widget ->
            HomeWidgetCard(widget = widget)
        }
    }
}
