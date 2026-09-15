package com.example.miformacionctma.data.mapper

import com.example.miformacionctma.data.dto.ActividadDto
import com.example.miformacionctma.data.local.ActividadEntity
import com.example.miformacionctma.model.ActividadFormativa

// 1. Convertir de DTO (Red) a Entity (Room)
fun ActividadDto.toEntity(): ActividadEntity {
    return ActividadEntity(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion ?: "",
        competencia = this.competencia ?: "Sin competencia",
        fechaEntrega = this.fechaEntrega ?: ""
    )
}

// 2. Convertir de Entity (Room) a Modelo de Dominio (UI)
fun ActividadEntity.toDomain(): ActividadFormativa {
    return ActividadFormativa(
        id = this.id.toIntOrNull() ?: 0,
        titulo = this.titulo,
        descripcion = this.descripcion,
        competencia = this.competencia,
        fecha = this.fechaEntrega,
        estado = "Pendiente",
        progreso = 0
    )
}



// 3. Convertir de Modelo de Dominio (UI) a Entity (Room)
fun ActividadFormativa.toEntity(): ActividadEntity {
    return ActividadEntity(
        id = this.id.toString(),
        titulo = this.titulo,
        descripcion = this.descripcion,
        competencia = this.competencia,
        fechaEntrega = this.fecha
    )
}