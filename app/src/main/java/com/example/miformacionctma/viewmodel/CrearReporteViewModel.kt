package com.example.miformacionctma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.miformacionctma.model.Reporte
import com.example.miformacionctma.repository.ReporteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class CrearUiState(
    val titulo: String = "",
    val errorTitulo: String? = null,
    val guardando: Boolean = false,
    val guardadoId: String? = null
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
                it.copy(
                    errorTitulo = "El título es obligatorio"
                )
            }
            return
        }

        if (titulo.length < 4) {
            _uiState.update {
                it.copy(
                    errorTitulo = "El título debe tener al menos 4 caracteres"
                )
            }
            return
        }

        val id = UUID.randomUUID().toString()

        val reporte = Reporte(
            id = id,
            titulo = titulo
        )

        _uiState.update {
            it.copy(
                guardando = true,
                errorTitulo = null
            )
        }

        repository.agregar(reporte)

        _uiState.update {
            it.copy(
                guardando = false,
                guardadoId = id
            )
        }
    }
}