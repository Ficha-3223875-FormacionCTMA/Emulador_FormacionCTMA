package com.example.miformacionctma.data.repository

import com.example.miformacionctma.model.ActividadFormativa
import kotlinx.coroutines.flow.Flow

interface ActividadRepository {
    // Consulta reactiva a Room (Fuente Canónica)
    fun obtenerActividadesLocal(busqueda: String): Flow<List<ActividadFormativa>>

    // Consulta heredada con filtros
    fun obtenerActividades(filtro: String, orden: String, busqueda: String): Flow<List<ActividadFormativa>>

    // Sincronización remota desde Retrofit hacia Room
    suspend fun refreshActividades(): Result<Unit>

    // Operaciones locales de persistencia
    suspend fun insertarActividad(actividad: ActividadFormativa)
    suspend fun eliminarActividad(actividad: ActividadFormativa)
}