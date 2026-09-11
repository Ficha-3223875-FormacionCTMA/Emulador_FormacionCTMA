package com.example.miformacionctma.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// Extensión para crear la instancia única de DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuraciones_usuario")

class PreferenciasRepositoryImpl(
    private val context: Context
) : PreferenciasRepository {

    private object Claves {
        val FILTRO_COMPETENCIA = stringPreferencesKey("filtro_competencia")
        val ORDENAMIENTO = stringPreferencesKey("ordenamiento")
    }

    override fun obtenerFiltroCompetencia(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[Claves.FILTRO_COMPETENCIA] ?: ""
            }
            .flowOn(Dispatchers.IO) // Lectura en hilo de E/S
    }

    override fun obtenerOrdenamiento(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[Claves.ORDENAMIENTO] ?: "ASC"
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun guardarFiltroCompetencia(filtro: String) {
        withContext(Dispatchers.IO) { // Escritura en hilo de E/S
            context.dataStore.edit { preferences ->
                preferences[Claves.FILTRO_COMPETENCIA] = filtro
            }
        }
    }

    override suspend fun guardarOrdenamiento(orden: String) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[Claves.ORDENAMIENTO] = orden
            }
        }
    }
}