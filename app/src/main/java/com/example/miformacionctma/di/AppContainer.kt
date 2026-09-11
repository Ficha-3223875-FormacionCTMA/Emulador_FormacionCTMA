package com.example.miformacionctma.di

import android.content.Context
import com.example.miformacionctma.data.local.AppDatabase
import com.example.miformacionctma.data.preferences.PreferenciasRepository
import com.example.miformacionctma.data.repository.ReporteRepository
import com.example.miformacionctma.data.repository.RoomReporteRepository

/**
 * Contenedor de dependencias de la app, sin librería de inyección: crea
 * una única instancia de AppDatabase y expone el Repository y las
 * preferencias ya construidos. Este es el único lugar del proyecto que
 * conoce a la vez Room y DataStore.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    private val database: AppDatabase by lazy { AppDatabase.obtener(appContext) }

    val reporteRepository: ReporteRepository by lazy {
        RoomReporteRepository(
            reporteDao = database.reporteDao(),
            categoriaDao = database.categoriaDao()
        )
    }

    val preferenciasRepository: PreferenciasRepository by lazy {
        PreferenciasRepository(appContext)
    }

    companion object {
        val CATEGORIAS_SEMILLA = listOf("Eléctrico", "Mecánico", "Software", "Seguridad")
    }
}