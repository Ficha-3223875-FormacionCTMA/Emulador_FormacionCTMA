package com.example.miformacionctma.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.miformacionctma.domain.model.OrdenReportes
import com.example.miformacionctma.domain.model.PreferenciasUsuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.text.get

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "preferencias_reportactma"
)

/**
 * Único punto del proyecto que escribe/lee DataStore. La pantalla no
 * instancia DataStore directamente (regla arquitectónica de la
 * Semana 6): solo observa el Flow que expone esta clase.
 */
class PreferenciasRepository(context: Context) {

    private val dataStore = context.applicationContext.dataStore

    private object Claves {
        val ORDEN = stringPreferencesKey("orden_reportes")
        val CATEGORIA_FILTRO_ID = longPreferencesKey("categoria_filtro_id")
    }

    val preferencias: Flow<PreferenciasUsuario> = dataStore.data.map { prefs ->
        PreferenciasUsuario(
            orden = OrdenReportes.fromNombre(prefs[Claves.ORDEN]),
            categoriaFiltroId = prefs[Claves.CATEGORIA_FILTRO_ID]?.takeIf { it != -1L }
        )
    }

    suspend fun actualizarOrden(orden: OrdenReportes) {
        dataStore.edit { prefs -> prefs[Claves.ORDEN] = orden.name }
    }

    suspend fun actualizarCategoriaFiltro(categoriaId: Long?) {
        dataStore.edit { prefs ->
            if (categoriaId == null) {
                prefs[Claves.CATEGORIA_FILTRO_ID] = -1L
            } else {
                prefs[Claves.CATEGORIA_FILTRO_ID] = categoriaId
            }
        }
    }
}