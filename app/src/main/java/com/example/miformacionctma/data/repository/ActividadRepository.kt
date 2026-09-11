// ActividadRepository.kt
package com.example.miformacionctma.data.repository

import com.example.miformacionctma.model.ActividadFormativa
import kotlinx.coroutines.flow.Flow

interface ActividadRepository {
    fun obtenerActividades(filtro: String, orden: String, busqueda: String): Flow<List<ActividadFormativa>>
    suspend fun insertarActividad(actividad: ActividadFormativa)
    suspend fun eliminarActividad(actividad: ActividadFormativa)
}