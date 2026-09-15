package com.example.miformacionctma.repository

import com.example.miformacionctma.data.local.AppDatabase
import com.example.miformacionctma.data.local.toActividadFormativa
import com.example.miformacionctma.data.local.toReporteEntity
import com.example.miformacionctma.model.ActividadFormativa
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ReporteRepository {
    val reportes: Flow<List<ActividadFormativa>>
    suspend fun agregar(reporte: ActividadFormativa)
    suspend fun actualizar(reporte: ActividadFormativa)
}

class RoomReporteRepository(
    private val db: AppDatabase
) : ReporteRepository {

    override val reportes: Flow<List<ActividadFormativa>> =
        db.reporteDao().obtenerTodos().map { lista ->
            lista.map { it.toActividadFormativa() }
        }

    override suspend fun agregar(reporte: ActividadFormativa) {
        db.reporteDao().insertar(reporte.toReporteEntity())
    }

    override suspend fun actualizar(reporte: ActividadFormativa) {
        db.reporteDao().actualizar(reporte.toReporteEntity())
    }
}