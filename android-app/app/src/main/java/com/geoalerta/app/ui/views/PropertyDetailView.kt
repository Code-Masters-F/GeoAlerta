package com.geoalerta.app.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import com.geoalerta.app.models.MockRepository
import com.geoalerta.app.models.Property
import com.geoalerta.app.models.RiskLevel

/**
 * Detalhe de uma propriedade. Recebe o id pela rota `detalhe/{propertyId}`,
 * busca no [MockRepository] e exibe área, cultura, riscos ativos e histórico.
 */
@Composable
fun PropertyDetailView(navController: NavController, propertyId: String?) {
    val prop = MockRepository.getProperties().firstOrNull { it.id == propertyId }

    DetailScaffold(title = prop?.name ?: "Propriedade", navController = navController) { padding ->
        if (prop == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Propriedade não encontrada")
            }
            return@DetailScaffold
        }

        val color = riskColor(prop.riskLevel)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Cabeçalho com banner de risco
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = riskContainer(prop.riskLevel))
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                        Text(prop.location, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(shape = RoundedCornerShape(50), color = color) {
                        Text(
                            "Risco ${riskLabel(prop.riskLevel)}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        "Última atualização: ${prop.lastUpdate}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Indicadores da propriedade
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailTile("ÁREA TOTAL", areaFor(prop), Modifier.weight(1f))
                DetailTile("CULTURA", culturaFor(prop), Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailTile("TALHÕES", "12", Modifier.weight(1f))
                DetailTile("NDVI MÉDIO", "0.68", Modifier.weight(1f))
            }

            // Riscos ativos
            Text("RISCOS ATIVOS", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            risksFor(prop).forEach { (titulo, detalhe, lvl) ->
                DetailRisk(titulo, detalhe, lvl)
            }

            // Acoes
            Button(
                onClick = { navController.navigate("mapa") },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver no mapa", fontWeight = FontWeight.SemiBold)
            }
            OutlinedButton(
                onClick = { navController.navigate("notificacoes") },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Configurar alertas")
            }
        }
    }
}

@Composable
private fun DetailTile(label: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(valor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DetailRisk(titulo: String, detalhe: String, lvl: RiskLevel) {
    val color = riskColor(lvl)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(detalhe, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Surface(shape = RoundedCornerShape(50), color = color) {
            Text(
                riskLabel(lvl).uppercase(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// Dados derivados (mock) para enriquecer o detalhe sem alterar o repositorio.
private fun areaFor(p: Property): String = when (p.id) {
    "1" -> "1.250 ha"; "2" -> "640 ha"; "3" -> "2.100 ha"; else -> "900 ha"
}

private fun culturaFor(p: Property): String = when (p.id) {
    "1" -> "Soja (V6)"; "2" -> "Milho"; "3" -> "Algodão"; else -> "Pastagem"
}

private fun risksFor(p: Property): List<Triple<String, String, RiskLevel>> = when (p.riskLevel) {
    RiskLevel.CRITICAL, RiskLevel.HIGH -> listOf(
        Triple("Stress Hídrico Severo", "Zona A (320 ha afetados)", RiskLevel.CRITICAL),
        Triple("Anomalia de Biomassa", "Zona C (queda de 15% NDVI)", RiskLevel.MEDIUM)
    )
    RiskLevel.MEDIUM -> listOf(
        Triple("Foco de Praga", "Gafanhotos a 50km", RiskLevel.MEDIUM)
    )
    RiskLevel.LOW -> listOf(
        Triple("Sem riscos críticos", "Monitoramento estável", RiskLevel.LOW)
    )
}
