package com.geoalerta.app.ui.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.geoalerta.app.models.Alert
import com.geoalerta.app.models.AlertType
import com.geoalerta.app.models.MockRepository
import com.geoalerta.app.models.RiskLevel

private data class Indicador(
    val valor: String,
    val titulo: String,
    val legenda: String,
    val barColor: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val rota: String? = null
)

private data class Probabilidade(val nome: String, val valor: Int, val color: Color)

@Composable
fun DashboardView(navController: NavController) {
    val repo = MockRepository()
    val propriedades = repo.getProperties()
    val alertas = repo.getAlerts()

    val indicadores = listOf(
        Indicador("${propriedades.size}", "Propriedades", "Monitoradas", MaterialTheme.colorScheme.primary, Icons.Filled.Home, rota = "propriedades"),
        Indicador("${alertas.size}", "Alertas ativos", "Últimas 24h", RiskColors.Critical, Icons.Filled.Notifications, rota = "alertas"),
        Indicador("1", "Risco alto", "Requer atenção", RiskColors.High, Icons.Filled.Warning),
        Indicador("98%", "Cobertura", "Satélite Sentinel-2", RiskColors.Low, Icons.Filled.Home)
    )

    val probabilidades = listOf(
        Probabilidade("Seca", 72, RiskColors.Critical),
        Probabilidade("Chuva excessiva", 38, RiskColors.Medium),
        Probabilidade("Geada", 15, RiskColors.Low),
        Probabilidade("Incêndio", 54, RiskColors.High)
    )

    GeoAlertaScaffold(navController) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Cabeçalho
            Column {
                Text(
                    text = "Dashboard / Visão Geral",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Fazenda Palmares",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Admin Regional",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Indicadores (StatCards) em grade 2 colunas
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                indicadores.chunked(2).forEach { linha ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        linha.forEach { ind ->
                            StatCard(
                                ind,
                                modifier = Modifier.weight(1f),
                                onClick = ind.rota?.let { rota -> { navController.navigate(rota) } }
                            )
                        }
                        if (linha.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            // Alertas recentes
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ALERTAS RECENTES",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Ver todos",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.pressClickable { navController.navigate("alertas") }
                        )
                    }
                    alertas.forEachIndexed { index, alerta ->
                        StaggeredItem(index) {
                            AlertItem(alerta, onClick = { navController.navigate("alerta/${alerta.id}") })
                        }
                    }
                }
            }

            // Probabilidade de riscos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        Text(
                            text = "PROBABILIDADE DE RISCOS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Média entre propriedades hoje",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    probabilidades.forEach { RiskBar(it.nome, it.valor, it.color) }
                }
            }

            // Atalho para o mapa
            Button(
                onClick = { navController.navigate("mapa") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Abrir mapa interativo", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StatCard(ind: Indicador, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Card(
        modifier = if (onClick != null) modifier.pressClickable(onClick = onClick) else modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(ind.barColor)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ind.barColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(ind.icon, contentDescription = null, tint = ind.barColor, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = ind.valor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ind.titulo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = ind.legenda.uppercase(),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun AlertItem(alert: Alert, onClick: () -> Unit) {
    val color = alertColor(alert.type)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.08f))
            .pressClickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = alert.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = alert.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
        Text(
            text = alert.date,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
private fun RiskBar(nome: String, valor: Int, color: Color) {
    // Anima a barra de 0 ate o valor ao aparecer (cause-efeito: dado preenchendo).
    val reduced = rememberReducedMotion()
    var iniciado by remember { mutableStateOf(reduced) }
    LaunchedEffect(Unit) { iniciado = true }
    val progresso by animateFloatAsState(
        targetValue = if (iniciado) valor / 100f else 0f,
        animationSpec = tween(durationMillis = 700),
        label = "riskProgress"
    )
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = nome, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "$valor%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progresso },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
