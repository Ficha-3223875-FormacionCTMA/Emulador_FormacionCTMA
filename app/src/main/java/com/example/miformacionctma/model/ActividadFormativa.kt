package com.example.miformacionctma.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividades")
data class ActividadFormativa(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "nombre")
    val titulo: String,
    val descripcion: String = "", // <-- Valor por defecto
    val fecha: String,
    val estado: String,
    val progreso: Int,
    val competencia: String = "" // <-- Valor por defecto
)