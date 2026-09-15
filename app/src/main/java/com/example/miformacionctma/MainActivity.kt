package com.example.miformacionctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.miformacionctma.data.local.AppDatabase
import com.example.miformacionctma.data.preferences.UserPreferencesRepository
import com.example.miformacionctma.data.remote.api.NetworkModule
import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.repository.ReporteRepository
import com.example.miformacionctma.repository.RoomReporteRepository
import com.example.miformacionctma.ui.screens.CrearReporteScreen
import com.example.miformacionctma.ui.screens.PantallaActividades
import com.example.miformacionctma.ui.screens.actividadesEjemplo
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme
import com.example.miformacionctma.viewmodel.CrearReporteViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SyncState {
    object Inactivo : SyncState
    object Cargando : SyncState
    object Exito : SyncState
    data class Error(val mensaje: String) : SyncState
}

sealed interface PantallaActividadesUiState {
    object Cargando : PantallaActividadesUiState
    data class Exito(
        val actividades: List<ActividadFormativa>,
        val filtro: String,
        val syncState: SyncState = SyncState.Inactivo
    ) : PantallaActividadesUiState
    data class Error(val mensaje: String) : PantallaActividadesUiState
}

class PantallaActividadesViewModel(
    private val repository: ReporteRepository,
    private val preferences: UserPreferencesRepository
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Inactivo)

    val uiState: StateFlow<PantallaActividadesUiState> = combine(
        repository.reportes,
        preferences.filtroEstado,
        _syncState
    ) { reportes, filtro, sync ->
        val filtradas = when (filtro) {
            "Completadas" -> reportes.filter { it.estado == "Completada" }
            "En proceso" -> reportes.filter { it.estado == "En proceso" }
            "Pendientes" -> reportes.filter { it.estado == "Pendiente" }
            else -> reportes
        }
        PantallaActividadesUiState.Exito(filtradas, filtro, sync) as PantallaActividadesUiState
    }.catch {
        emit(PantallaActividadesUiState.Error("Error al cargar datos"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PantallaActividadesUiState.Cargando
    )

    init {
        sincronizar()
    }

    fun sincronizar() {
        viewModelScope.launch {
            _syncState.value = SyncState.Cargando
            try {
                repository.sincronizar()
                _syncState.value = SyncState.Exito
            } catch (e: Exception) {
                _syncState.value = SyncState.Error("Fallo la sincronización, usando caché local")
            }
        }
    }

    fun cambiarFiltro(nuevoFiltro: String) {
        viewModelScope.launch {
            preferences.guardarFiltro(nuevoFiltro)
        }
    }

    fun completarActividad(actividad: ActividadFormativa) {
        viewModelScope.launch {
            val actualizada = actividad.copy(
                estado = "Completada",
                progreso = 100
            )
            repository.actualizar(actualizada)
        }
    }
}

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "miformacion_database"
        ).build()
    }

    private val repository by lazy {
        RoomReporteRepository(
            db = database,
            api = NetworkModule.apiService
        )
    }

    private val preferences by lazy {
        UserPreferencesRepository(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val scope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                scope.launch {
                    val actual = repository.reportes.first()
                    if (actual.isEmpty()) {
                        actividadesEjemplo.forEach {
                            repository.agregar(it)
                        }
                    }
                }
            }

            MiFormacionCTMATheme {
                var pantallaActual by remember { mutableStateOf("LISTA") }

                val viewModelFactory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return when {
                            modelClass.isAssignableFrom(PantallaActividadesViewModel::class.java) -> {
                                PantallaActividadesViewModel(repository, preferences) as T
                            }
                            modelClass.isAssignableFrom(CrearReporteViewModel::class.java) -> {
                                CrearReporteViewModel(repository) as T
                            }
                            else -> throw IllegalArgumentException("ViewModel desconocido")
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (pantallaActual) {
                        "LISTA" -> {
                            val actividadesViewModel: PantallaActividadesViewModel = viewModel(factory = viewModelFactory)
                            
                            PantallaActividades(
                                viewModel = actividadesViewModel,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        "CREAR" -> {
                            val crearViewModel: CrearReporteViewModel = viewModel(factory = viewModelFactory)

                            CrearReporteScreen(
                                viewModel = crearViewModel,
                                onVolver = { pantallaActual = "LISTA" },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
