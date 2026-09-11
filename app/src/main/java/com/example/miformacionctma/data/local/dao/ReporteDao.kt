package com.example.miformacionctma.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.miformacionctma.data.local.entity.ReporteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    @Insert
    suspend fun insertar(reporte: ReporteEntity): Long

    @Update
    suspend fun actualizar(reporte: ReporteEntity)

    @Delete
    suspend fun eliminar(reporte: ReporteEntity)

    @Query("SELECT * FROM reportes ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reportes WHERE id = :id")
    suspend fun obtenerPorId(id: Int): ReporteEntity?

    @Query("SELECT * FROM reportes WHERE estado = :estado ORDER BY id DESC")
    fun filtrarPorEstado(estado: String): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reportes WHERE categoriaId = :categoriaId ORDER BY id DESC")
    fun obtenerPorCategoria(categoriaId: Int): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reportes WHERE titulo LIKE '%' || :texto || '%' OR descripcion LIKE '%' || :texto || '%' ORDER BY id DESC")
    fun buscarPorTexto(texto: String): Flow<List<ReporteEntity>>
}