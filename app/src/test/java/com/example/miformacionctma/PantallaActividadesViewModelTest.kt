package com.example.miformacionctma

import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.repository.ReporteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FakeReporteRepository : ReporteRepository {
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
class PantallaActividadesViewModelTest {

    private lateinit var viewModel: PantallaActividadesViewModel
    private lateinit var repository: FakeReporteRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeReporteRepository()
        viewModel = PantallaActividadesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al seleccionar filtro Completadas solo aparecen actividades completadas`() = runTest {
        // Dado
        val actividades = listOf(
            ActividadFormativa(1, "A1", "D1", "F1", "Completada", 100),
            ActividadFormativa(2, "A2", "D2", "F2", "En proceso", 50)
        )
        repository.emitir(actividades)

        // Cuando
        viewModel.cambiarFiltro("Completadas")
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces
        val result = viewModel.actividadesFiltradas.first()
        assertEquals(1, result.size)
        assertEquals("Completada", result[0].estado)
    }

    @Test
    fun `la cantidad de actividades se actualiza segun el filtro`() = runTest {
        // Dado
        val actividades = listOf(
            ActividadFormativa(1, "A1", "D1", "F1", "Completada", 100),
            ActividadFormativa(2, "A2", "D2", "F2", "Completada", 100),
            ActividadFormativa(3, "A3", "D3", "F3", "Pendiente", 0)
        )
        repository.emitir(actividades)

        // Cuando filtro por Completadas
        viewModel.cambiarFiltro("Completadas")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Entonces
        assertEquals(2, viewModel.actividadesFiltradas.first().size)

        // Cuando filtro por Todas
        viewModel.cambiarFiltro("Todas")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Entonces
        assertEquals(3, viewModel.actividadesFiltradas.first().size)
    }

    @Test
    fun `si no hay actividades para el filtro el resultado es vacio`() = runTest {
        // Dado
        val actividades = listOf(
            ActividadFormativa(1, "A1", "D1", "F1", "Completada", 100)
        )
        repository.emitir(actividades)

        // Cuando
        viewModel.cambiarFiltro("Pendientes")
        testDispatcher.scheduler.advanceUntilIdle()

        // Entonces
        val result = viewModel.actividadesFiltradas.first()
        assertEquals(0, result.size)
    }
}
