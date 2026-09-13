package com.example.miformacionctma.ui.actividades

import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad

data class ActividadesUiState(
    val actividades: List<Actividad> = emptyList(),
    val totalActividades: Int = 0,
    val filtroEstado: EstadoActividad? = null,
    val ordenProgresoDesc: Boolean = true,
    val cargando: Boolean = true
)
