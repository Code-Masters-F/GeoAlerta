package com.geoalerta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.geoalerta.app.ui.views.AjudaView
import com.geoalerta.app.ui.views.AlertDetailView
import com.geoalerta.app.ui.views.AlertsView
import com.geoalerta.app.ui.views.CadastroView
import com.geoalerta.app.ui.views.ContaView
import com.geoalerta.app.ui.views.DashboardView
import com.geoalerta.app.ui.views.ErrorConnectionView
import com.geoalerta.app.ui.views.IdiomaView
import com.geoalerta.app.ui.views.LoginView
import com.geoalerta.app.ui.views.MapView
import com.geoalerta.app.ui.views.MonitoramentoView
import com.geoalerta.app.ui.views.NotificacoesView
import com.geoalerta.app.ui.views.PreferencesView
import com.geoalerta.app.ui.views.PropertiesView
import com.geoalerta.app.ui.views.PropertyDetailView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val anim = tween<Float>(durationMillis = 300)
                    val animInt = tween<androidx.compose.ui.unit.IntOffset>(durationMillis = 300)
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        // Avancar: entra pela direita; sair desliza levemente p/ esquerda.
                        enterTransition = {
                            slideInHorizontally(animInt) { it / 3 } + fadeIn(anim)
                        },
                        exitTransition = {
                            slideOutHorizontally(animInt) { -it / 6 } + fadeOut(anim)
                        },
                        // Voltar: inverte a direcao (continuidade espacial).
                        popEnterTransition = {
                            slideInHorizontally(animInt) { -it / 6 } + fadeIn(anim)
                        },
                        popExitTransition = {
                            slideOutHorizontally(animInt) { it / 3 } + fadeOut(anim)
                        }
                    ) {
                        composable("login") {
                            LoginView(navController)
                        }
                        composable("cadastro") {
                            CadastroView(navController)
                        }
                        composable("dashboard") {
                            DashboardView(navController)
                        }
                        composable(
                            route = "mapa?propertyId={propertyId}",
                            arguments = listOf(navArgument("propertyId") {
                                type = NavType.StringType
                                nullable = true
                            })
                        ) { entry ->
                            MapView(navController, entry.arguments?.getString("propertyId"))
                        }
                        composable("propriedades") {
                            PropertiesView(navController)
                        }
                        composable("preferencias") {
                            PreferencesView(navController)
                        }
                        composable(
                            route = "detalhe/{propertyId}",
                            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
                        ) { entry ->
                            PropertyDetailView(navController, entry.arguments?.getString("propertyId"))
                        }
                        composable("alertas") { AlertsView(navController) }
                        composable(
                            route = "alerta/{alertId}",
                            arguments = listOf(navArgument("alertId") { type = NavType.StringType })
                        ) { entry ->
                            AlertDetailView(navController, entry.arguments?.getString("alertId"))
                        }
                        composable("conta") { ContaView(navController) }
                        composable("notificacoes") { NotificacoesView(navController) }
                        composable("monitoramento") { MonitoramentoView(navController) }
                        composable("idioma") { IdiomaView(navController) }
                        composable("ajuda") { AjudaView(navController) }
                        composable("error") {
                            ErrorConnectionView(onRetry = {
                                navController.navigate("login") { popUpTo(0) }
                            })
                        }
                    }
                }
            }
        }
    }
}
