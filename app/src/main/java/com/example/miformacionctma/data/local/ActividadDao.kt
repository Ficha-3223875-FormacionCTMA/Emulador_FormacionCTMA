package com.example.miformacionctma.data.local

import androidx.room.*
import com.example.miformacionctma.model.ActividadFormativa
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {
    // Al retornar Flow, Room notifica automáticamente cuando los datos cambian (CA-02)
    @Query("""
        SELECT * FROM actividades 
        WHERE (:filtro = '' OR competencia = :filtro)
        AND (:query = '' OR nombre LIKE '%' || :query || '%')
        ORDER BY 
            CASE WHEN :orden = 'ASC' THEN nombre END ASC,
            CASE WHEN :orden = 'DESC' THEN nombre END DESC
    """)
    fun obtenerActividades(filtro: String, orden: String, query: String): Flow<List<ActividadFormativa>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(actividad: ActividadFormativa)

    @Delete
    suspend fun eliminar(actividad: ActividadFormativa)
}