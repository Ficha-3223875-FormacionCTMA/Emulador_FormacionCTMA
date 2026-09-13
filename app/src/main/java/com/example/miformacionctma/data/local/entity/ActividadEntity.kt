package com.example.miformacionctma.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividades")
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val descripcion: String,
    val progreso: Int,
    val estado: String, // PENDIENTE, EN_PROCESO, COMPLETADA
    val fechaCreacion: Long
)
