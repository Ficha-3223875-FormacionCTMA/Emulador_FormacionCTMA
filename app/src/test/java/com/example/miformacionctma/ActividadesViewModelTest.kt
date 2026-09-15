package com.example.miformacionctma

import com.example.miformacionctma.data.preferences.PreferenciasRepository
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad
import com.example.miformacionctma.domain.model.PreferenciasUsuario
import com.example.miformacionctma.ui.actividades.ActividadesViewModel
import com.example.miformacionctma.ui.actividades.EstadoPantallaActividades
import com.example.miformacionctma.ui.actividades.EstadoOperacionActividades
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ActividadesViewModel
    private lateinit var repository: FakeActividadRepository
    private lateinit var fakePrefs: FakePreferenciasRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeActividadRepository()
        fakePrefs = FakePreferenciasRepository()
        viewModel = ActividadesViewModel(repository, fakePrefs)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_cargaTotalDeActividadesYEsVacioInicialmente() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect { } }

        repository.total = 3
        viewModel.actualizarTotal()

        val state = viewModel.uiState.filter { it.totalActividades == 3 }.first()
        assertEquals(3, state.totalActividades)
        assertEquals(EstadoPantallaActividades.VACIO, state.estadoPantalla)
    }

    @Test
    fun conActividades_cambiaAEstadoContenido() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect { } }

        val lista = listOf(
            Actividad(1L, "Estudiar Scrum", "Ver ceremonias", 50, EstadoActividad.EN_PROCESO, 1000L)
        )
        repository.emitir(lista)

        val state = viewModel.uiState.first { it.actividades.isNotEmpty() }
        assertEquals(EstadoPantallaActividades.CONTENIDO, state.estadoPantalla)
        assertEquals(1, state.actividades.size)
    }

    @Test
    fun onCambiarOrdenProgreso_actualizaPreferencia() = runTest {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect { } }

        val inicial = viewModel.uiState.value.ordenProgresoDesc
        viewModel.onCambiarOrdenProgreso()

        assertNotEquals(inicial, fakePrefs.ultimaOrdenDesc)
    }

    @Test
    fun guardarActividad_multiplesLlamadasSimultaneas_soloGuardaUnaVez() = runTest {
        val actividad = Actividad(nombre = "Test Concurrente", descripcion = "", progreso = 10, estado = EstadoActividad.PENDIENTE, fechaCreacion = 0)
        repository.delayMillis = 100

        viewModel.guardarActividad(actividad)
        viewModel.guardarActividad(actividad)

        advanceUntilIdle()

        assertEquals(1, repository.guardarLlamadas)
        assertEquals(EstadoOperacionActividades.EXITOSA, viewModel.uiState.value.estadoOperacion)
    }
}

class FakeActividadRepository : ActividadRepository {
    var total = 0
    var guardarLlamadas = 0
    var delayMillis = 0L
    private val flow = MutableStateFlow<List<Actividad>>(emptyList())

    fun emitir(lista: List<Actividad>) {
        flow.value = lista
    }

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

class FakePreferenciasRepository : PreferenciasRepository() {
    private val _prefsFlow = MutableStateFlow(PreferenciasUsuario())
    var ultimaOrdenDesc: Boolean? = null
    var ultimoEstadoFiltro: String? = null

    override val preferencias: Flow<PreferenciasUsuario> = _prefsFlow

    override suspend fun actualizarActividadesOrdenDesc(desc: Boolean) {
        ultimaOrdenDesc = desc
        _prefsFlow.value = _prefsFlow.value.copy(actividadesOrdenDesc = desc)
    }

    override suspend fun actualizarActividadesFiltroEstado(estado: String?) {
        ultimoEstadoFiltro = estado
        _prefsFlow.value = _prefsFlow.value.copy(actividadesFiltroEstado = estado)
    }
}
