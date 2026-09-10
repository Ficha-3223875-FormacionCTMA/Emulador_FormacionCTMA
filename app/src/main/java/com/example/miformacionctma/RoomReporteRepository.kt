package com.example.miformacionctma

import com.example.miformacionctma.model.TareaDao
import com.example.miformacionctma.model.TareaEntity
import kotlinx.coroutines.flow.Flow

class TareaRepository(private val dao: TareaDao) {

    // Obtiene el flujo de tareas desde la base de datos Room
    val todasLasTareas: Flow<List<TareaEntity>> = dao.obtenerTodasLasTareas()

    // Inserta una tarea en la base de datos
    suspend fun insertar(tarea: TareaEntity) {
        dao.insertarTarea(tarea)
    }

    // Elimina una tarea de la base de datos
    suspend fun eliminar(tarea: TareaEntity) {
        dao.eliminarTarea(tarea)
    }
}