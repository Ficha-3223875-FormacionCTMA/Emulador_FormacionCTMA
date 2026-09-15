package com.example.miformacionctma.ui.actividades

import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad

enum class EstadoPantallaActividades {
    CARGANDO,
    CONTENIDO,
    VACIO,
    ERROR
}

enum class EstadoOperacionActividades {
    INACTIVA,
    EN_CURSO,
    EXITOSA,
    FALLIDA
}

data class ActividadesUiState(
    val actividades: List<Actividad> = emptyList(),
    val totalActividades: Int = 0,
    val filtroEstado: EstadoActividad? = null,
    val ordenProgresoDesc: Boolean = true,
    val estadoPantalla: EstadoPantallaActividades = EstadoPantallaActividades.CARGANDO,
    val estadoOperacion: EstadoOperacionActividades = EstadoOperacionActividades.INACTIVA,
    val errorMensaje: String? = null
)
