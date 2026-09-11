package com.example.miformacionctma

import com.example.miformacionctma.data.preferences.PreferenciasRepository
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.ui.ActividadesViewModel
import com.example.miformacionctma.ui.ListadoUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadesUnitTest {

    private lateinit var listaActividades: List<ActividadFormativa>

    // Dispatcher y Mocks para pruebas deterministas de Corrutinas (CA-08)
    private val testDispatcher = StandardTestDispatcher()
    private val repository: ActividadRepository = mock()
    private val preferenciasRepository: PreferenciasRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        listaActividades = listOf(
            ActividadFormativa(1, "Scrum", "Desc 1", "11 de agosto", "Completada", 100),
            ActividadFormativa(
                2,
                "Proyecto de arquitectura limpia y desarrollo móvil avanzado con Kotlin",
                "Desc 2",
                "12 de agosto",
                "Completada",
                100
            ),
            ActividadFormativa(3, "Principios Ágiles", "Desc 3", "13 de agosto", "Completada", 100),
            ActividadFormativa(
                4,
                "Introducción a Scrum",
                "Desc 4",
                "14 de agosto",
                "En proceso",
                75
            ),
            ActividadFormativa(5, "Roles de Scrum", "Desc 5", "15 de agosto", "En proceso", 60),
            ActividadFormativa(
                6,
                "Artefactos de Scrum",
                "Desc 6",
                "16 de agosto",
                "En proceso",
                50
            ),
            ActividadFormativa(7, "Pruebas de software", "Desc 7", "17 de agosto", "Pendiente", 0),
            ActividadFormativa(8, "Tipos de pruebas", "Desc 8", "18 de agosto", "Pendiente", 0),
            ActividadFormativa(9, "Jetpack Compose", "Desc 9", "19 de agosto", "Pendiente", 0),
            ActividadFormativa(
                10,
                "Proyecto Mi Formación CTMA",
                "Desc 10",
                "20 de agosto",
                "Pendiente",
                0
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ==========================================
    // BLOQUE 1: FECHAS
    // ==========================================

    @Test
    fun fechas_CP01_verificarTodasLasActividadesTienenFecha() {
        val todasTienenFecha = listaActividades.all { it.fecha.isNotBlank() }
        Assert.assertTrue("Todas las actividades deben tener una fecha asignada", todasTienenFecha)
    }

    @Test
    fun fechas_CP02_compararFechaMostradaConDatos() {
        val actividadPrueba = listaActividades[0]
        Assert.assertEquals("11 de agosto", actividadPrueba.fecha)
    }

    @Test
    fun fechas_CP03_verificarQueLaFechaSeaAccesible() {
        listaActividades.forEach { actividad ->
            Assert.assertNotNull("La fecha no debe ser nula", actividad.fecha)
            Assert.assertTrue("La fecha debe contener texto", actividad.fecha.isNotEmpty())
        }
    }

    // ==========================================
    // BLOQUE 2: IDS Y VISIBILIDAD DE LISTA
    // ==========================================

    @Test
    fun ids_CP01_revisarIdsDeLas10Actividades() {
        Assert.assertEquals("Deben existir 10 actividades", 10, listaActividades.size)
        val idsValidos = listaActividades.all { it.id > 0 }
        Assert.assertTrue("Todos los IDs deben ser mayores a 0", idsValidos)
    }

    @Test
    fun ids_CP02_verificarQueNingunIdEsteRepetido() {
        val idsUnicos = listaActividades.map { it.id }.toSet()
        Assert.assertEquals("No debe haber IDs duplicados", listaActividades.size, idsUnicos.size)
    }

    @Test
    fun ids_CP03_verificarQueTodasLasActividadesAparezcanCorrectamente() {
        Assert.assertFalse(
            "La lista de actividades no debe estar vacía",
            listaActividades.isEmpty()
        )
        Assert.assertEquals(10, listaActividades.distinctBy { it.id }.size)
    }

    // ==========================================
    // BLOQUE 3: TÍTULOS Y CONTENIDO
    // ==========================================

    @Test
    fun titulos_CP01_verificarActividadConTituloCorto() {
        val actividadCorta = listaActividades[0]
        Assert.assertTrue(
            "El título es corto (menor a 15 caracteres)",
            actividadCorta.titulo.length < 15
        )
        Assert.assertEquals("Scrum", actividadCorta.titulo)
    }

    @Test
    fun titulos_CP02_verificarActividadConTituloLargo() {
        val actividadLarga = listaActividades[1]
        Assert.assertTrue(
            "El título es largo (mayor a 30 caracteres)",
            actividadLarga.titulo.length > 30
        )
    }

    @Test
    fun titulos_CP03_verificarQueLaInformacionContinuaSiendoValida() {
        val actividadLarga = listaActividades[1]
        Assert.assertNotNull(actividadLarga.titulo)
        Assert.assertNotNull(actividadLarga.fecha)
        Assert.assertTrue(actividadLarga.progreso in 0..100)
    }

    // ==========================================
    // BLOQUE 4: CORRUTINAS Y STATEFLOW (GUÍA 7)
    // ==========================================

    @Test
    fun uiState_emiteEstadoVacio_cuandoNoHayDatos() = runTest {
        whenever(preferenciasRepository.obtenerFiltroCompetencia()).thenReturn(flowOf(""))
        whenever(preferenciasRepository.obtenerOrdenamiento()).thenReturn(flowOf("ASC"))
        whenever(repository.obtenerActividades("", "ASC", "")).thenReturn(flowOf(emptyList()))

        val viewModel = ActividadesViewModel(repository, preferenciasRepository)

        val collector = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle() // <-- Avanza las corrutinas pendientes para procesar el Flow

        Assert.assertEquals(ListadoUiState.Vacio, viewModel.uiState.value)
        collector.cancel()
    }

    @Test
    fun uiState_emiteEstadoContenido_cuandoHayDatos() = runTest {
        whenever(preferenciasRepository.obtenerFiltroCompetencia()).thenReturn(flowOf(""))
        whenever(preferenciasRepository.obtenerOrdenamiento()).thenReturn(flowOf("ASC"))
        whenever(repository.obtenerActividades("", "ASC", "")).thenReturn(flowOf(listaActividades))

        val viewModel = ActividadesViewModel(repository, preferenciasRepository)

        val collector = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle() // <-- Avanza las corrutinas pendientes para procesar el Flow

        val estadoActual = viewModel.uiState.value
        Assert.assertTrue(estadoActual is ListadoUiState.Contenido)
        Assert.assertEquals(10, (estadoActual as ListadoUiState.Contenido).actividades.size)
        collector.cancel()
    }
}