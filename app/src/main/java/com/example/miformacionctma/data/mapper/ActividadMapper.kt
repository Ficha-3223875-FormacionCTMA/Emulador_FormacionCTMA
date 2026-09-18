package com.example.miformacionctma.data.mapper

import com.example.miformacionctma.data.dto.ActividadDto
import com.example.miformacionctma.data.local.ActividadEntity
import com.example.miformacionctma.model.ActividadFormativa

// 1. Convertir de DTO (Red) a Entity (Room)
fun ActividadDto.toEntity(): ActividadEntity {
    return ActividadEntity(
        id = this.id.toLongOrNull() ?: 0L,
        titulo = this.titulo,
        descripcion = this.descripcion ?: "",
        competencia = this.competencia ?: "Sin competencia",
        fechaEntrega = this.fechaEntrega ?: "",
        progreso = this.progreso ?: 0,       // <-- Mapea correctamente el progreso desde la red
        estado = this.estado ?: "Pendiente"  // <-- Mapea correctamente el estado desde la red
    )
}

// 2. Convertir de Entity (Room) a Modelo de Dominio (UI)
fun ActividadEntity.toDomain(): ActividadFormativa {
    return ActividadFormativa(
        id = this.id.toInt(),
        titulo = this.titulo,
        descripcion = this.descripcion,
        competencia = this.competencia,
        fecha = this.fechaEntrega,
        estado = this.estado,      // <-- Pasa el estado real guardado en Room
        progreso = this.progreso   // <-- Pasa el porcentaje real guardado en Room
    )
}

// 3. Convertir de Modelo de Dominio (UI) a Entity (Room)
fun ActividadFormativa.toEntity(): ActividadEntity {
    return ActividadEntity(
        id = this.id.toLong(),
        titulo = this.titulo,
        descripcion = this.descripcion,
        competencia = this.competencia,
        fechaEntrega = this.fecha,
        progreso = this.progreso,   // <-- Incluye el progreso hacia la entidad
        estado = this.estado        // <-- Incluye el estado hacia la entidad
    )
}