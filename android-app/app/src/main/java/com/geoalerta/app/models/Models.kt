package com.geoalerta.app.models

data class Property(
    val id: String,
    val name: String,
    val location: String,
    val riskLevel: RiskLevel,
    val lastUpdate: String
)

enum class RiskLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}

data class Alert(
    val id: String,
    val propertyId: String,
    val title: String,
    val description: String,
    val date: String,
    val type: AlertType
)

enum class AlertType {
    DROUGHT, EXCESS_RAIN, FROST, FIRE, PESTS
}

object MockRepository {
    fun getProperties(): List<Property> {
        return listOf(
            Property("1", "Fazenda Bela Vista", "Mato Grosso", RiskLevel.MEDIUM, "Hoje, 10:00"),
            Property("2", "Sitio São José", "Goiás", RiskLevel.LOW, "Hoje, 08:30"),
            Property("3", "Estância Esperança", "Mato Grosso do Sul", RiskLevel.HIGH, "Ontem, 18:00")
        )
    }

    fun getAlerts(): List<Alert> {
        return listOf(
            Alert("1", "3", "Risco de Seca Extrema", "Previsão de 15 dias sem chuva com umidade do ar abaixo de 20%.", "2026-06-05", AlertType.DROUGHT),
            Alert("2", "1", "Alerta de Praga (Gafanhotos)", "Foco detectado a 50km da propriedade.", "2026-06-04", AlertType.PESTS)
        )
    }
}
