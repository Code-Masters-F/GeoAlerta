package com.geoalerta.app.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * Tela de mapa "inteligente". Usa Google Maps (satelite) como base real e
 * sobrepoe camadas simuladas de dados agro: poligono da area cultivada,
 * zonas de risco (stress hidrico, anomalia de biomassa), marcadores de risco
 * e uma varredura animada de sensoriamento. O painel inferior resume o que
 * foi detectado. Espelha `MapaPage.jsx`.
 */

// Centro da fazenda demonstrada (Vale do Rio Verde, MG).
private val fazendaCentro = LatLng(-21.4521, -45.4560)

// Contorno da area agricola monitorada.
private val areaAgricola = listOf(
    LatLng(-21.4470, -45.4625),
    LatLng(-21.4455, -45.4500),
    LatLng(-21.4560, -45.4475),
    LatLng(-21.4600, -45.4585),
    LatLng(-21.4535, -45.4640)
)

@Composable
fun MapView(navController: NavController) {
    var painelVisivel by remember { mutableStateOf(true) }
    val reduced = rememberReducedMotion()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(fazendaCentro, 14.2f)
    }

    GeoAlertaScaffold(navController) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Mapa real (satelite) com as camadas de dados sobrepostas.
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = MapType.HYBRID),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    mapToolbarEnabled = false,
                    compassEnabled = false
                ),
                onMapClick = { painelVisivel = true }
            ) {
                // Area agricola monitorada.
                Polygon(
                    points = areaAgricola,
                    fillColor = Color(0x3322C55E),
                    strokeColor = Color(0xFF22C55E),
                    strokeWidth = 4f
                )

                // Zona A: stress hidrico severo (critico).
                Circle(
                    center = LatLng(-21.4505, -45.4595),
                    radius = 600.0,
                    fillColor = RiskColors.Critical.copy(alpha = 0.25f),
                    strokeColor = RiskColors.Critical,
                    strokeWidth = 3f
                )

                // Zona C: anomalia de biomassa (atencao).
                Circle(
                    center = LatLng(-21.4560, -45.4515),
                    radius = 450.0,
                    fillColor = RiskColors.Medium.copy(alpha = 0.22f),
                    strokeColor = RiskColors.Medium,
                    strokeWidth = 3f
                )

                Marker(
                    state = MarkerState(position = LatLng(-21.4505, -45.4595)),
                    title = "Stress Hidrico Severo",
                    snippet = "Zona A - 320 ha afetados",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                    onClick = { painelVisivel = true; false }
                )
                Marker(
                    state = MarkerState(position = LatLng(-21.4560, -45.4515)),
                    title = "Anomalia de Biomassa",
                    snippet = "Zona C - queda de 15% NDVI",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                    onClick = { painelVisivel = true; false }
                )
                Marker(
                    state = MarkerState(position = fazendaCentro),
                    title = "Fazenda Otor",
                    snippet = "Sede - Soja (V6)",
                    onClick = { painelVisivel = true; false }
                )
            }

            // Varredura de sensoriamento por cima do mapa (nao intercepta gestos).
            if (!reduced) ScanSweep(Modifier.fillMaxSize())

            // Barra de busca.
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.92f),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "Buscar fazenda, talhao ou regiao...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Rodape de telemetria.
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.92f)
            ) {
                Text(
                    text = "Real-time Satellite | Last: 14:32:05\nResolution: 10m | Sensor: Sentinel-2B",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Painel de detalhes: surge deslizando de baixo (modal a partir da origem).
            AnimatedVisibility(
                visible = painelVisivel,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = slideInVertically(tween(300)) { it } + fadeIn(tween(300)),
                exit = slideOutVertically(tween(200)) { it } + fadeOut(tween(200))
            ) {
                PropertyDetailPanel(
                    modifier = Modifier.padding(16.dp),
                    onClose = { painelVisivel = false },
                    onVerDetalhes = { navController.navigate("detalhe/${android.net.Uri.encode("3")}") }
                )
            }
        }
    }
}

/**
 * Linha de varredura horizontal que cruza o mapa em loop, simulando uma
 * passagem de sensoriamento remoto. Usa apenas desenho (transform/opacity),
 * nao consome toques, e e suprimida em reduced-motion pelo chamador.
 */
@Composable
private fun ScanSweep(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "scanSweep")
    val progresso by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanProgress"
    )

    val scanColor = Color(0xFF4ADE80)
    Canvas(modifier = modifier) {
        val faixa = size.width * 0.32f
        val cabeca = size.width * progresso
        val inicio = (cabeca - faixa).coerceAtLeast(0f)
        // Rastro suave atras da linha de varredura.
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, scanColor.copy(alpha = 0.16f)),
                startX = inicio,
                endX = cabeca
            ),
            topLeft = Offset(inicio, 0f),
            size = Size(width = (cabeca - inicio).coerceAtLeast(0f), height = size.height)
        )
        // Linha-cabeca brilhante.
        drawLine(
            color = scanColor.copy(alpha = 0.85f),
            start = Offset(cabeca, 0f),
            end = Offset(cabeca, size.height),
            strokeWidth = 3f
        )
    }
}

@Composable
private fun PropertyDetailPanel(modifier: Modifier = Modifier, onClose: () -> Unit, onVerDetalhes: () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(RiskColors.Critical)
            )
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Fazenda Otor", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
                            Text(
                                "Vale do Rio Verde, MG",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = "Fechar")
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoTile("AREA TOTAL", "1.250 ha", Modifier.weight(1f))
                    InfoTile("CULTURA ATUAL", "Soja (V6)", Modifier.weight(1f))
                }

                Text("RISCOS ATIVOS", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                ActiveRisk("Stress Hidrico Severo", "Zona A (320 ha afetados)", "CRITICO", RiskColors.Critical)
                ActiveRisk("Anomalia de Biomassa", "Zona C (Queda de 15% NDVI)", "ATENCAO", RiskColors.Medium)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onVerDetalhes,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ver Detalhes", fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedIconButton(onClick = { }, enabled = false) {
                        Icon(Icons.Filled.Share, contentDescription = "Compartilhar (Em breve)")
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoTile(label: String, valor: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(12.dp)
    ) {
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ActiveRisk(titulo: String, detalhe: String, badge: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(detalhe, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Surface(shape = RoundedCornerShape(50), color = color) {
            Text(
                badge,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
