package com.example.miformacionctma.ui.actividades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.miformacionctma.di.AppContainer
import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.data.preferences.PreferenciasRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadesViewModel(
    private val repository: ActividadRepository,
    private val preferenciasRepository: PreferenciasRepository
) : ViewModel() {

    private val _totalActividades = MutableStateFlow(0)
    private val _estadoOperacion = MutableStateFlow(EstadoOperacionActividades.INACTIVA)
    private val _errorMensaje = MutableStateFlow<String?>(null)

    private data class ParamsActividades(
        val prefs: com.example.miformacionctma.domain.model.PreferenciasUsuario,
        val total: Int,
        val operacion: EstadoOperacionActividades,
        val errorMsg: String?
    )

    val uiState: StateFlow<ActividadesUiState> = combine(
        preferenciasRepository.preferencias,
        _totalActividades,
        _estadoOperacion,
        _errorMensaje
    ) { prefs, total, operacion, errorMsg ->
        ParamsActividades(prefs, total, operacion, errorMsg)
    }.flatMapLatest { params ->
        val filtroEnum = params.prefs.actividadesFiltroEstado?.let {
            try { EstadoActividad.valueOf(it) } catch (e: Exception) { null }
        }
        val ordenDesc = params.prefs.actividadesOrdenDesc

        val flow = if (filtroEnum != null) {
            repository.observarPorEstado(filtroEnum)
        } else {
            repository.observarOrdenadasPorProgreso(ordenDesc)
        }

        flow.map { list ->
            val listaOrdenada = if (filtroEnum != null) {
                if (ordenDesc) list.sortedByDescending { it.progreso } else list.sortedBy { it.progreso }
            } else {
                list
            }

            val pantallaFinal = if (listaOrdenada.isEmpty()) {
                EstadoPantallaActividades.VACIO
            } else {
                EstadoPantallaActividades.CONTENIDO
            }

            ActividadesUiState(
                actividades = listaOrdenada,
                totalActividades = params.total,
                filtroEstado = filtroEnum,
                ordenProgresoDesc = ordenDesc,
                estadoPantalla = pantallaFinal,
                estadoOperacion = params.operacion,
                errorMensaje = params.errorMsg
            )
        }.catch { e ->
            emit(
                ActividadesUiState(
                    totalActividades = params.total,
                    filtroEstado = filtroEnum,
                    ordenProgresoDesc = ordenDesc,
                    estadoPantalla = EstadoPantallaActividades.ERROR,
                    estadoOperacion = params.operacion,
                    errorMensaje = e.message ?: "Error desconocido"
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ActividadesUiState(estadoPantalla = EstadoPantallaActividades.CARGANDO)
    )

    init {
        verificarYCrearActividadesSemilla()
    }

    private fun verificarYCrearActividadesSemilla() {
        viewModelScope.launch {
            try {
                val cantidadActual = repository.contar()
                if (cantidadActual == 0) {
                    val nombresSemilla = listOf(
                        "Configuración de Entorno", "Diseño de Interfaz", "Persistencia con Room",
                        "Migración de Base de Datos", "Configuración de DataStore", "Concurrencia con Corrutinas",
                        "Flujos Reactivos Asíncronos", "Integración de StateFlow", "Arquitectura Main-Safe",
                        "Manejo de Errores Robustos", "Pruebas Unitarias de Capa Datos", "Pruebas Unitarias de UI",
                        "Optimización de Rendimiento", "Refactorización Limpia", "Validación Completa Final"
                    )
                    
                    nombresSemilla.forEachIndexed { index, nombre ->
                        val estado = when (index % 3) {
                            0 -> EstadoActividad.PENDIENTE
                            1 -> EstadoActividad.EN_PROCESO
                            else -> EstadoActividad.COMPLETADA
                        }
                        val progreso = when (estado) {
                            EstadoActividad.PENDIENTE -> 0
                            EstadoActividad.EN_PROCESO -> 45
                            EstadoActividad.COMPLETADA -> 100
                        }
                        repository.guardar(
                            Actividad(
                                nombre = nombre,
                                descripcion = "Descripción automatizada para la tarea semilla número ${index + 1}",
                                progreso = progreso,
                                estado = estado,
                                fechaCreacion = System.currentTimeMillis() + (index * 1000)
                            )
                        )
                    }
                }
                actualizarTotal()
            } catch (e: Exception) {
                // Captura silenciosa
            }
        }
    }

    fun actualizarTotal() {
        viewModelScope.launch {
            try {
                _totalActividades.value = repository.contar()
            } catch (e: Exception) {
                // Silencioso o registrado si falla el conteo inicial
            }
        }
    }

    fun onCambiarOrdenProgreso() {
        viewModelScope.launch {
            preferenciasRepository.actualizarActividadesOrdenDesc(!uiState.value.ordenProgresoDesc)
        }
    }

    fun onFiltrarEstado(estado: EstadoActividad?) {
        viewModelScope.launch {
            preferenciasRepository.actualizarActividadesFiltroEstado(estado?.name)
        }
    }

    fun resetEstadoOperacion() {
        _estadoOperacion.value = EstadoOperacionActividades.INACTIVA
        _errorMensaje.value = null
    }

    fun guardarActividad(actividad: Actividad) {
        _estadoOperacion.value = EstadoOperacionActividades.EN_CURSO
        _errorMensaje.value = null
        viewModelScope.launch {
            try {
                repository.guardar(actividad)
                actualizarTotal()
                _estadoOperacion.value = EstadoOperacionActividades.EXITOSA
            } catch (e: Exception) {
                _errorMensaje.value = e.message ?: "Error al guardar actividad"
                _estadoOperacion.value = EstadoOperacionActividades.FALLIDA
            }
        }
    }

    fun eliminarActividad(actividad: Actividad) {
        _estadoOperacion.value = EstadoOperacionActividades.EN_CURSO
        _errorMensaje.value = null
        viewModelScope.launch {
            try {
                repository.eliminar(actividad)
                actualizarTotal()
                _estadoOperacion.value = EstadoOperacionActividades.EXITOSA
            } catch (e: Exception) {
                _errorMensaje.value = e.message ?: "Error al eliminar actividad"
                _estadoOperacion.value = EstadoOperacionActividades.FALLIDA
            }
        }
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer {
                ActividadesViewModel(
                    repository = container.actividadRepository,
                    preferenciasRepository = container.preferenciasRepository
                )
            }
        }
    }
}
