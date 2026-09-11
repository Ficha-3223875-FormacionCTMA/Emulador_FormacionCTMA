package com.example.miformacionctma.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.miformacionctma.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(categoria: CategoriaEntity): Long

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun observarTodas(): Flow<List<CategoriaEntity>>

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun obtenerPorId(id: Long): CategoriaEntity?

    @Query("SELECT COUNT(*) FROM categorias")
    suspend fun contar(): Int
}