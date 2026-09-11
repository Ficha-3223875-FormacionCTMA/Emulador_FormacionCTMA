package com.example.miformacionctma.data.repository

import com.example.miformacionctma.data.local.dao.CategoriaDao
import com.example.miformacionctma.data.local.dao.ReporteDao
import com.example.miformacionctma.data.local.entity.CategoriaEntity
import com.example.miformacionctma.data.mapper.aDominio
import com.example.miformacionctma.data.mapper.aEntidad
import com.example.miformacionctma.domain.model.Categoria
import com.example.miformacionctma.domain.model.OrdenReportes
import com.example.miformacionctma.domain.model.Reporte
import com.example.miformacionctma.domain.model.ReporteConCategoria
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación de producción del incremento de la Semana 6.
 * Sustituye a InMemoryReporteRepository sin que la UI cambie una sola
 * línea: implementa exactamente el mismo contrato ReporteRepository y
 * delega todo en el DAO. Room es la fuente única de verdad; esta clase
 * es el único punto del proyecto que conoce las entidades.
 */
class RoomReporteRepository(
    private val reporteDao: ReporteDao,
    private val categoriaDao: CategoriaDao
) : ReporteRepository {

    override fun observarReportes(
        categoriaId: Long?,
        texto: String,
        orden: OrdenReportes
    ): Flow<List<Reporte>> =
        reporteDao.observarReportes(categoriaId, texto, orden.name)
            .map { lista -> lista.map { it.aDominio() } }

    override fun observarPorId(id: Long): Flow<Reporte?> =
        reporteDao.observarPorId(id).map { it?.aDominio() }

    override suspend fun obtenerConCategoria(id: Long): ReporteConCategoria? =
        reporteDao.obtenerConCategoria(id)?.aDominio()

    override suspend fun guardar(reporte: Reporte): Long =
        if (reporte.id == 0L) {
            reporteDao.insertar(reporte.aEntidad())
        } else {
            reporteDao.actualizar(reporte.aEntidad())
            reporte.id
        }

    override suspend fun eliminar(reporte: Reporte) {
        reporteDao.eliminar(reporte.aEntidad())
    }

    override fun observarCategorias(): Flow<List<Categoria>> =
        categoriaDao.observarTodas().map { lista -> lista.map { it.aDominio() } }

    override suspend fun asegurarCategoriasSemilla(nombres: List<String>) {
        if (categoriaDao.contar() > 0) return
        nombres.forEach { nombre -> categoriaDao.insertar(CategoriaEntity(nombre = nombre)) }
    }
}