package com.example.miformacionctma.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {
    // Consulta reactiva filtrando por query sobre la columna 'titulo'
    @Query("""
        SELECT * FROM actividades
        WHERE titulo LIKE '%' || :query || '%'
        ORDER BY id ASC
    """)
    fun obtenerActividades(query: String): Flow<List<ActividadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(actividades: List<ActividadEntity>)

    @Query("DELETE FROM actividades")
    suspend fun limpiarTabla()

    // Sincronización inteligente: solo borra e inserta si la red trae datos reales
    @Transaction
    suspend fun sincronizarActividades(actividades: List<ActividadEntity>) {
        if (actividades.isNotEmpty()) {
            limpiarTabla()
            insertarTodas(actividades)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(actividad: ActividadEntity)

    @Delete
    suspend fun eliminar(actividad: ActividadEntity)
}