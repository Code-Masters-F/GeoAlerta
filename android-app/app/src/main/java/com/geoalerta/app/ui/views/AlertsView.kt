package com.geoalerta.app.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.geoalerta.app.models.Alert
import com.geoalerta.app.models.MockRepository

/**
 * Lista de alertas ativos / recentes. Alimentada por [MockRepository.getAlerts].
 * Acessada pelo StatCard "Alertas ativos" e pelo "Ver todos" do dashboard.
 * Cada item navega para o detalhe do alerta (`alerta/{id}`).
 */
@Composable
fun AlertsView(navController: NavController) {
    val alertas = MockRepository().getAlerts()

    DetailScaffold(title = "Alertas Ativos", navController = navController) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    "${alertas.size} alertas nas ultimas 24h",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            itemsIndexed(alertas) { index, alerta ->
                StaggeredItem(index) {
                    AlertListCard(alerta, onClick = { navController.navigate("alerta/${alerta.id}") })
                }
            }
        }
    }
}

@Composable
private fun AlertListCard(alert: Alert, onClick: () -> Unit) {
    val color = alertColor(alert.type)
    Card(
        modifier = Modifier.fillMaxWidth().pressClickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(alert.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(
                    alert.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.15f)) {
                    Text(
                        "${alertTypeLabel(alert.type)} - ${alert.date}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = color
                    )
                }
            }
            Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}
