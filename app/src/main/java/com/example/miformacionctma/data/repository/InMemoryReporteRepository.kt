package com.example.miformacionctma.data.repository

import com.example.miformacionctma.domain.model.Categoria
import com.example.miformacionctma.domain.model.OrdenReportes
import com.example.miformacionctma.domain.model.Reporte
import com.example.miformacionctma.domain.model.ReporteConCategoria
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Implementación del incremento de la Semana 5: una lista temporal en
 * memoria. Se conserva en el proyecto (sin usarse en producción) como
 * referencia de la "transformación" pedida por la guía y como doble de
 * pruebas rápido para el ViewModel, ya que no depende de Android ni de
 * una base de datos real.
 */
class InMemoryReporteRepository : ReporteRepository {

    private val idGenerator = AtomicLong(1)
    private val reportes = MutableStateFlow<List<Reporte>>(emptyList())
    private val categorias = MutableStateFlow<List<Categoria>>(emptyList())

    override fun observarReportes(
        categoriaId: Long?,
        texto: String,
        orden: OrdenReportes
    ): Flow<List<Reporte>> = reportes.asStateFlow().map { lista ->
        lista
            .filter { categoriaId == null || it.categoriaId == categoriaId }
            .filter {
                texto.isBlank() ||
                        it.titulo.contains(texto, ignoreCase = true) ||
                        it.descripcion.contains(texto, ignoreCase = true)
            }
            .let { filtrada ->
                when (orden) {
                    OrdenReportes.FECHA_DESC -> filtrada.sortedByDescending { it.fechaCreacion }
                    OrdenReportes.FECHA_ASC -> filtrada.sortedBy { it.fechaCreacion }
                    OrdenReportes.TITULO_ASC -> filtrada.sortedBy { it.titulo }
                }
            }
    }

    override fun observarPorId(id: Long): Flow<Reporte?> =
        reportes.asStateFlow().map { lista -> lista.firstOrNull { it.id == id } }

    override suspend fun obtenerConCategoria(id: Long): ReporteConCategoria? {
        val reporte = reportes.value.firstOrNull { it.id == id } ?: return null
        val categoria = categorias.value.firstOrNull { it.id == reporte.categoriaId }
        return ReporteConCategoria(reporte, categoria)
    }

    override suspend fun guardar(reporte: Reporte): Long {
        val actual = reportes.value
        return if (reporte.id == 0L) {
            val nuevoId = idGenerator.getAndIncrement()
            reportes.value = actual + reporte.copy(id = nuevoId)
            nuevoId
        } else {
            reportes.value = actual.map { if (it.id == reporte.id) reporte else it }
            reporte.id
        }
    }

    override suspend fun eliminar(reporte: Reporte) {
        reportes.value = reportes.value.filterNot { it.id == reporte.id }
    }

    override fun observarCategorias(): Flow<List<Categoria>> = categorias.asStateFlow()

    override suspend fun asegurarCategoriasSemilla(nombres: List<String>) {
        if (categorias.value.isNotEmpty()) return
        var siguienteId = 1L
        categorias.value = nombres.map { Categoria(id = siguienteId++, nombre = it) }
    }
}