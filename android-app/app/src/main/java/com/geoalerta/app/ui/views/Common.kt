package com.geoalerta.app.ui.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.geoalerta.app.models.AlertType
import com.geoalerta.app.models.RiskLevel

/**
 * Paleta semântica de risco. Espelha o `severityConfig` do front-end React,
 * mapeada para os níveis do enum [RiskLevel].
 */
object RiskColors {
    val Critical = Color(0xFFD92D20)
    val High = Color(0xFFEA580C)
    val Medium = Color(0xFFD97706)
    val Low = Color(0xFF15803D)
    val CriticalContainer = Color(0xFFFEE4E2)
    val HighContainer = Color(0xFFFFEDD5)
    val MediumContainer = Color(0xFFFEF3C7)
    val LowContainer = Color(0xFFDCFCE7)
}

fun riskColor(level: RiskLevel): Color = when (level) {
    RiskLevel.CRITICAL -> RiskColors.Critical
    RiskLevel.HIGH -> RiskColors.High
    RiskLevel.MEDIUM -> RiskColors.Medium
    RiskLevel.LOW -> RiskColors.Low
}

fun riskContainer(level: RiskLevel): Color = when (level) {
    RiskLevel.CRITICAL -> RiskColors.CriticalContainer
    RiskLevel.HIGH -> RiskColors.HighContainer
    RiskLevel.MEDIUM -> RiskColors.MediumContainer
    RiskLevel.LOW -> RiskColors.LowContainer
}

fun riskLabel(level: RiskLevel): String = when (level) {
    RiskLevel.CRITICAL -> "Crítico"
    RiskLevel.HIGH -> "Alto"
    RiskLevel.MEDIUM -> "Atenção"
    RiskLevel.LOW -> "Normal"
}

/** Cor semântica de um alerta conforme a gravidade implicada pelo seu tipo. */
fun alertColor(type: AlertType): Color = when (type) {
    AlertType.DROUGHT, AlertType.FIRE -> RiskColors.Critical
    AlertType.PESTS, AlertType.EXCESS_RAIN -> RiskColors.Medium
    AlertType.FROST -> RiskColors.Low
}

/** Rótulo legível do tipo de alerta. */
fun alertTypeLabel(type: AlertType): String = when (type) {
    AlertType.DROUGHT -> "Seca"
    AlertType.EXCESS_RAIN -> "Chuva excessiva"
    AlertType.FROST -> "Geada"
    AlertType.FIRE -> "Incêndio"
    AlertType.PESTS -> "Praga"
}

private data class NavItem(val route: String, val label: String, val icon: ImageVector)

/**
 * Casca padrão das telas internas: barra de navegação inferior fixa, espelhando
 * o `BottomBar.jsx` (Início, Mapa, Alertas, Fazendas, Mais). O slot [content]
 * recebe o padding interno do [Scaffold].
 */
@Composable
fun GeoAlertaScaffold(
    navController: NavController,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    val items = listOf(
        NavItem("dashboard", "Início", Icons.Filled.Home),
        NavItem("mapa", "Mapa", Icons.Filled.LocationOn),
        NavItem("propriedades", "Fazendas", Icons.Filled.List),
        NavItem("preferencias", "Mais", Icons.Filled.MoreVert)
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val selected = currentRoute == item.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    popUpTo("dashboard")
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors()
                    )
                }
            }
        }
    ) { padding ->
        content(padding)
    }
}

/**
 * Casca para telas de detalhe / subtelas: [CenterAlignedTopAppBar] com botão de
 * voltar e um título. Usada pelo detalhe de propriedade e pelas telas de
 * configurações.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScaffold(
    title: String,
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        content(padding)
    }
}
