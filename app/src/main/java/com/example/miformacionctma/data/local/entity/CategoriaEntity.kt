package com.example.miformacionctma.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categorias",
    indices = [Index(value = ["nombre"], unique = true)]
)
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "nombre")
    val nombre: String
)