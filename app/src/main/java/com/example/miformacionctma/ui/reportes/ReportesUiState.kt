package com.example.miformacionctma.ui.reportes

import com.example.miformacionctma.domain.model.Categoria
import com.example.miformacionctma.domain.model.OrdenReportes
import com.example.miformacionctma.domain.model.Reporte

/**
 * Estado observable único de la pantalla de lista. Compose solo lee
 * este estado y envía eventos; nunca toca el Repository directamente.
 */
data class ReportesUiState(
    val reportes: List<Reporte> = emptyList(),
    val categorias: List<Categoria> = emptyList(),
    val textoBusqueda: String = "",
    val categoriaFiltroId: Long? = null,
    val orden: OrdenReportes = OrdenReportes.FECHA_DESC,
    val cargando: Boolean = true
)

/** Estado efímero del formulario de nuevo reporte: no se persiste (según el inventario de datos). */
data class NuevoReporteUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoriaId: Long? = null,
    val error: String? = null
)