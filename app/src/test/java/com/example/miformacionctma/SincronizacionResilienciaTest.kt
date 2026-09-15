package com.example.miformacionctma

import com.example.miformacionctma.data.local.AppDatabase
import com.example.miformacionctma.data.remote.api.ReporteApiService
import com.example.miformacionctma.repository.RoomReporteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import android.content.Context

@RunWith(RobolectricTestRunner::class)
class SincronizacionResilienciaTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ReporteApiService
    private lateinit var repository: RoomReporteRepository
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ReporteApiService::class.java)

        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = RoomReporteRepository(db, apiService)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        db.close()
    }

    @Test
    fun `sincronizacion exitosa actualiza la base de datos local`() = runBlocking {
        // Dado: Una respuesta exitosa del servidor
        val jsonResponse = """
            [
                {
                    "id": 100,
                    "titulo": "Actividad Remota",
                    "descripcion": "Desc",
                    "fecha": "2026-09-15",
                    "estado": "Pendiente",
                    "progreso": 0
                }
            ]
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        // Cuando: Sincronizamos
        repository.sincronizar()

        // Entonces: El dato debe estar en Room
        val reportes = repository.reportes.first()
        assertEquals(1, reportes.size)
        assertEquals("Actividad Remota", reportes[0].titulo)
    }

    @Test
    fun `fallo de red no borra los datos locales (resiliencia)`() = runBlocking {
        // Dado: Ya existen datos locales (caché)
        val jsonResponse1 = """
            [{"id": 1, "titulo": "Local", "descripcion": "D", "fecha": "F", "estado": "E", "progreso": 0}]
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse1).setResponseCode(200))
        repository.sincronizar()
        
        // Verificamos que se guardó
        assertEquals(1, repository.reportes.first().size)

        // Cuando: El servidor falla (Error 500 o Timeout)
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        
        try {
            repository.sincronizar()
        } catch (e: Exception) {
            // Error esperado
        }

        // Entonces: Los datos locales siguen ahí (Caché conservado)
        val reportes = repository.reportes.first()
        assertEquals(1, reportes.size)
        assertEquals("Local", reportes[0].titulo)
    }
}
