package com.example.miformacionctma.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.miformacionctma.data.local.entity.EvidenciaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EvidenciaDao {


    @Query("SELECT * FROM evidencias WHERE actividadId = :actividadId LIMIT 1")
    fun obtenerEvidenciaPorActividad(actividadId: Long): Flow<EvidenciaEntity?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarOActualizarEvidencia(evidencia: EvidenciaEntity): Long


    @Query("DELETE FROM evidencias WHERE actividadId = :actividadId")
    suspend fun eliminarEvidenciaPorActividad(actividadId: Long)


    @Query("UPDATE evidencias SET estadoSincronizacion = :nuevoEstado WHERE id = :evidenciaId")
    suspend fun actualizarEstadoSincronizacion(evidenciaId: Long, nuevoEstado: String)
}