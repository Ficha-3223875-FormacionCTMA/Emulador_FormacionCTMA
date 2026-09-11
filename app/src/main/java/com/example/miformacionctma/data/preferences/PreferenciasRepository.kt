package com.example.miformacionctma.data.preferences

import kotlinx.coroutines.flow.Flow

interface PreferenciasRepository {
    fun obtenerFiltroCompetencia(): Flow<String>
    fun obtenerOrdenamiento(): Flow<String>
    suspend fun guardarFiltroCompetencia(filtro: String)
    suspend fun guardarOrdenamiento(orden: String)
}