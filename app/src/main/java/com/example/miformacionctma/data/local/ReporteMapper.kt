package com.example.miformacionctma.data.local

import com.example.miformacionctma.data.local.entity.ReporteEntity
import com.example.miformacionctma.model.ActividadFormativa

fun ReporteEntity.toActividadFormativa(): ActividadFormativa {
    return ActividadFormativa(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        fecha = this.fecha,
        estado = this.estado,
        progreso = this.progreso
    )
}

fun ActividadFormativa.toReporteEntity(categoriaId: Int? = null): ReporteEntity {
    return ReporteEntity(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        fecha = this.fecha,
        estado = this.estado,
        progreso = this.progreso,
        categoriaId = categoriaId
    )
}