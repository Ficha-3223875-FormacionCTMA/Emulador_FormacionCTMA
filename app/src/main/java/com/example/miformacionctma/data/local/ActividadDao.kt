package com.example.miformacionctma.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {
    // Consulta reactiva filtrando por query sobre la columna 'nombre'
    @Query("""
        SELECT * FROM actividades 
        WHERE (:query = '' OR nombre LIKE '%' || :query || '%')
        ORDER BY nombre ASC
    """)
    fun obtenerActividades(query: String): Flow<List<ActividadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(actividades: List<ActividadEntity>)

    @Query("DELETE FROM actividades")
    suspend fun limpiarTabla()

    // Sincronización atómica para la red
    @Transaction
    suspend fun sincronizarActividades(actividades: List<ActividadEntity>) {
        limpiarTabla()
        insertarTodas(actividades)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(actividad: ActividadEntity)

    @Delete
    suspend fun eliminar(actividad: ActividadEntity)
}