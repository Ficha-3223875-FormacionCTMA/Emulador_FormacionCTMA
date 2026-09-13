package com.example.miformacionctma.domain.model

enum class EstadoActividad {
    PENDIENTE,
    EN_PROCESO,
    COMPLETADA
}

data class Actividad(
    val id: Long = 0L,
    val nombre: String,
    val descripcion: String,
    val progreso: Int, // 0-100
    val estado: EstadoActividad,
    val fechaCreacion: Long
)
