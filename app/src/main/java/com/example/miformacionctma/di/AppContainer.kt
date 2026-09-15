package com.example.miformacionctma.di

import android.content.Context
import com.example.miformacionctma.data.local.AppDatabase
import com.example.miformacionctma.data.preferences.PreferenciasRepository
import com.example.miformacionctma.data.remote.api.ActividadApiService
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.data.repository.RoomActividadRepository
import com.example.miformacionctma.data.repository.ReporteRepository
import com.example.miformacionctma.data.repository.RoomReporteRepository
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * Contenedor de dependencias de la app, sin librería de inyección: crea
 * una única instancia de AppDatabase y expone el Repository y las
 * preferencias ya construidos.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    private val database: AppDatabase by lazy { AppDatabase.obtener(appContext) }

    // Configuración centralizada de Retrofit con kotlinx.serialization (Semana 8)
    private val retrofit: Retrofit by lazy {
        @Suppress("DEPRECATION")
        val contentType = okhttp3.MediaType.parse("application/json")!!
        val jsonConfig = Json { 
            ignoreUnknownKeys = true
            coerceInputValues = true 
        }
        Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/skyriderctma/mock-api/main/") // URL base ficticia/mock resiliente
            .addConverterFactory(jsonConfig.asConverterFactory(contentType))
            .build()
    }

    private val apiService: ActividadApiService by lazy {
        retrofit.create(ActividadApiService::class.java)
    }

    val reporteRepository: ReporteRepository by lazy {
        RoomReporteRepository(
            reporteDao = database.reporteDao(),
            categoriaDao = database.categoriaDao()
        )
    }

    val actividadRepository: ActividadRepository by lazy {
        RoomActividadRepository(
            actividadDao = database.actividadDao(),
            apiService = apiService
        )
    }

    val preferenciasRepository: PreferenciasRepository by lazy {
        PreferenciasRepository(appContext)
    }

    companion object {
        val CATEGORIAS_SEMILLA = listOf("Eléctrico", "Mecánico", "Software", "Seguridad")
    }
}
