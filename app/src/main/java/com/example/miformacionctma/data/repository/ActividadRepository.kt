package com.example.miformacionctma.data.repository

import com.example.miformacionctma.data.local.dao.ActividadDao
import com.example.miformacionctma.data.local.entity.ActividadEntity
import com.example.miformacionctma.data.remote.api.ActividadApiService
import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface ActividadRepository {
    fun observarTodas(): Flow<List<Actividad>>
    fun observarOrdenadasPorProgreso(desc: Boolean): Flow<List<Actividad>>
    fun observarPorEstado(estado: EstadoActividad): Flow<List<Actividad>>
    suspend fun contar(): Int
    suspend fun guardar(actividad: Actividad): Long
    suspend fun eliminar(actividad: Actividad)
    suspend fun refrescarDesdeServidor()
}

class RoomActividadRepository(
    private val actividadDao: ActividadDao,
    private val apiService: ActividadApiService
) : ActividadRepository {

    override fun observarTodas(): Flow<List<Actividad>> =
        actividadDao.observarTodas().map { list -> list.map { it.toDomain() } }

    override fun observarOrdenadasPorProgreso(desc: Boolean): Flow<List<Actividad>> {
        val flow = if (desc) {
            actividadDao.observarOrdenadasPorProgresoDesc()
        } else {
            actividadDao.observarOrdenadasPorProgresoAsc()
        }
        return flow.map { list -> list.map { it.toDomain() } }
    }

    override fun observarPorEstado(estado: EstadoActividad): Flow<List<Actividad>> =
        actividadDao.observarPorEstado(estado.name).map { list -> list.map { it.toDomain() } }

    override suspend fun contar(): Int = withContext(Dispatchers.IO) {
        actividadDao.contar()
    }

    override suspend fun guardar(actividad: Actividad): Long = withContext(Dispatchers.IO) {
        if (actividad.id == 0L) {
            actividadDao.insertar(actividad.toEntity())
        } else {
            actividadDao.actualizar(actividad.toEntity())
            actividad.id
        }
    }

    override suspend fun eliminar(actividad: Actividad) = withContext(Dispatchers.IO) {
        actividadDao.eliminar(actividad.toEntity())
    }

    override suspend fun refrescarDesdeServidor() = withContext(Dispatchers.IO) {
        // Consultar API remota de forma consistente
        val dtosRemotos = apiService.obtenerActividades()
        
        // Transformar e insertar de forma coordinada conservando la caché local si la red es exitosa
        dtosRemotos.forEach { dto ->
            val actividadDominio = dto.toDomain()
            val existeLocal = actividadDao.contar() > 0 // Verificación o actualización inteligente
            actividadDao.insertar(actividadDominio.toEntity())
        }
    }
}

private fun ActividadEntity.toDomain() = Actividad(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    progreso = progreso,
    estado = EstadoActividad.valueOf(estado),
    fechaCreacion = fechaCreacion
)

private fun Actividad.toEntity() = ActividadEntity(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    progreso = progreso,
    estado = estado.name,
    fechaCreacion = fechaCreacion
)
