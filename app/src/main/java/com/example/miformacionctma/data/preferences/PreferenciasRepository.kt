package com.example.miformacionctma.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.miformacionctma.domain.model.OrdenReportes
import com.example.miformacionctma.domain.model.PreferenciasUsuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "preferencias_reportactma"
)

/**
 * Único punto del proyecto que escribe/lee DataStore. La pantalla no
 * instancia DataStore directamente (regla arquitectónica de la
 * Semana 6): solo observa el Flow que expone esta clase.
 */
open class PreferenciasRepository {

    private val dataStore: DataStore<Preferences>?

    constructor(context: Context) {
        this.dataStore = context.applicationContext.dataStore
    }

    // Constructor secundario para pruebas unitarias sin contexto de Android
    constructor() {
        this.dataStore = null
    }

    private object Claves {
        val ORDEN = stringPreferencesKey("orden_reportes")
        val CATEGORIA_FILTRO_ID = longPreferencesKey("categoria_filtro_id")
        val ACTIVIDADES_FILTRO_ESTADO = stringPreferencesKey("actividades_filtro_estado")
        val ACTIVIDADES_ORDEN_DESC = booleanPreferencesKey("actividades_orden_desc")
    }

    open val preferencias: Flow<PreferenciasUsuario> by lazy {
        dataStore?.data?.map { prefs ->
            PreferenciasUsuario(
                orden = OrdenReportes.fromNombre(prefs[Claves.ORDEN]),
                categoriaFiltroId = prefs[Claves.CATEGORIA_FILTRO_ID]?.takeIf { it != -1L },
                actividadesFiltroEstado = prefs[Claves.ACTIVIDADES_FILTRO_ESTADO],
                actividadesOrdenDesc = prefs[Claves.ACTIVIDADES_ORDEN_DESC] ?: true
            )
        } ?: kotlinx.coroutines.flow.flowOf(PreferenciasUsuario())
    }

    open suspend fun actualizarOrden(orden: OrdenReportes) {
        dataStore?.edit { prefs -> prefs[Claves.ORDEN] = orden.name }
    }

    open suspend fun actualizarCategoriaFiltro(categoriaId: Long?) {
        dataStore?.edit { prefs ->
            if (categoriaId == null) {
                prefs[Claves.CATEGORIA_FILTRO_ID] = -1L
            } else {
                prefs[Claves.CATEGORIA_FILTRO_ID] = categoriaId
            }
        }
    }

    open suspend fun actualizarActividadesFiltroEstado(estado: String?) {
        dataStore?.edit { prefs ->
            if (estado == null) {
                prefs.remove(Claves.ACTIVIDADES_FILTRO_ESTADO)
            } else {
                prefs[Claves.ACTIVIDADES_FILTRO_ESTADO] = estado
            }
        }
    }

    open suspend fun actualizarActividadesOrdenDesc(desc: Boolean) {
        dataStore?.edit { prefs ->
            prefs[Claves.ACTIVIDADES_ORDEN_DESC] = desc
        }
    }
}
