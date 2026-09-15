package com.example.miformacionctma.repository

import com.example.miformacionctma.data.local.AppDatabase
import com.example.miformacionctma.data.local.toActividadFormativa
import com.example.miformacionctma.data.local.toEntity
import com.example.miformacionctma.data.local.toReporteEntity
import com.example.miformacionctma.data.remote.api.ReporteApiService
import com.example.miformacionctma.model.ActividadFormativa
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface ReporteRepository {
    val reportes: Flow<List<ActividadFormativa>>
    suspend fun agregar(reporte: ActividadFormativa)
    suspend fun actualizar(reporte: ActividadFormativa)
    suspend fun sincronizar()
}

class RoomReporteRepository(
    private val db: AppDatabase,
    private val api: ReporteApiService
) : ReporteRepository {

    override val reportes: Flow<List<ActividadFormativa>> =
        db.reporteDao().obtenerTodos().map { lista ->
            lista.map { it.toActividadFormativa() }
        }

    override suspend fun agregar(reporte: ActividadFormativa) {
        withContext(Dispatchers.IO) {
            db.reporteDao().insertar(reporte.toReporteEntity())
        }
    }

    override suspend fun actualizar(reporte: ActividadFormativa) {
        withContext(Dispatchers.IO) {
            db.reporteDao().actualizar(reporte.toReporteEntity())
        }
    }

    override suspend fun sincronizar() {
        withContext(Dispatchers.IO) {
            // SIMULACIÓN PARA DEMOSTRACIÓN (Guía #8):
            // Añadimos un retraso de 2 segundos para ver el estado "Sincronizando..."
            delay(2000)

            // 1. Intentar obtener datos remotos
            // Esto lanzará una excepción debido a que la URL es ficticia.
            val remotas = api.obtenerActividades()
            val entidades = remotas.map { it.toEntity() }

            // 2. Actualizar Room de forma consistente
            db.withTransaction {
                entidades.forEach { entity ->
                    val existe = db.reporteDao().obtenerPorId(entity.id)
                    if (existe != null) {
                        db.reporteDao().actualizar(entity)
                    } else {
                        db.reporteDao().insertar(entity)
                    }
                }
            }
        }
    }
}
