package com.geoalerta.app.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private data class SettingItem(val icon: ImageVector, val titulo: String, val subtitulo: String, val rota: String)

/**
 * Preferências / "Mais". Espelha `PreferenciasPage.jsx`: cartão de perfil,
 * grupos de configurações navegáveis e botão de sair que retorna ao login.
 */
@Composable
fun PreferencesView(navController: NavController) {
    GeoAlertaScaffold(navController) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Cartão de perfil
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(64.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                    }
                    Column {
                        Text("Roberto Oliveira", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Gestor de Operações Agro",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = RiskColors.Low, modifier = Modifier.size(14.dp))
                                Text("Conta Enterprise", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // Configuracoes gerais
            SettingsGroup(
                titulo = "CONFIGURAÇÕES GERAIS",
                itens = listOf(
                    SettingItem(Icons.Filled.Person, "Conta", "Email, Senha, CNPJ", "conta"),
                    SettingItem(Icons.Filled.Notifications, "Notificações", "Alertas, Push, Email", "notificacoes"),
                    SettingItem(Icons.Filled.Settings, "Monitoramento", "Sensibilidade, Limites de Risco", "monitoramento")
                ),
                navController = navController
            )

            // Sistema
            SettingsGroup(
                titulo = "SISTEMA",
                itens = listOf(
                    SettingItem(Icons.Filled.Info, "Idioma e Região", "Português (BR)", "idioma"),
                    SettingItem(Icons.Filled.Email, "Ajuda e Suporte", "FAQ, Contato, Termos", "ajuda")
                ),
                navController = navController
            )

            // Sair
            OutlinedButton(
                onClick = {
                    navController.navigate("login") { popUpTo(0) }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RiskColors.Critical)
            ) {
                Icon(Icons.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Sair da Conta", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SettingsGroup(titulo: String, itens: List<SettingItem>, navController: NavController) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            titulo,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                itens.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pressClickable { navController.navigate(item.rota) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(item.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.titulo, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text(item.subtitulo, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                    }
                    if (index < itens.lastIndex) HorizontalDivider()
                }
            }
        }
    }
}
