package com.example.miformacionctma.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.miformacionctma.data.local.entity.ActividadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(actividad: ActividadEntity): Long

    @Update
    suspend fun actualizar(actividad: ActividadEntity)

    @Delete
    suspend fun eliminar(actividad: ActividadEntity)

    @Query("SELECT * FROM actividades ORDER BY fechaCreacion DESC")
    fun observarTodas(): Flow<List<ActividadEntity>>

    @Query("SELECT COUNT(*) FROM actividades")
    suspend fun contar(): Int

    @Query("SELECT * FROM actividades ORDER BY progreso DESC")
    fun observarOrdenadasPorProgresoDesc(): Flow<List<ActividadEntity>>

    @Query("SELECT * FROM actividades ORDER BY progreso ASC")
    fun observarOrdenadasPorProgresoAsc(): Flow<List<ActividadEntity>>
    
    @Query("SELECT * FROM actividades WHERE estado = :estado")
    fun observarPorEstado(estado: String): Flow<List<ActividadEntity>>
}
