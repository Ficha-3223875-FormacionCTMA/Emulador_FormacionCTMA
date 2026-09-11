package com.example.miformacionctma.data.repository

import com.example.miformacionctma.domain.model.Categoria
import com.example.miformacionctma.domain.model.OrdenReportes
import com.example.miformacionctma.domain.model.Reporte
import com.example.miformacionctma.domain.model.ReporteConCategoria
import kotlinx.coroutines.flow.Flow

/**
 * Contrato único que conocen el ViewModel y la UI. Ni Compose ni el
 * ViewModel saben si detrás hay Room o una lista en memoria: ese es
 * justamente el punto de tener un Repository como puerta de entrada.
 */
interface ReporteRepository {

    fun observarReportes(
        categoriaId: Long?,
        texto: String,
        orden: OrdenReportes
    ): Flow<List<Reporte>>

    fun observarPorId(id: Long): Flow<Reporte?>

    suspend fun obtenerConCategoria(id: Long): ReporteConCategoria?

    suspend fun guardar(reporte: Reporte): Long

    suspend fun eliminar(reporte: Reporte)

    fun observarCategorias(): Flow<List<Categoria>>

    suspend fun asegurarCategoriasSemilla(nombres: List<String>)
}