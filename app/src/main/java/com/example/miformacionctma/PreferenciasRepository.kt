package com.example.miformacionctma

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenciasRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val CLAVE_FILTRO = stringPreferencesKey("filtro")
    }

    val filtro: Flow<String?> = dataStore.data.map { preferences ->
        preferences[CLAVE_FILTRO]
    }

    suspend fun guardarFiltro(valor: String) {
        dataStore.edit { preferences ->
            preferences[CLAVE_FILTRO] = valor
        }
    }
}