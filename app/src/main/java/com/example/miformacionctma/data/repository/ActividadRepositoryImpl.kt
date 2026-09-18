package com.example.miformacionctma.data.repository

import com.example.miformacionctma.data.local.ActividadDao
import com.example.miformacionctma.data.local.EvidenciaDao
import com.example.miformacionctma.data.local.entity.EvidenciaEntity
import com.example.miformacionctma.data.mapper.toDomain
import com.example.miformacionctma.data.mapper.toEntity
import com.example.miformacionctma.data.network.RemoteActividadDataSource
import com.example.miformacionctma.model.ActividadFormativa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ActividadRepositoryImpl(
    private val dao: ActividadDao,
    private val evidenciaDao: EvidenciaDao,
    private val remoteDataSource: RemoteActividadDataSource
) : ActividadRepository {

    override fun obtenerActividadesLocal(busqueda: String): Flow<List<ActividadFormativa>> {
        return dao.obtenerActividades(busqueda).map { entities ->
            entities.map { it.toDomain() }
        }.flowOn(Dispatchers.IO)
    }

    override fun obtenerActividades(
        filtro: String,
        orden: String,
        busqueda: String
    ): Flow<List<ActividadFormativa>> {
        return dao.obtenerActividades(busqueda).map { entities ->
            entities.map { it.toDomain() }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun refreshActividades(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = remoteDataSource.fetchActividades()
            if (response.isSuccessful) {
                val dtos = response.body() ?: emptyList()
                dao.sincronizarActividades(dtos.map { it.toEntity() })
                Result.success(Unit)
            } else {
                when (response.code()) {
                    401 -> Result.failure(Exception("Sesión vencida (401). Inicie sesión de nuevo."))
                    in 500..599 -> Result.failure(Exception("Error en servidor (${response.code()})."))
                    else -> Result.failure(Exception("Error HTTP: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            when (e) {
                is IOException -> Result.failure(Exception("Sin conexión a Internet."))
                else -> Result.failure(Exception("Error procesando datos: ${e.localizedMessage}"))
            }
        }
    }

    override suspend fun insertarActividad(actividad: ActividadFormativa) {
        withContext(Dispatchers.IO) {
            dao.insertar(actividad.toEntity())
        }
    }

    override suspend fun eliminarActividad(actividad: ActividadFormativa) {
        withContext(Dispatchers.IO) {
            dao.eliminar(actividad.toEntity())
        }
    }

    // =========================================================================
    // Métodos para la gestión de Evidencias en Room
    // =========================================================================

    override fun obtenerEvidenciaPorActividad(actividadId: Long): Flow<EvidenciaEntity?> {
        return evidenciaDao.obtenerEvidenciaPorActividad(actividadId)
    }

    override suspend fun guardarEvidencia(evidencia: EvidenciaEntity): Long {
        return withContext(Dispatchers.IO) {
            evidenciaDao.insertarOActualizarEvidencia(evidencia)
        }
    }

    override suspend fun eliminarEvidenciaPorActividad(actividadId: Long) {
        withContext(Dispatchers.IO) {
            evidenciaDao.eliminarEvidenciaPorActividad(actividadId)
        }
    }
}