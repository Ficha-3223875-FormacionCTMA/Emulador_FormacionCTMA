package com.example.miformacionctma.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miformacionctma.data.preferences.PreferenciasRepository // <-- IMPORTANTE
import com.example.miformacionctma.data.repository.ActividadRepository       // <-- IMPORTANTE
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

    val uiState: StateFlow<ListadoUiState> = combine(
        preferenciasRepository.obtenerFiltroCompetencia(),
        preferenciasRepository.obtenerOrdenamiento(),
        _busquedaQuery
    ) { filtro, orden, query ->
        Triple(filtro, orden, query)
    }.flatMapLatest { (filtro, orden, query) ->
        repository.obtenerActividades(filtro, orden, query)
    }.map { lista ->
        if (lista.isEmpty()) ListadoUiState.Vacio else ListadoUiState.Contenido(lista)
    }.catch { throwable ->
        if (throwable is CancellationException) throw throwable
        emit(ListadoUiState.Error(throwable.localizedMessage ?: "Error inesperado"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ListadoUiState.Cargando
    )

    fun onBusquedaChanged(nuevaBusqueda: String) {
        _busquedaQuery.value = nuevaBusqueda
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
}