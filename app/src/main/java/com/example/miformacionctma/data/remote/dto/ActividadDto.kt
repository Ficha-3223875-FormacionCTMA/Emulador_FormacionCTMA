package com.example.miformacionctma.data.remote.dto

import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad
import kotlinx.serialization.Serializable

@Serializable
data class ActividadDto(
    val id: Long? = null,
    val nombre: String,
    val descripcion: String,
    val progreso: Int,
    val estado: String,
    val fechaCreacion: Long? = null
) {
    fun toDomain(): Actividad {
        val estadoEnum = try {
            EstadoActividad.valueOf(estado)
        } catch (e: Exception) {
            EstadoActividad.PENDIENTE
        }
        return Actividad(
            id = id ?: 0L,
            nombre = nombre,
            descripcion = descripcion,
            progreso = progreso,
            estado = estadoEnum,
            fechaCreacion = fechaCreacion ?: System.currentTimeMillis()
        )
    }
}
