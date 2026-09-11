package com.example.miformacionctma.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miformacionctma.data.preferences.PreferenciasRepository
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.model.ActividadFormativa
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadesViewModel(
    private val repository: ActividadRepository,
    private val preferenciasRepository: PreferenciasRepository
) : ViewModel() {

    private val _busquedaQuery = MutableStateFlow("")
    val busquedaQuery: StateFlow<String> = _busquedaQuery.asStateFlow()

    private val _operacionState = MutableStateFlow<OperacionUiState>(OperacionUiState.Inactiva)
    val operacionState: StateFlow<OperacionUiState> = _operacionState.asStateFlow()

    // Evento de refresco/reintento para la UI (CA-05)
    private val _refrescarTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val uiState: StateFlow<ListadoUiState> = combine(
        preferenciasRepository.obtenerFiltroCompetencia(),
        preferenciasRepository.obtenerOrdenamiento(),
        _busquedaQuery,
        _refrescarTrigger.onStart { emit(Unit) }
    ) { filtro, orden, query, _ ->
        Triple(filtro, orden, query)
    }.flatMapLatest { (filtro, orden, query) ->
        // Búsqueda cancelable automática al escribir rápido (CA-04)
        repository.obtenerActividades(filtro, orden, query)
    }.map { lista ->
        if (lista.isEmpty()) ListadoUiState.Vacio else ListadoUiState.Contenido(lista)
    }.catch { throwable ->
        if (throwable is CancellationException) throw throwable
        emit(ListadoUiState.Error(throwable.localizedMessage ?: "Error inesperado"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000), // Mantiene 5 al rotar (CA-07)
        initialValue = ListadoUiState.Cargando
    )

    fun onBusquedaChanged(nuevaBusqueda: String) {
        _busquedaQuery.value = nuevaBusqueda
    }

    fun reintentar() {
        _refrescarTrigger.tryEmit(Unit)
    }

    fun guardarActividad(actividad: ActividadFormativa) {
        viewModelScope.launch {
            _operacionState.value = OperacionUiState.EnCurso
            try {
                repository.insertarActividad(actividad)
                _operacionState.value = OperacionUiState.Exitosa
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _operacionState.value = OperacionUiState.Fallida(e.localizedMessage ?: "Error al guardar")
            }
        }
    }

    fun eliminarActividad(actividad: ActividadFormativa) {
        viewModelScope.launch {
            _operacionState.value = OperacionUiState.EnCurso
            try {
                repository.eliminarActividad(actividad)
                _operacionState.value = OperacionUiState.Exitosa
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _operacionState.value = OperacionUiState.Fallida(e.localizedMessage ?: "Error al eliminar")
            }
        }
    }

    fun resetOperacionState() {
        _operacionState.value = OperacionUiState.Inactiva
    }
}