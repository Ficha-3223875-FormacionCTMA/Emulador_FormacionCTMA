package com.example.miformacionctma.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.miformacionctma.data.local.entity.ReporteConCategoriaEntity
import com.example.miformacionctma.data.local.entity.ReporteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    // --- Alta ---
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(reporte: ReporteEntity): Long

    // --- Actualización ---
    @Update
    suspend fun actualizar(reporte: ReporteEntity)

    // --- Eliminación ---
    @Delete
    suspend fun eliminar(reporte: ReporteEntity)

    @Query("DELETE FROM reportes WHERE id = :id")
    suspend fun eliminarPorId(id: Long)

    // --- Lectura observable: fuente única de verdad de la lista ---
    @Query(
        """
        SELECT * FROM reportes
        WHERE (:categoriaId IS NULL OR categoriaId = :categoriaId)
          AND (:texto = '' OR titulo LIKE '%' || :texto || '%' OR descripcion LIKE '%' || :texto || '%')
        ORDER BY
          CASE WHEN :orden = 'TITULO_ASC' THEN titulo END ASC,
          CASE WHEN :orden = 'FECHA_ASC' THEN fechaCreacion END ASC,
          CASE WHEN :orden = 'FECHA_DESC' THEN fechaCreacion END DESC
        """
    )
    fun observarReportes(
        categoriaId: Long?,
        texto: String,
        orden: String
    ): Flow<List<ReporteEntity>>

    // --- Consulta por id (detalle) ---
    @Query("SELECT * FROM reportes WHERE id = :id")
    fun observarPorId(id: Long): Flow<ReporteEntity?>

    // --- Búsqueda simple por texto (usada también por pruebas del DAO) ---
    @Query(
        "SELECT * FROM reportes WHERE titulo LIKE '%' || :texto || '%' " +
                "OR descripcion LIKE '%' || :texto || '%' ORDER BY fechaCreacion DESC"
    )
    suspend fun buscar(texto: String): List<ReporteEntity>

    // --- Consulta transaccional de la relación reporte–categoría ---
    @Transaction
    @Query("SELECT * FROM reportes WHERE id = :id")
    suspend fun obtenerConCategoria(id: Long): ReporteConCategoriaEntity?

    @Transaction
    @Query("SELECT * FROM reportes ORDER BY fechaCreacion DESC")
    fun observarTodosConCategoria(): Flow<List<ReporteConCategoriaEntity>>

    // --- Utilidades para pruebas ---
    @Query("SELECT COUNT(*) FROM reportes")
    suspend fun contar(): Int
}