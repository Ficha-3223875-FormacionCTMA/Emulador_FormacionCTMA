package com.example.miformacionctma.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla "reportes". Versión de esquema 2: agrega la columna "resuelto"
 * (ver Migrations.kt, MIGRATION_1_2) sobre el esquema original de la
 * Semana 6, que solo tenía id, titulo, descripcion, categoriaId y
 * fechaCreacion.
 */
@Entity(
    tableName = "reportes",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["categoriaId"])]
)
data class ReporteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "titulo")
    val titulo: String,
    @ColumnInfo(name = "descripcion")
    val descripcion: String,
    @ColumnInfo(name = "categoriaId")
    val categoriaId: Long?,
    @ColumnInfo(name = "fechaCreacion")
    val fechaCreacion: Long,
    @ColumnInfo(name = "resuelto", defaultValue = "0")
    val resuelto: Boolean = false
)