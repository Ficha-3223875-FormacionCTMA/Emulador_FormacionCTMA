package com.example.miformacionctma.ui.reportes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.miformacionctma.di.AppContainer
import com.example.miformacionctma.domain.model.OrdenReportes
import com.example.miformacionctma.domain.model.Reporte
import com.example.miformacionctma.data.preferences.PreferenciasRepository
import com.example.miformacionctma.data.repository.ReporteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ReportesViewModel(
    private val reporteRepository: ReporteRepository,
    private val preferenciasRepository: PreferenciasRepository
) : ViewModel() {

    private val _textoBusqueda = MutableStateFlow("")

    val uiState: StateFlow<ReportesUiState> = combine(
        reporteRepository.observarCategorias(),
        preferenciasRepository.preferencias,
        _textoBusqueda
    ) { categorias, prefs, texto ->
        Triple(categorias, prefs, texto)
    }.flatMapLatest { (categorias, prefs, texto) ->
        reporteRepository.observarReportes(prefs.categoriaFiltroId, texto, prefs.orden)
            .map { reportes ->
                ReportesUiState(
                    reportes = reportes,
                    categorias = categorias,
                    textoBusqueda = texto,
                    categoriaFiltroId = prefs.categoriaFiltroId,
                    orden = prefs.orden,
                    cargando = false
                )
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReportesUiState(cargando = true)
    )

    fun onTextoBusquedaCambia(nuevoTexto: String) {
        _textoBusqueda.value = nuevoTexto
    }

    fun onOrdenSeleccionado(orden: OrdenReportes) {
        viewModelScope.launch {
            preferenciasRepository.actualizarOrden(orden)
        }
    }

    fun onCategoriaFiltroSeleccionada(categoriaId: Long?) {
        viewModelScope.launch {
            preferenciasRepository.actualizarCategoriaFiltro(categoriaId)
        }
    }

    fun alternarResuelto(reporte: Reporte) {
        viewModelScope.launch {
            reporteRepository.guardar(reporte.copy(resuelto = !reporte.resuelto))
        }
    }

    fun eliminarReporte(reporte: Reporte) {
        viewModelScope.launch {
            reporteRepository.eliminar(reporte)
        }
    }

    fun guardarReporte(titulo: String, descripcion: String, categoriaId: Long?) {
        viewModelScope.launch {
            reporteRepository.guardar(
                Reporte(
                    titulo = titulo,
                    descripcion = descripcion,
                    categoriaId = categoriaId ?: 0L,
                    fechaCreacion = System.currentTimeMillis()
                )
            )
        }
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer {
                ReportesViewModel(
                    reporteRepository = container.reporteRepository,
                    preferenciasRepository = container.preferenciasRepository
                )
            }
        }
    }
}
