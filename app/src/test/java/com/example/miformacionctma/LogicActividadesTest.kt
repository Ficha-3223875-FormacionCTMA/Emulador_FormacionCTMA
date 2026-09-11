package com.example.miformacionctma

import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.repository.ReporteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Repositorio falso para simular el comportamiento del backend (Room/Data layer)
 */
class LogicFakeRepository : ReporteRepository {
    private val _reportes = MutableStateFlow<List<ActividadFormativa>>(emptyList())
    override val reportes: Flow<List<ActividadFormativa>> = _reportes

    override suspend fun agregar(reporte: ActividadFormativa) {
        _reportes.value = _reportes.value + reporte
    }

    fun emitir(lista: List<ActividadFormativa>) {
        _reportes.value = lista
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class LogicActividadesTest {

    private lateinit var viewModel: PantallaActividadesViewModel
    private lateinit var repository: LogicFakeRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = LogicFakeRepository()
        viewModel = PantallaActividadesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Dado: un dispositivo móvil.
     * Cuando: se abre la pantalla de actividades.
     * Entonces: las actividades deben aparecer una debajo de otra (validamos orden de lista).
     */
    @Test
    fun testActividadesAparecenEnOrden() = runTest {
        val actividades = listOf(
            ActividadFormativa(1, "A1", "D1", "F1", "Pendiente", 0),
            ActividadFormativa(2, "A2", "D2", "F2", "Pendiente", 0)
        )
        repository.emitir(actividades)

        // En UnconfinedTestDispatcher, las actualizaciones ocurren inmediatamente
        val result = viewModel.actividadesFiltradas.value
        assertEquals(2, result.size)
        assertEquals("A1", result[0].titulo)
        assertEquals("A2", result[1].titulo)
    }

    /**
     * la actividad "Programación en Kotlin".
     * Cuando: se muestra su tarjeta.
     * Entonces: debe aparecer su título, descripción, fecha, estado y progreso.
     */
    @Test
    fun testIntegridadDatosKotlin() {
        val actividad = ActividadFormativa(
            id = 2,
            titulo = "Programación en Kotlin",
            descripcion = "Variables, funciones, colecciones y clases en Kotlin.",
            fecha = "12 de agosto",
            estado = "Completada",
            progreso = 100
        )
        assertEquals("Programación en Kotlin", actividad.titulo)
        assertEquals("Variables, funciones, colecciones y clases en Kotlin.", actividad.descripcion)
        assertEquals("12 de agosto", actividad.fecha)
        assertEquals("Completada", actividad.estado)
        assertEquals(100, actividad.progreso)
    }

    /**
     * Al seleccionar un estado, solamente deben aparecer las actividades correspondientes.
     * La cantidad de actividades mostradas debe actualizarse según el filtro seleccionado.
     */
    @Test
    fun testFiltradoPorEstadoYCantidad() = runTest {
        val actividades = listOf(
            ActividadFormativa(1, "A1", "D1", "F1", "Completada", 100),
            ActividadFormativa(2, "A2", "D2", "F2", "En proceso", 50)
        )
        repository.emitir(actividades)

        // Filtrar por Completadas
        viewModel.cambiarFiltro("Completadas")
        
        var result = viewModel.actividadesFiltradas.value
        assertEquals(1, result.size)
        assertEquals("Completada", result[0].estado)

        // Filtrar por Todas y verificar cantidad
        viewModel.cambiarFiltro("Todas")
        result = viewModel.actividadesFiltradas.value
        assertEquals(2, result.size)
    }

    /**
     * Si no existen actividades para el filtro seleccionado, debe aparecer un mensaje indicando que no hay actividades con ese estado.
     * (Validamos que la lógica retorne lista vacía para activar el mensaje).
     */
    @Test
    fun testSinActividadesParaFiltro() = runTest {
        val actividades = listOf(
            ActividadFormativa(1, "A1", "D1", "F1", "Pendiente", 0)
        )
        repository.emitir(actividades)

        viewModel.cambiarFiltro("Completadas")
        val result = viewModel.actividadesFiltradas.value
        assertTrue("La lista debe estar vacía para mostrar el mensaje de error en UI", result.isEmpty())
    }
}
