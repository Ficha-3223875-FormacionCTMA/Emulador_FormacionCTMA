package com.example.miformacionctma.domain.model

/**
 * Modelo de dominio de un reporte de novedad de un equipo.
 * No tiene ninguna anotación de persistencia: Compose y el ViewModel
 * solo conocen esta clase, nunca la entidad de Room.
 */
data class Reporte(
    val id: Long = 0L,
    val titulo: String,
    val descripcion: String,
    val categoriaId: Long,
    val fechaCreacion: Long,
    val resuelto: Boolean = false
)

/**
 * Reporte junto con el nombre de su categoría, resultado de la
 * consulta transaccional relación Reporte–Categoria (DAO).
 */
data class ReporteConCategoria(
    val reporte: Reporte,
    val categoria: Categoria?
)