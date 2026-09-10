package com.example.miformacionctma.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    // Obtiene todas las tareas guardadas en tiempo real
    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun obtenerTodasLasTareas(): Flow<List<TareaEntity>>

    // Inserta una nueva tarea enviada desde el formulario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(tarea: TareaEntity)

    // Elimina una tarea existente de la base de datos
    @Delete
    suspend fun eliminarTarea(tarea: TareaEntity)
}