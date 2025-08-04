package com.example.organisationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.organisationapp.presentation.navigation.OrganisationNavGraph
import com.example.organisationapp.ui.theme.OrganisationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrganisationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OrganisationApp()
                }
            }
        }
    }
}

@Composable
fun OrganisationApp() {
    val navController = rememberNavController()
    OrganisationNavGraph(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun OrganisationAppPreview() {
    OrganisationAppTheme {
        OrganisationApp()
    }
}
