package com.example.miformacionctma.data.mapper

import com.example.miformacionctma.data.local.entity.CategoriaEntity
import com.example.miformacionctma.data.local.entity.ReporteConCategoriaEntity
import com.example.miformacionctma.data.local.entity.ReporteEntity
import com.example.miformacionctma.domain.model.Categoria
import com.example.miformacionctma.domain.model.Reporte
import com.example.miformacionctma.domain.model.ReporteConCategoria

/**
 * Mapeadores entidad ↔ dominio. Mantienen las anotaciones de Room
 * fuera del modelo de dominio y de la UI (regla arquitectónica de la
 * Semana 6).
 */

fun ReporteEntity.aDominio(): Reporte = Reporte(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    categoriaId = categoriaId ?: 0L,
    fechaCreacion = fechaCreacion,
    resuelto = resuelto
)

fun Reporte.aEntidad(): ReporteEntity = ReporteEntity(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    categoriaId = categoriaId,
    fechaCreacion = fechaCreacion,
    resuelto = resuelto
)

fun CategoriaEntity.aDominio(): Categoria = Categoria(id = id, nombre = nombre)

fun Categoria.aEntidad(): CategoriaEntity = CategoriaEntity(id = id, nombre = nombre)

fun ReporteConCategoriaEntity.aDominio(): ReporteConCategoria = ReporteConCategoria(
    reporte = reporte.aDominio(),
    categoria = categoria?.aDominio()
)