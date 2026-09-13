package com.example.miformacionctma

import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad
import com.example.miformacionctma.ui.actividades.ActividadesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ActividadesViewModel
    private lateinit var repository: FakeActividadRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeActividadRepository()
        viewModel = ActividadesViewModel(repository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_cargaTotalDeActividades() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect { } }

        repository.total = 5
        viewModel.actualizarTotal()
        
        val state = viewModel.uiState.filter { !it.cargando }.first { it.totalActividades == 5 }
        assertEquals(5, state.totalActividades)
    }

    @Test
    fun onCambiarOrdenProgreso_actualizaElEstado() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect { } }

        val inicial = viewModel.uiState.value.ordenProgresoDesc
        viewModel.onCambiarOrdenProgreso()
        
        assertEquals(!inicial, viewModel.uiState.value.ordenProgresoDesc)
    }

    @Test
    fun guardarActividad_multiplesLlamadasSimultaneas_soloGuardaUnaVez() = runTest {
        val actividad = Actividad(nombre = "Test", descripcion = "", progreso = 0, estado = EstadoActividad.PENDIENTE, fechaCreacion = 0)
        repository.delayMillis = 100 
        
        viewModel.guardarActividad(actividad)
        viewModel.guardarActividad(actividad)
        
        advanceUntilIdle()
        
        assertEquals(1, repository.guardarLlamadas)
    }
}

class FakeActividadRepository : ActividadRepository {
    var total = 0
    var guardarLlamadas = 0
    var delayMillis = 0L
    private val flow = MutableStateFlow<List<Actividad>>(emptyList())
    
    override fun observarTodas(): Flow<List<Actividad>> = flow
    override fun observarOrdenadasPorProgreso(desc: Boolean): Flow<List<Actividad>> = flow
    override fun observarPorEstado(estado: EstadoActividad): Flow<List<Actividad>> = flow
    override suspend fun contar(): Int = total
    override suspend fun guardar(actividad: Actividad): Long {
        if (delayMillis > 0) kotlinx.coroutines.delay(delayMillis)
        guardarLlamadas++
        return 0L
    }
    override suspend fun eliminar(actividad: Actividad) {}
}
