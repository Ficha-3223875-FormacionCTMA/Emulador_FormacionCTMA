package com.example.miformacionctma.ui.actividades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.miformacionctma.di.AppContainer
import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad
import com.example.miformacionctma.data.repository.ActividadRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadesViewModel(
    private val repository: ActividadRepository
) : ViewModel() {

    private val _filtroEstado = MutableStateFlow<EstadoActividad?>(null)
    private val _ordenProgresoDesc = MutableStateFlow(true)
    private val _totalActividades = MutableStateFlow(0)

    val uiState: StateFlow<ActividadesUiState> = combine(
        _filtroEstado,
        _ordenProgresoDesc,
        _totalActividades
    ) { filtro, orden, total ->
        Triple(filtro, orden, total)
    }.flatMapLatest { (filtro, orden, total) ->
        val flow = if (filtro != null) {
            repository.observarPorEstado(filtro)
        } else {
            repository.observarOrdenadasPorProgreso(orden)
        }
        flow.map { list ->
            ActividadesUiState(
                actividades = list,
                totalActividades = total,
                filtroEstado = filtro,
                ordenProgresoDesc = orden,
                cargando = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ActividadesUiState(cargando = true)
    )

    init {
        actualizarTotal()
    }

    fun actualizarTotal() {
        viewModelScope.launch {
            _totalActividades.value = repository.contar()
        }
    }

    fun onCambiarOrdenProgreso() {
        _ordenProgresoDesc.value = !_ordenProgresoDesc.value
    }

    fun onFiltrarEstado(estado: EstadoActividad?) {
        _filtroEstado.value = estado
    }
    
    private var guardando = false

    fun guardarActividad(actividad: Actividad) {
        if (guardando) return
        guardando = true
        viewModelScope.launch {
            try {
                repository.guardar(actividad)
                actualizarTotal()
            } finally {
                guardando = false
            }
        }
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer {
                ActividadesViewModel(repository = container.actividadRepository)
            }
        }
    }
}
