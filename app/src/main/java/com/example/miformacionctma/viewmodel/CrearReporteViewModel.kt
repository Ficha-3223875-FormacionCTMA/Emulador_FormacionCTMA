package com.example.miformacionctma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.repository.ReporteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CrearUiState(
    val titulo: String = "",
    val errorTitulo: String? = null,
    val guardando: Boolean = false,
    val guardadoId: Int? = null
)

class CrearReporteViewModel(
    private val repository: ReporteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrearUiState())

    val uiState: StateFlow<CrearUiState> =
        _uiState.asStateFlow()

    fun actualizarTitulo(valor: String) {
        val nuevoTitulo = valor.take(80)

        _uiState.update {
            it.copy(
                titulo = nuevoTitulo,
                errorTitulo = if (nuevoTitulo.length >= 4) {
                    null
                } else {
                    it.errorTitulo
                }
            )
        }
    }

    fun guardar() {
        val titulo = _uiState.value.titulo.trim()

        if (titulo.isBlank()) {
            _uiState.update {
                it.copy(errorTitulo = "El título es obligatorio")
            }
            return
        }

        if (titulo.length < 4) {
            _uiState.update {
                it.copy(errorTitulo = "El título debe tener al menos 4 caracteres")
            }
            return
        }

        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())

        val reporte = ActividadFormativa(
            id = 0,
            titulo = titulo,
            descripcion = "",
            fecha = fechaHoy,
            estado = "Pendiente",
            progreso = 0
        )

        _uiState.update {
            it.copy(guardando = true, errorTitulo = null)
        }

        viewModelScope.launch {
            repository.agregar(reporte)
            _uiState.update {
                it.copy(guardando = false, guardadoId = 0)
            }
        }
    }
}