package com.example.miformacionctma.data.repository

import com.example.miformacionctma.data.local.ActividadDao
import com.example.miformacionctma.model.ActividadFormativa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ActividadRepositoryImpl(
    private val dao: ActividadDao
) : ActividadRepository {

    override fun obtenerActividades(filtro: String, orden: String, busqueda: String): Flow<List<ActividadFormativa>> {
        // Garantiza que la lectura de la BD ocurra en el hilo Dispatchers.IO
        return dao.obtenerActividades(filtro, orden, busqueda).flowOn(Dispatchers.IO)
    }

    override suspend fun insertarActividad(actividad: ActividadFormativa) {
        withContext(Dispatchers.IO) {
            dao.insertar(actividad)
        }
    }

    override suspend fun eliminarActividad(actividad: ActividadFormativa) {
        withContext(Dispatchers.IO) {
            dao.eliminar(actividad)
        }
    }
}