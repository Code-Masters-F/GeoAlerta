package com.geoalerta.app.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/** Bloco de cartão branco usado pelas subtelas de configurações. */
@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun ToggleRow(titulo: String, subtitulo: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitulo, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

// ---- Conta ----
@Composable
fun ContaView(navController: NavController) {
    var email by remember { mutableStateOf("roberto@fazendapalmares.com.br") }
    var cnpj by remember { mutableStateOf("12.345.678/0001-90") }

    DetailScaffold("Conta", navController) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Dados de acesso", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            OutlinedTextField(
                value = cnpj, onValueChange = { cnpj = it },
                label = { Text("CNPJ") }, modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                Text("Alterar senha")
            }
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("Salvar alterações", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ---- Notificações ----
@Composable
fun NotificacoesView(navController: NavController) {
    var push by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf(true) }
    var sms by remember { mutableStateOf(false) }
    var resumo by remember { mutableStateOf(true) }

    DetailScaffold("Notificações", navController) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Canais", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            SettingsCard {
                ToggleRow("Alertas push", "Notificações no aparelho", push) { push = it }
                HorizontalDivider()
                ToggleRow("E-mail", "Resumo de alertas por e-mail", email) { email = it }
                HorizontalDivider()
                ToggleRow("SMS", "Mensagens críticas via SMS", sms) { sms = it }
            }
            Text("Frequência", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            SettingsCard {
                ToggleRow("Resumo diário", "Receber às 07:00", resumo) { resumo = it }
            }
        }
    }
}

// ---- Monitoramento ----
@Composable
fun MonitoramentoView(navController: NavController) {
    var sensibilidade by remember { mutableStateOf(0.6f) }
    var seca by remember { mutableStateOf(true) }
    var geada by remember { mutableStateOf(true) }
    var praga by remember { mutableStateOf(false) }

    DetailScaffold("Monitoramento", navController) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Sensibilidade dos alertas", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            SettingsCard {
                Text("${(sensibilidade * 100).toInt()}%", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Slider(value = sensibilidade, onValueChange = { sensibilidade = it })
                Text(
                    "Quanto maior, mais cedo você recebe avisos de risco.",
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text("Tipos de risco monitorados", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            SettingsCard {
                ToggleRow("Seca", "Déficit hídrico e umidade", seca) { seca = it }
                HorizontalDivider()
                ToggleRow("Geada", "Quedas bruscas de temperatura", geada) { geada = it }
                HorizontalDivider()
                ToggleRow("Pragas", "Focos detectados na região", praga) { praga = it }
            }
        }
    }
}

// ---- Idioma e Região ----
@Composable
fun IdiomaView(navController: NavController) {
    val opcoes = listOf("Português (BR)", "English (US)", "Español")
    var selecionado by remember { mutableStateOf(opcoes.first()) }

    DetailScaffold("Idioma e Região", navController) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Idioma do aplicativo", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            SettingsCard {
                opcoes.forEachIndexed { index, opcao ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(selected = selecionado == opcao, onClick = { selecionado = opcao })
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = selecionado == opcao, onClick = { selecionado = opcao })
                        Spacer(Modifier.width(8.dp))
                        Text(opcao, fontSize = 15.sp)
                    }
                    if (index < opcoes.lastIndex) HorizontalDivider()
                }
            }
        }
    }
}

// ---- Ajuda e Suporte ----
@Composable
fun AjudaView(navController: NavController) {
    val itens = listOf(
        "Perguntas frequentes" to "Tire dúvidas sobre alertas e propriedades",
        "Fale com o suporte" to "suporte@geoalerta.com.br",
        "Termos de Uso" to "Versão 1.0",
        "Política de Privacidade" to "LGPD"
    )
    DetailScaffold("Ajuda e Suporte", navController) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingsCard {
                itens.forEachIndexed { index, (titulo, subtitulo) ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                        Text(titulo, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text(subtitulo, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (index < itens.lastIndex) HorizontalDivider()
                }
            }
            Text(
                "GeoAlerta v1.0 - Global Solution 2026",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
