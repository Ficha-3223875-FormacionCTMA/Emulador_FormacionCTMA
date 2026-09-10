package com.example.miformacionctma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miformacionctma.model.TareaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TareaViewModel(
    private val repo: TareaRepository
) : ViewModel() {

    // Exponer la lista de tareas observada desde Room en tiempo real
    val tareas: StateFlow<List<TareaEntity>> = repo.todasLasTareas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Insertar una nueva tarea en la base de datos
    fun agregarTarea(titulo: String, descripcion: String) {
        if (titulo.isNotBlank()) {
            viewModelScope.launch {
                repo.insertar(TareaEntity(titulo = titulo, descripcion = descripcion))
            }
        }
    }

    // Eliminar una tarea de la base de datos
    fun eliminarTarea(tarea: TareaEntity) {
        viewModelScope.launch {
            repo.eliminar(tarea)
        }
    }
}